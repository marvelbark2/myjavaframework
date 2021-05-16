package ws.prospeak.myweb.framework.config;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;

public class ViewConfig {
    public Configuration config() throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
        System.out.println(this.getClass().getResource("/views/test.egde"));
        System.out.println(this.getClass().getResource("/views/").getPath());
        cfg.setDirectoryForTemplateLoading(new File(this.getClass().getResource("/views/").getPath()));
        cfg.setDefaultEncoding("UTF-8");

        return cfg;
    }
}
