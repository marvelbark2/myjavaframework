package ws.prospeak.myweb.framework.Illuminate.database.orm.query;

public class Expression {
    protected  Object value;

    public Expression(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
