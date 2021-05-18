package ws.prospeak.myweb.framework.config;

import java.util.HashMap;
import java.util.Map;

public enum DatabaseConfig {
    CONFIG;
    private Map<String, Map<String, String>> dbConfig;

    DatabaseConfig() {
        loadMachine();
    }

    public void loadMachine() {
        dbConfig = new HashMap<>();
        Map<String, String> pg = Map.of(
                "driver", "org.postgresql.Driver",
                "name", "postgresql",
                "db", "testframe",
                "host", "localhost",
                "port", "5432",
                "username", "postgres",
                "password", "Aqwzsxedc-123"
        );
        Map<String, String> mysql = Map.of(
                "driver", "com.mysql.cj.jdbc.Driver",
                "name", "mysql",
                "db", "testframe?nullNamePatternMatchesAll=true",
                "host", "localhost",
                "port", "3306",
                "username", "root",
                "password", "Aqwzsxedc-123"
        );
        dbConfig.put("pg", pg);
        dbConfig.put("mysql", mysql);
    }

    public Map<String, Map<String, String>> getDbConfig() {
        return dbConfig;
    }
}
