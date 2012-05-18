package ro.utcluj.ecsb;

import org.apache.log4j.Logger;
import ro.utcluj.ecsb.utils.EcsbClassifiers;
import ro.utcluj.ecsb.utils.EcsbFactory;
import ro.utcluj.ecsb.utils.EcsbUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Properties;

public class EcsbTests {
    public static void main(String[] args){
        try{
            for(EcsbClassifiers classifier : EcsbClassifiers.values()){
                Logger.getLogger(EcsbTests.class).info(classifier.getClassName() + " - starting time:" + new Timestamp(System.currentTimeMillis()));
                Properties configuration = EcsbUtils.loadConfiguration(false, ECSB.CONF_PATH + "decsb.properties");
                EcsbUtils.initLogger(false, "nd_"+classifier.getClassName()+".txt");
                configuration.setProperty("base_classifier", classifier.getClassName());
                final ECSB ecsb = new EcsbFactory(configuration).setUpECSB(false);
                ecsb.runEvolutionaryCostSensitiveBalancing();
                Logger.getLogger(EcsbTests.class).info(classifier.getClassName() + " - end time:" + new Timestamp(System.currentTimeMillis()));
            }
        } catch (IOException e) {
            Logger.getLogger("ECSBLog").error("Unable to load configuration file.");
    }

    }
}
