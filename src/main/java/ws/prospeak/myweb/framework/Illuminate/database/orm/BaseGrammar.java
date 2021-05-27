package ws.prospeak.myweb.framework.Illuminate.database.orm;

import org.apache.commons.lang.StringUtils;
import ws.prospeak.myweb.framework.Illuminate.database.orm.query.Expression;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class BaseGrammar {
    protected String tablePrefix = "";

    public List wrapArray(List values) {
        return (List) values.stream().map(value -> this.wrap(value, false)).collect(Collectors.toList());
    }

    public String wrapTable(Object value) {
        if (!this.isExpression(value)) {
            return this.wrap(this.tablePrefix + String.valueOf(value), true);
        }
        return (String) this.getValue((Expression) value);
    }

    public String wrap(Object value, boolean prefixAlias) {
        if (this.isExpression(value)) {
            return ((Expression) value).getValue().toString();
        }
        String val = String.valueOf(value);
        if (val.startsWith(" as ")) {
            return this.wrapAliasedValue(val, prefixAlias);
        }
        if (this.isJsonSelector(val)) {
            return (String) this.wrapJsonSelector(val);
        }
        return null;
    }

    public String wrapAliasedValue(String value, boolean prefixAlias) {
        Pattern p = Pattern.compile("/\\s+as\\s+/i");
        Matcher m = p.matcher(value);
        List<String> matches = new ArrayList<>();
        while (m.find())
            matches.add(m.group());
        if (prefixAlias) {
            matches.add(1, matches.get(1) + tablePrefix);
        }
        return this.wrap(matches.get(0), false) + " as " + this.wrapValue(matches.get(1));
    }

    public String wrapSegments(List<String> segments) {
        List<String> str = new ArrayList<>();
        for (int i = 0; i < segments.size(); i++) {
            if (i == 0 && segments.size() > 1) {
                str.add(this.wrapTable(segments.get(i)));
            } else {
                str.add(this.wrapValue(segments.get(i)));
            }
        }
        return StringUtils.join(str, ".");
    }

    public String wrapValue(String value) {
        if (value.equals("*")) {
            return StringUtils.replace(value, "\"", "\"\"");
        }
        return value;
    }

    protected Object wrapJsonSelector(String value) {
        throw new RuntimeException("This database engine does not support JSON operations");
    }

    protected boolean isJsonSelector(String value) {
        return value.contains("->");
    }

    public String columnize(List<Object> columns) {
        return null;
    }

    public String parameterize(List<Object> values) {
        List<Object> parameterizeList = values.stream().map(this::parameter).collect(Collectors.toList());
        return StringUtils.join(parameterizeList, ", ");
    }

    public Object parameter(Object value) {
        return this.isExpression(value) ? this.getValue((Expression) value) : "?";
    }

    public String quoteString(Object value) {
        if (value instanceof List) {
            return StringUtils.join((List<Object>) value, ", ");
        } else if (value instanceof Object[]) {
            return StringUtils.join((Object[]) value, ", ");
        }
        return String.valueOf(value);
    }

    public boolean isExpression(Object value) {
        return value instanceof Expression;
    }

    public Object getValue(Expression expression) {
        return expression.getValue();
    }

    public String getDateFormat() {
        return "Y-m-d H:i:s";
    }

    public String setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
        return this.tablePrefix;
    }

    public String getTablePrefix() {
        return this.tablePrefix;
    }
}
