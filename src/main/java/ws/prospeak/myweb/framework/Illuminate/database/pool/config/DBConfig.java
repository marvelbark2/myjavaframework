package ws.prospeak.myweb.framework.Illuminate.database.pool.config;

public enum DBConfig {
    Instance;
    public String HOST;
    public String USER;
    public String PASS;
    public String DRIVER;

    DBConfig() {
        HOST = "jdbc:postgresql://172.31.249.90:5432/myframework";
        USER = "postgres";
        PASS = "Aqwzsxedc-123";
        DRIVER = "org.postgresql.Driver";
    }
}
