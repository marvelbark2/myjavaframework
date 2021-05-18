package ws.prospeak.myweb.framework;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import ws.prospeak.myweb.framework.app.models.Users;
import ws.prospeak.myweb.framework.config.DatabaseConfig;

import java.util.List;
import java.util.Map;

public class TestClass {
    public static void main(String[] args) {
        Users users = new Users();
       /*

        users.setId(7L);
        users.setName("Younes");
        users.setUsername("Prospeak");
        users.setPassword("aqwzsxedc");

        users.saveNow();*/
//        users.list();
        List<Users> list = Users.findAll();

        System.out.println(users.all());

        Base.close();
    }
}
