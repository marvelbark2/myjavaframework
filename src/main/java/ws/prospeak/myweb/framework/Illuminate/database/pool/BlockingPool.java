package ws.prospeak.myweb.framework.Illuminate.database.pool;

import java.sql.Connection;
import java.util.concurrent.TimeUnit;

public interface BlockingPool extends Pool {
    Connection getConnection(long time, TimeUnit unit);
    int poolSize();
}
