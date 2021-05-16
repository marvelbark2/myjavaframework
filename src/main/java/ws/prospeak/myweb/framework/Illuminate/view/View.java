package ws.prospeak.myweb.framework.Illuminate.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import ws.prospeak.myweb.framework.ApplicationBoot;
import ws.prospeak.myweb.framework.config.ViewConfig;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class View {
    public static String render(String templateName, Map<String, Object> input) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
        cfg.setClassForTemplateLoading(View.class, "/views/");

        StringWriter stringWriter = new StringWriter();
        try {
            Template template = cfg.getTemplate(templateName);
            template.process(input, stringWriter);
        } catch (IOException ioException) {
            return "Error! Template doesn't exist ! \n" +
                    "Tips: Check in resources/view/ if file exist \n" +
                    "Tips: Check if the file has Egde extension";
        } catch (TemplateException exception) {
            return "Error! Data wrong formatted !";
        }

        return stringWriter.toString();
    }
    public static String render(String templateName) {
        Configuration cfg = null;
        try {
            cfg = new ViewConfig().config();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        StringWriter stringWriter = new StringWriter();
        try {
            Template template = cfg.getTemplate(templateName);
            template.process(Map.of(),stringWriter);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return "Error! Template doesn't exist ! \n" +
                    "Tips: Check in resources/view/ if file exist \n" +
                    "Tips: Check if the file has Egde extension";
        } catch (TemplateException exception) {
            exception.printStackTrace();
            return "Error! Data (Map) formatted badly \n";
        }

        return stringWriter.toString();
    }

    public static String json(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error! Bad json Serialization";
        }
    }

    //TODO: Adding Json response
}
