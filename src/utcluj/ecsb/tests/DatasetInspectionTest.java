package utcluj.ecsb.tests;

import org.junit.Test;
import utcluj.ecsb.preprocessing.ConfigurationHandler;

/**
 * Created by IntelliJ IDEA.
 * User: adibo
 * Date: 11.12.2011
 * Time: 20:07
 * To change this template use File | Settings | File Templates.
 */
public class DatasetInspectionTest {

    @Test
    public void testGetFitness() throws Exception {

        ConfigurationHandler configurationHandler = new ConfigurationHandler("conf.xml");

        System.out.println(configurationHandler.getInstances().toSummaryString());



    }
}
