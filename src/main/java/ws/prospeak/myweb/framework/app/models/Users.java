package ws.prospeak.myweb.framework.app.models;

import ws.prospeak.myweb.framework.Illuminate.database.orm.Models;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Users extends Models implements Serializable {

    private static final long serialVersionUID = 7600159191940189307L;

    @Id
    private Long id;
    private String name;
    private String username;
    private String password;

    public Users() {
        super();
        this.encrypted_field = "password";
    }

    public Users(Object id) throws Exception {
        super(id);
        this.encrypted_field = "password";
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }
}
