import net.bndy.config.ConfigurationManager;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.YAMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class TestConfigurationManager {

    @Test public void test() {
        ConfigurationManager.saveConfiguration("a.yml", "updatedAt", new Date());
        ConfigurationManager.saveConfiguration("a.yml", "p.p1", "p1");
        Assert.assertEquals(ConfigurationManager.getConfiguration("a.yml", "p.p1", String.class), "p1");

        ConfigurationManager.saveConfiguration("a.properties", "updatedAt", new Date());
        ConfigurationManager.saveConfiguration("a.properties", "p.p1", 1);
        Assert.assertEquals(ConfigurationManager.getConfiguration("a.properties", "p.p1", Integer.class).intValue(), 1);

        ConfigurationManager.removeConfiguration("a.properties", "p.p1");
        Assert.assertEquals(ConfigurationManager.getConfiguration("a.properties", "p.p1", String.class), null);

    }


    @Test public void testPropertiesFile() {
        Configurations configurations = new Configurations();
        FileBasedConfigurationBuilder<PropertiesConfiguration> configurationBuilder= configurations.propertiesBuilder("a.properties");
        PropertiesConfiguration configuration = null;
        try {
            configuration = configurationBuilder.getConfiguration();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        configuration.addProperty("a.b", "Bing Zhang");
        try {
            configurationBuilder.save();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test public void testYamlFile() {
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<YAMLConfiguration> configurationBuilder = new FileBasedConfigurationBuilder<>(YAMLConfiguration.class)
            .configure(params.hierarchical().setFileName("a.yml"));
        YAMLConfiguration configuration = null;
        try {
            configuration = configurationBuilder.getConfiguration();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        configuration.addProperty("a.b", "Bing Zhang");
        try {
            configurationBuilder.save();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }
}
