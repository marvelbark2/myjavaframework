package ws.prospeak.myweb.framework.Illuminate.database.orm;

import org.hibernate.Session;

@FunctionalInterface
public interface Command {
    Object execute(Session session);
}
