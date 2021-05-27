package ws.prospeak.myweb.framework.Illuminate.database.pool;

import java.sql.Connection;

public interface Pool {
    //Init pool
    void init(int nPool);

    // Asking for connection object if pool is empty wait until it will has a connection
    Connection getConnection();

    // Return pbject to the pool
    void release(Connection connection);

    // Is connections returnable to pool
    void isReturnedTo(Boolean v);

    // Close all connections on the pool
    void shutdown();

}
