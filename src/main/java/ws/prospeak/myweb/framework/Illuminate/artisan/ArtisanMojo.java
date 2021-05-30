package ws.prospeak.myweb.framework.Illuminate.artisan;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name="XmlToJson", defaultPhase = LifecyclePhase.COMPILE)
public class ArtisanMojo extends AbstractMojo {
    @Parameter
    private String command;

    @Parameter
    private String opt;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println(command + "  : " + opt);
    }
}
