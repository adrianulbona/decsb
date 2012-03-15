package utcluj.ecsb.preprocessing;

import org.apache.log4j.Logger;
import utcluj.ecsb.watchmaker.metrics.FitnessMetric;
import utcluj.ecsb.watchmaker.metrics.FitnessMetricWithBeta;
import weka.classifiers.Classifier;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.core.AttributeStats;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

/**
 * User: adibo
 * Date: 12.12.2011
 * Time: 16:29
 */
public class ConfigurationHandler {

    public static final String PROPERTIES_FILE = "decsb.properties";

    private Instances instances;
    private int minorityClassIndex;
    private Classifier costClassifier;
    private String baseClassifierName;
    private FitnessMetric fitnessMetric;
    private int numFolds;

    public ConfigurationHandler() {
        Properties properties = loadConfiguration(PROPERTIES_FILE);
        Logger.getLogger("ECSBLog").error(properties);
        initHandler(properties);
    }

    // TODO : commentaries
    private void initHandler(Properties props){

        instances = loadInstances(props.getProperty("dataset_path"));

        numFolds = Integer.valueOf(props.getProperty("num_folds"));

        int classIndex = instances.numAttributes() - 1;
        instances.setClassIndex(classIndex);
        instances.randomize(new Random(1));
        instances.stratify(numFolds);
        AttributeStats classStats = instances.attributeStats(classIndex);
        if (classStats.nominalCounts[0] > classStats.nominalCounts[1]) {
            minorityClassIndex = 1;
        } else {
            minorityClassIndex = 0;
        }
        baseClassifierName = props.getProperty("base_classifier");

        try {
            Class<?> costClassifierClass = Class.forName(props.getProperty("cost_classifier"));
            costClassifier = (Classifier) costClassifierClass.newInstance();
            if (costClassifier instanceof CostSensitiveClassifier) {
                ((CostSensitiveClassifier) costClassifier).setMinimizeExpectedCost(Boolean.parseBoolean(props.getProperty("use_reweight")));
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            Logger.getLogger("DECSBLog").error("Unable to create the cost classifier.");
        }
        try {
            Class<?> fitnessMetricClass = Class.forName(props.getProperty("fitness_metric"));
            fitnessMetric = (FitnessMetric) fitnessMetricClass.newInstance();
            if (fitnessMetric instanceof FitnessMetricWithBeta) {
                ((FitnessMetricWithBeta) fitnessMetric).setBeta(Double.parseDouble(props.getProperty("beta")));
            }

        }catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            Logger.getLogger("DECSBLog").error("Unable to create the fitness metric.");
        }
    }

    private Properties loadConfiguration(String propertiesFile) {
        Properties props = new Properties();
        try(FileInputStream in = new FileInputStream(propertiesFile)){
            props.load(in);
        } catch (IOException e ) {
            Logger.getLogger("DECSBLog").error("Unable to load props file.");
        }
        return props;
    }

    private Instances loadInstances(String datasetPath) {
        Instances instances = null;
        try(BufferedReader instancesReader = new BufferedReader(new FileReader(datasetPath))) {
            instances = new Instances(instancesReader);
        } catch (IOException e) {
            Logger.getLogger("DECSBLog").error("Unable to load dataset.");
        }
        return instances;
    }

    public Instances getInstances() {
        return instances;
    }

    public int getMinorityClassIndex() {
        return minorityClassIndex;
    }

    public Classifier getCostClassifier() {
        return costClassifier;
    }

    public String getBaseClassifierName() {
        return baseClassifierName;
    }

    public FitnessMetric getFitnessMetric() {
        return fitnessMetric;
    }
    public int getNumFolds(){
        return numFolds;
    }
}
