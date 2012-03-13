package utcluj.ecsb.preprocessing;

import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: adibo
 * Date: 12.12.2011
 * Time: 17:21
 * To change this template use File | Settings | File Templates.
 */
public class ConfigurationHandlerTest {
    @Test
    public void testGetCostClassifier() throws Exception {
        Configuration configuration = ConfigurationHandler.loadConfiguration("conf.xml");

        configuration.setFitnessMetricsNames(new String[]{
                "utcluj.ecsb.watchmaker.metrics.BaccMetric",
                "utcluj.ecsb.watchmaker.metrics.FMeasureMetric",
                "utcluj.ecsb.watchmaker.metrics.GMMetric",
                "utcluj.ecsb.watchmaker.metrics.LinTPFNMetric",
                "utcluj.ecsb.watchmaker.metrics.LinTPPrecisionMetric",
                "utcluj.ecsb.watchmaker.metrics.LinTPTNMetric"});
        configuration.setFitnessMetricUsed(5);
        configuration.setBeta(1.0);
        ConfigurationHandler.saveConfiguration(configuration,"conf1.xml");

    }
}
