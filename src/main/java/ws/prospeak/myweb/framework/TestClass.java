package ws.prospeak.myweb.framework;

import org.apache.commons.lang.StringUtils;
import ws.prospeak.myweb.framework.Illuminate.collection.CollectionEntity;
import ws.prospeak.myweb.framework.Illuminate.database.orm.Models;
import ws.prospeak.myweb.framework.app.models.Users;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class TestClass {
    final static Logger logger = Logger.getLogger(TestClass.class.getName());
    public static void main(String[] args) throws Exception {
        String html = "@foreach($edt as $ed)" +
                "                    {" +
                "                      id: {{$ed->id}},\n" +
                "                      title: \"{{$ed->title}}\",\n" +
                "                      start: \"{{$ed->start_at}}\",\n" +
                "                      end: \"{{$ed->end_at}}\"\n" +
                "                    }," +
                "                    @endforeach";
        System.out.println(StringUtils.substringBetween(html, "@foreach", "@endforeach"));

        Users user = new Users();
        Field[] fc = user.getClass().getFields();
        for (Field f: fc) {
            System.out.println(f.getName());
        }
        System.out.println(System.lineSeparator());
        Field[] fa = user.getClass().getSuperclass().getFields();
        for (Field f: fa) {
            System.out.println(f.getName());
        }

//        user.setId(13L);
//        user.setName("SAED 12");
//        user.setUsername("SAED");
//        user.setPassword("SAED22344");
      //  Users userSave = user.save();
       // logger.info(userSave.toString());
//        Users userById = user.find(12L);
//        System.out.println("be false : " + userById.checkEncrypted("123433") + " be True: " + userById.checkEncrypted("SAED22344"));
//        CollectionEntity<Users> allUser = user.all();
//        CollectionEntity<Users> sortedAllUser = allUser.sortBy("username");
//        CollectionEntity<Users> whereTest = allUser.where("username", "Malik");
//
//        Users findUser = whereTest.find(3L);
//        logger.info("Filtered user "+ whereTest);
//        logger.info("User found by id : " +findUser.toString());
//        logger.info("All Users : " + user.all().toString());
//        logger.info("All Users sorted by id: " + sortedAllUser.getCollection());
//        logger.info("Intersection test: " + allUser.intersaction(whereTest).toString());
//        logger.info("Difference test: " + allUser.diff(whereTest).toString());
//        Users u2 = new Users(2L);
//        logger.info("user with find in constructor  "+u2);
       // findUser.toRmi();
        // findUser.toSocket();
    }
}
