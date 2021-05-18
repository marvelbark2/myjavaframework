package ws.prospeak.myweb.framework.Illuminate.orm;

import org.flywaydb.core.internal.database.postgresql.PostgreSQLDatabase;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.DB;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
import org.postgresql.ds.PGSimpleDataSource;
import ws.prospeak.myweb.framework.config.DatabaseConfig;

import javax.sql.DataSource;
import javax.sql.rowset.BaseRowSet;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public abstract class Models extends Model {
    public Models() {
    }



    public void saveNow() {
        System.out.println("Saving it ");
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                String name = field.getName();
                Object o = field.get(this);
                System.out.println(name + " : " +o);
                this.set(name, o);
                field.setAccessible(false);
            }
            System.out.println(this);
            Boolean b = this.insert();
            System.out.println("Is saved " + b);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Base.close();
        }
    }

    public LazyList<? extends Model> all() {
        /*loadMachine().withDb(() -> {
            LazyList collection = null;
            try {
                System.out.println(Base.findAll("select * from users").size());
                Method findAll = this.getClass().getMethod("findAll");
                collection = (LazyList) findAll.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return collection;
        });*/
        return null;
    }
}
