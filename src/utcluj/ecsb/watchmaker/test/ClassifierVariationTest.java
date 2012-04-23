package utcluj.ecsb.watchmaker.test;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utcluj.ecsb.ECSB;
import utcluj.ecsb.watchmaker.Utils;
import utcluj.ecsb.watchmaker.preprocessing.Classifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

@RunWith(value = Parameterized.class)
public class ClassifierVariationTest {

    final private static String CONFIGURATION_FILE = "decsb.properties";

    private Properties configuration;

    public ClassifierVariationTest(Properties configuration) {
        this.configuration = configuration;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Utils.initLogger();

        Collection<Object[]> data = new ArrayList<Object[]> ();
        try {
            final Properties tempConfiguration = Utils.loadConfiguration(CONFIGURATION_FILE);

            for (Classifier classifier : Classifier.values()){
                tempConfiguration.setProperty("base_classifier", classifier.getClassName());
                data.add(new Object[]{tempConfiguration.clone()});
            }
        } catch (IOException e) {
            Logger.getLogger("ECSBLog").error("Unable to load configuration file.");
        }

        return data;
    }

    @Test
    public void classifierVariationTest() {
        ECSB engine = new ECSB();
        engine.runEvolutionaryCostSensitiveBalancing(configuration);
    }
}
