package ws.prospeak.myweb.framework;

import org.apache.commons.lang.StringUtils;
import ws.prospeak.myweb.framework.Illuminate.collection.Collection;
import ws.prospeak.myweb.framework.app.models.Users;


public class TestClass {
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

        Users findUser = whereTest.find(1L);
        System.out.println(findUser);
        System.out.println(user.all());
    }
}
