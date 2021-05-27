package ws.prospeak.myweb.framework;

import org.apache.commons.lang.StringUtils;
import ws.prospeak.myweb.framework.Illuminate.collection.Collection;
import ws.prospeak.myweb.framework.app.models.Users;

import java.util.logging.Logger;


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
//        user.setId(9L);
//        user.setName("Leonardo 12");
//        user.setUsername("Leonardo");
//        user.setPassword("Leonardo1201");
//        Users userSave = user.save();
        Collection<Users> allUser = user.all();
        Collection<Users> whereTest = allUser.where("username", "Malik");

        Users findUser = whereTest.find(3L);
        logger.info(whereTest.toString());
        logger.info(findUser.toString());
        logger.info(user.all().toString());
        logger.info(allUser.intersaction(whereTest).toString());
        logger.info(allUser.diff(whereTest).toString());
    }
}
