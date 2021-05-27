package ws.prospeak.myweb.framework.Illuminate.database.orm;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ws.prospeak.myweb.framework.Illuminate.collection.Collection;
import ws.prospeak.myweb.framework.Illuminate.database.orm.hibernate.HibernateUtil;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

@MappedSuperclass
public abstract class Models implements Serializable {
    @Transient
    protected String table;

    @Transient
    @JsonIgnore
    protected String primaryKey;

    @Transient
    protected List<String> guarded = new ArrayList<>();
    @Transient
    protected List<String>  fillable = new ArrayList<>();
    @Transient
    protected ObjectMapper mapper = new ObjectMapper();



    public Models() throws IllegalAccessException { }

    private void loadTableName() {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field: fields) {
            if(table == null) {
                if(field.getName().equals("table")) {

                }
            }
        }
    }

    private void setTableName(String name) {
        if(this.getClass().isAnnotationPresent(Table.class)) {
            Table annotation = this.getClass().getAnnotation(Table.class);
        }
    }


    private void loadId(Object obj) throws IllegalAccessException{
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field: fields) {
            if(field.getName().equals("username")){
                field.setAccessible(true);
                System.out.println(" User name : " + field.get(obj));
            }
        }
    }

    public String json() throws JsonProcessingException {
        return mapper.writeValueAsString(this);
    }
    public <T extends Models> Map<Object, T> map() {
        Map<Object, T> hm = new HashMap<>();
        return mapper.convertValue(this,  hm.getClass());
    }

    public <T extends Models> T save() throws Exception {
        hibernateContext(session -> session.save(this));
        return (T) this;
    }


    public<T extends Models> Collection<T> all() throws Exception {

        List<T> allList = new ArrayList<>();
        hibernateContext(session -> {
            Class<T> clzz = (Class<T>) this.getClass();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(clzz);
            Root<T> rootEntry = cq.from(clzz);
            CriteriaQuery<T> all = cq.select(rootEntry);
            allList.addAll(session.createQuery(all).getResultList());
            return null;
        });
        return new Collection<>(allList);
    }

    public <T extends  Models>  T find(Object id) throws Exception {
        Map<String, T>  data = new HashMap<>();
        Long idSerialized = Long.parseLong(String.valueOf(id));
        hibernateContext(
                session -> data.put(
                        "obj",
                        (T) session.get(this.getClass(), idSerialized)
            )
        );
        return data.get("obj");
    }

    private void hibernateContext(Command command) throws Exception {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory("database.properties");
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        command.execute(session);
        session.flush();
        session.close();
        sessionFactory.close();
    }

    public void setTable(String table) {
        this.table = table;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setGuarded(String guarded) {
        this.guarded.add(guarded);
    }

    public void setFillable(String fillable) {
        this.fillable.add(fillable);
    }
    public String getPrimaryKey() {
        return primaryKey;
    }

    @SneakyThrows
    @Override
    public String toString() {
        return mapper.writeValueAsString(this);
    }
}
