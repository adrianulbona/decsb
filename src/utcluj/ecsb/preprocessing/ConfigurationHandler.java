package utcluj.ecsb.preprocessing;

import org.apache.log4j.Logger;
import utcluj.ecsb.watchmaker.metrics.FitnessMetric;
import utcluj.ecsb.watchmaker.metrics.Metrics;
import weka.classifiers.Classifier;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.core.AttributeStats;
import weka.core.Instances;

import java.io.BufferedReader;
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


    private Instances instances;
    private int minorityClassIndex;
    private Classifier costClassifier;
    private String baseClassifierName;
    private FitnessMetric fitnessMetric;
    private int numFolds;

    public ConfigurationHandler(Properties configuration) {
        initHandler(configuration);
    }

    // TODO : commentaries
    private void initHandler(Properties props) {
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
        fitnessMetric = createFitnessMetric(props.getProperty("fitness_metric"), props.getProperty("beta"));

    }


    private Instances loadInstances(String datasetPath) {
        Instances instances = null;
        try (BufferedReader instancesReader = new BufferedReader(new FileReader(datasetPath))) {
            instances = new Instances(instancesReader);
        } catch (IOException e) {
            Logger.getLogger("DECSBLog").error("Unable to load dataset.");
        }
        return instances;
    }

    public FitnessMetric createFitnessMetric(String fitnessMetricName, String beta) {
        switch (fitnessMetricName) {
            case "GMMetric":
                return Metrics.getAGMMetric();
            case "BaccMetric":
                return Metrics.getAGBaccMetric();
            case "FMeasureMetric":
                return Metrics.getAFMeasureMetric(Double.valueOf(beta));
            case "LinTPFNMetric":
                return Metrics.getALinTPFNMetric(Double.valueOf(beta));
            case "LinTPPrecisionMetric":
                return Metrics.getALinTPPrecisionMetric(Double.valueOf(beta));
            case "LinTPTNMetric":
                return Metrics.getALinTPTNMetric(Double.valueOf(beta));
            default:
                Logger.getLogger("DECSBLog").error("Unable to create the fitness metric.");
                return null;
        }


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

    public int getNumFolds() {
        return numFolds;
    }
}
