package ws.prospeak.myweb.framework.Illuminate.database.orm;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mindrot.jbcrypt.BCrypt;
import ws.prospeak.myweb.framework.Illuminate.collection.CollectionEntity;
import ws.prospeak.myweb.framework.Illuminate.database.orm.hibernate.HibernateUtil;
import ws.prospeak.myweb.framework.Illuminate.network.rmi.RmiModel;
import ws.prospeak.myweb.framework.Illuminate.network.Services;
import ws.prospeak.myweb.framework.Illuminate.network.sockets.SocketService;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.*;

@MappedSuperclass
public abstract class Models implements Serializable {
    private static final long serialVersionUID = 8014042129743182574L;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    protected Calendar createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    protected Calendar updatedAt;

    @Transient
    protected String table;

    @Transient
    @JsonIgnore
    protected String primaryKey = "id";

    @Transient
    @JsonIgnore
    protected String encrypted_field;

    @Transient
    @JsonIgnore
    protected String created_at = "created_at";

    @Transient
    @JsonIgnore
    protected String updated_at = "updated_at";

    @Transient
    @JsonIgnore
    protected Services services = Services.INSTANCE;

    @Transient
    protected List<String> guarded = new ArrayList<>();
    @Transient
    protected List<String>  fillable = new ArrayList<>();
    @Transient
    protected ObjectMapper mapper = new ObjectMapper();

    public Models() {
        mapper.registerModule(new JavaTimeModule());
        try {
            System.out.println("test");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Models(Object id) throws Exception {
        this.find(id);
    }

    public void toRmi() {
        Registry registry = services.rmi();
        try {
            RmiModel stub = new RmiModel(this);
            registry.rebind(this.getClass().getSimpleName().toLowerCase(Locale.ROOT), stub);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Calendar createdAt) {
        this.createdAt = createdAt;
    }

    public Calendar getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Calendar updatedAt) {
        this.updatedAt = updatedAt;
    }

    private void loadTableName() {
        getFieldFromModel("table");
    }

    private Field getFieldFromModel(String key) {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field: fields) {
            if(table == null) {
                if(field.getName().equals(key)) {
                    return field;
                }
            }
        }
        return null;
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

    public String json() throws Exception {
        return mapper.writeValueAsString(this);
    }
    public <T extends Models> Map<Object, T> map() {
        Map<Object, T> hm = new HashMap<>();
        return mapper.convertValue(this,  hm.getClass());
    }

    public <T extends Models> T save() throws Exception {
        if(encrypted_field != null){
            Field encryptedField = getFieldFromModel(encrypted_field);
            Boolean accessible = encryptedField.isAccessible();
            encryptedField.setAccessible( accessible? true: true);
            String value = String.valueOf(encryptedField.get(this));
            String encryptedValue = BCrypt.hashpw(value, BCrypt.gensalt());
            encryptedField.set(this, encryptedValue);
            encryptedField.setAccessible( accessible);
        }
        hibernateContext(session -> session.save(this));
        return (T) this;
    }
    public Boolean checkEncrypted(String checkpw) throws Exception {
        if(encrypted_field != null) {
            Field encryptedField = getFieldFromModel(encrypted_field);
            Boolean accessible = encryptedField.isAccessible();
            encryptedField.setAccessible( accessible? true: true);
            String value = String.valueOf(encryptedField.get(this));
            return BCrypt.checkpw(checkpw, value);
        } else
            throw new IllegalAccessException("You need to specifiy the encryped field name by setting encrypted_field value(Striing value)");
    }



    public<T extends Models> CollectionEntity<T> all() throws Exception {

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
        return new CollectionEntity<>(allList);
    }

    public void toSocket() throws Exception {
        SocketService socket = services.socket();
        socket.enableTcp(this);
    }

    public <T extends Models>  T find(Object id) throws Exception {
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

    public void toTcp() {

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
        return this.json();
    }
}
