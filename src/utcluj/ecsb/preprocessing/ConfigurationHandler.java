package utcluj.ecsb.preprocessing;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import utcluj.ecsb.watchmaker.metrics.FitnessMetric;
import utcluj.ecsb.watchmaker.metrics.FitnessMetricWithBeta;
import weka.classifiers.Classifier;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.core.AttributeStats;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: adibo
 * Date: 12.12.2011
 * Time: 16:29
 * To change this template use File | Settings | File Templates.
 */
public class ConfigurationHandler {

    private Configuration configuration;
    private Instances instances;
    private int minorityClassIndex;
    private Classifier costClassifier;
    private String baseClassifierName;
    private FitnessMetric fitnessMetric;

    public ConfigurationHandler(String configurationFile) {
        configuration = loadConfiguration(configurationFile);
        instances = loadInstances();

        int classIndex = instances.numAttributes() - 1;
        instances.setClassIndex(classIndex);
        instances.randomize(new Random(1));
        instances.stratify(configuration.getNumFolds());
        AttributeStats classStats = instances.attributeStats(classIndex);
        if (classStats.nominalCounts[0] > classStats.nominalCounts[1]) {
            minorityClassIndex = 1;
        } else {
            minorityClassIndex = 0;
        }
        baseClassifierName = configuration.getBaseClassifiersNames()[configuration.getBaseClassifierUsed()];

        try {
            Class<?> costClassfifierClass = Class.forName(configuration.getCostClassifiersNames()[configuration.getCostClassifierUsed()]);
            costClassifier = (Classifier) costClassfifierClass.newInstance();
            if (costClassifier instanceof CostSensitiveClassifier) {
                ((CostSensitiveClassifier) costClassifier).setMinimizeExpectedCost(configuration.isUseReweight());
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            Class<?> fitnessMetricClass = Class.forName(configuration.getFitnessMetricsNames()[configuration.getFitnessMetricUsed()]);
            fitnessMetric = (FitnessMetric) fitnessMetricClass.newInstance();
            if (fitnessMetric instanceof FitnessMetricWithBeta) {
                ((FitnessMetricWithBeta) fitnessMetric).setBeta(configuration.getBeta());
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static Configuration loadConfiguration(String string) {
        FileReader fileReader;
        try {
            fileReader = new FileReader(string);
            return (Configuration) Unmarshaller.unmarshal(Configuration.class, fileReader);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Problems with loading the configuration file.");
            return null;
        }
    }

    public static void saveConfiguration(Configuration conf, String fichier_xml) {
        try {
            FileWriter file = new FileWriter(fichier_xml);
            Marshaller.marshal(conf, file);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Problems with saving the configuration file.");
        }
    }

    private Instances loadInstances() {
        try {
            BufferedReader instances = new BufferedReader(new FileReader(configuration.getDatasetPath()));
            return new Instances(instances);

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Instances getInstances() {
        return instances;
    }

    public void setInstances(Instances instances) {
        this.instances = instances;
    }

    public int getMinorityClassIndex() {
        return minorityClassIndex;
    }

    public void setMinorityClassIndex(int minorityClassIndex) {
        this.minorityClassIndex = minorityClassIndex;
    }

    public Classifier getCostClassifier() {
        return costClassifier;
    }

    public void setCostClassifier(Classifier costClassifier) {
        this.costClassifier = costClassifier;
    }

    public String getBaseClassifierName() {
        return baseClassifierName;
    }

    public void setBaseClassifierName(String baseClassifierName) {
        this.baseClassifierName = baseClassifierName;
    }

    public FitnessMetric getFitnessMetric() {
        return fitnessMetric;
    }

    public void setFitnessMetric(FitnessMetric fitnessMetric) {
        this.fitnessMetric = fitnessMetric;
    }
}
