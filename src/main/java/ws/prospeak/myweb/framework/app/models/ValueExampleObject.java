package ws.prospeak.myweb.framework.app.models;

public class ValueExampleObject {
    private String name;
    private String developer;

    public ValueExampleObject(String name, String developer) {
        this.name = name;
        this.developer = developer;
    }

    public String getName() {
        return name;
    }

    public String getDeveloper() {
        return developer;
    }
}
