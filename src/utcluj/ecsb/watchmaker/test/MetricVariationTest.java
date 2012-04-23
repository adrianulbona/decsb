package utcluj.ecsb.watchmaker.test;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utcluj.ecsb.ECSB;
import utcluj.ecsb.watchmaker.Utils;
import utcluj.ecsb.watchmaker.metrics.Metric;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

@RunWith(value = Parameterized.class)
public class MetricVariationTest {

    final private static String CONFIGURATION_FILE = "decsb.properties";

    private Properties configuration;

    public MetricVariationTest(Properties configuration) {
        this.configuration = configuration;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Utils.initLogger();

        Collection<Object[]> data = new ArrayList<Object[]> ();
        try {
            final Properties tempConfiguration = Utils.loadConfiguration(CONFIGURATION_FILE);

            for(Metric metric : Metric.values()){
                tempConfiguration.setProperty("fitness_metric", metric.getMetricName());
                data.add(new Object[]{tempConfiguration.clone()});
            }
        } catch (IOException e) {
            Logger.getLogger("ECSBLog").error("Unable to load configuration file.");
        }

        return data;
    }

    @Test
    public void metricVariationTest() {
        ECSB engine = new ECSB();
        engine.runEvolutionaryCostSensitiveBalancing(configuration);
    }
}
