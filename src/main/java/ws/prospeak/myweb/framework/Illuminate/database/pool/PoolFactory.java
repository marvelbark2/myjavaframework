package ws.prospeak.myweb.framework.Illuminate.database.pool;

public enum PoolFactory {
    Instance;

    public BlockingPool pool;
    boolean isNotReturnable;
    PoolFactory() {
        pool = new ConnectionPoolManager();
    }

    BlockingPool newPool(int n) {
        pool.init(n);
        return pool;
    }
    public boolean isNotReturnable() {
        return isNotReturnable;
    }

    public void setNotReturnable(boolean notReturnable) {
        isNotReturnable = notReturnable;
    }

}
