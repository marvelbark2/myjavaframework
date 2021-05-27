package ws.prospeak.myweb.framework.Illuminate.database.pool;


import ws.prospeak.myweb.framework.Illuminate.database.pool.config.DBConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.*;

public class ConnectionPoolManager extends AbstractPool implements BlockingPool {

    private BlockingQueue<Connection> mountedConnection;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private volatile boolean shutdownPool;
    private boolean isReturned = true;

    public ConnectionPoolManager() {
        shutdownPool = false;
    }

    public void init(int nPool) {
        mountedConnection = new LinkedBlockingQueue<>(nPool);
        for (int i = 0; i < nPool; i++) {
            Connection connection = connectionFactory();
            mountedConnection.add(connection);
        }
    }

    private Connection connectionFactory() {
        Connection connection = null;
        DBConfig config = DBConfig.Instance;
        String url = config.HOST;
        String user = config.USER;
        String pass = config.PASS;
        try {
            connection = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
        return connection;
    }

    @Override
    protected void handleInvalidReturn(Connection con) {
        throw new IllegalStateException("Error! Cannot put this Connection: " + con + " to the pool");
    }

    @Override
    protected void returnToPool(Connection connection) {
        if (isValid(connection))
            executor.submit(new ObjectReturner(mountedConnection, connection));
    }

    @Override
    protected boolean isValid(Connection connection) {
        boolean status = false;
        try {
            status = (!(connection == null || connection.isClosed()));
        } catch (SQLException throwables) {
            Thread.currentThread().interrupt();
        }
        return status;
    }

    @Override
    public Connection getConnection(long time, TimeUnit unit) {
        if (!shutdownPool) {
            Connection connection = null;
            try {
                connection = mountedConnection.poll(time, unit);
                if (!isValid(connection)) connection = connectionFactory();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            return connection;
        } else {
            throw new IllegalStateException("Pool is shutdown");
        }
    }

    @Override
    public int poolSize() {
        return mountedConnection.size();
    }

    @Override
    public Connection getConnection() {
        if (!shutdownPool) {
            Connection connection = null;
            try {
                connection = mountedConnection.take();
                if (!isValid(connection)) connection = connectionFactory();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            return connection;
        } else {
            throw new IllegalStateException("Pool is shutdown");
        }
    }

    @Override
    public void isReturnedTo(Boolean v) {
        this.isReturned = v;
    }

    @Override
    public void shutdown() {
        shutdownPool = true;
        executor.shutdownNow();
        closeConnections();
    }

    private void closeConnections() {
        mountedConnection.stream()
                .forEach(connection -> {
                    try {
                        connection.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                });
    }

    private class ObjectReturner implements Runnable {
        private BlockingQueue<Connection> queue;
        private Connection connection;

        ObjectReturner(BlockingQueue<Connection> queue, Connection connection) {
            this.queue = queue;
            this.connection = connection;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    queue.put(connection);
                    break;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

            }
        }
    }
}
