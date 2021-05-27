package ws.prospeak.myweb.framework.Illuminate.database.orm.query.grammars;

import org.apache.commons.lang.StringUtils;
import ws.prospeak.myweb.framework.Illuminate.database.orm.*;

import java.util.List;
import java.util.stream.Collectors;

public class Grammar extends BaseGrammar {
        List<String> operators;
        List<String> selectComponents;

    public Grammar() {
            operators = List.of();
            selectComponents = List.of(
                    "aggregate",
                    "columns",
                    "from",
                    "joins",
                    "wheres",
                    "groups",
                    "havings",
                    "orders",
                    "limit",
                    "offset",
                    "lock"
                    );
    }



    protected String concatenate(List<String> segments) {
        return StringUtils.join(segments.stream().filter(segment-> !segment.equals("")).collect(Collectors.toList()), " ");
    }

    protected String removeLeadingBoolean(String value) {
        return value.replaceAll("/and |or /i", "");
    }
    public List<String> getOperators() {
        return operators;
    }
}
