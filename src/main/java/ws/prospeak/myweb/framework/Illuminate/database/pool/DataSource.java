package ws.prospeak.myweb.framework.Illuminate.database.pool;

import java.sql.Connection;
import java.util.concurrent.TimeUnit;

public class DataSource {
    private BlockingPool pool;
    private long time;

    public DataSource(int n, long time) {
        pool = PoolFactory.Instance.newPool(n);
        this.time = time;
    }
    public DataSource(int n) {
        pool = PoolFactory.Instance.newPool(n);
        this.time = 1_000;
    }

    public Connection getConnection() {
        return pool.getConnection(time, TimeUnit.MILLISECONDS);
    }

    public void shutdownPool() {
        pool.shutdown();
    }

    public void release(Connection connection) {
        pool.release(connection);
    }

    public int poolSize() {
        return pool.poolSize();
    }

    public void returnable(Boolean v) { pool.isReturnedTo(v); }
}
