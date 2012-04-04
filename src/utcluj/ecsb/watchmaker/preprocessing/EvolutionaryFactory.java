package utcluj.ecsb.watchmaker.preprocessing;

import org.apache.log4j.Logger;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.SelectionStrategy;
import org.uncommons.watchmaker.framework.SequentialEvolutionEngine;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.*;
import utcluj.ecsb.watchmaker.*;
import utcluj.ecsb.watchmaker.metrics.FitnessMetric;
import utcluj.ecsb.watchmaker.metrics.Metrics;
import weka.classifiers.Classifier;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.core.AttributeStats;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

/**
 * User: adibo
 * Date: 12.12.2011
 * Time: 16:29
 */
public class EvolutionaryFactory {

    private Properties configuration;

    public EvolutionaryFactory(Properties configuration) {
        this.configuration = configuration;
    }

    public EvolutionEngine<Individual> buildEvolutionEngine() {

        final CandidateFactoryECSB candidateFactoryECSB = new CandidateFactoryECSB((float) 127.0);


        final Random rng = new MersenneTwisterRNG();

        return new SequentialEvolutionEngine<Individual>(candidateFactoryECSB,
                buildEvolutionPipeline(),
                buildEvaluator(),
                buildStrategy(),
                rng);

    }

    private EvolutionPipeline<Individual> buildEvolutionPipeline() {

        final int crossoverPoints = Integer.parseInt(configuration.getProperty("crossover_points"));
        final CrossoverECSB crossoverECSB = new CrossoverECSB(crossoverPoints);

        final Probability mutationProbability = new Probability(Double.parseDouble(configuration.getProperty("mutation_rate")));
        final MutationECSB mutationECSB = new MutationECSB(mutationProbability);

        return new EvolutionPipeline<Individual>(Arrays.asList(crossoverECSB, mutationECSB));
    }

    public FitnessEvaluatorECSB buildEvaluator() {

        final Instances instances = loadInstances(this.configuration.getProperty("dataset_path"));

        final int classIndex = instances.numAttributes() - 1;
        instances.setClassIndex(classIndex);
        AttributeStats classStats = instances.attributeStats(classIndex);

        final int minorityClassIndex = classStats.nominalCounts[0] > classStats.nominalCounts[1] ? 1 : 0;

        instances.randomize(new Random(1));

        final int numFolds = Integer.valueOf(this.configuration.getProperty("num_folds"));
        instances.stratify(numFolds);

        final String baseClassifierName = this.configuration.getProperty("base_classifier");

        final Classifier costClassifier;
        try {
            Class<?> costClassifierClass = Class.forName(this.configuration.getProperty("cost_classifier"));
            costClassifier = (Classifier) costClassifierClass.newInstance();
            if (costClassifier instanceof CostSensitiveClassifier) {
                ((CostSensitiveClassifier) costClassifier).setMinimizeExpectedCost(Boolean.parseBoolean(this.configuration.getProperty("use_reweight")));
            }
            final FitnessMetric fitnessMetric = buildFitnessMetric(this.configuration.getProperty("fitness_metric"), this.configuration.getProperty("beta"));

            return new FitnessEvaluatorECSB(instances, costClassifier, numFolds, baseClassifierName, fitnessMetric, minorityClassIndex);
        } catch (Exception e) {
            Logger.getLogger("ECSBLog").error("Unable to create the cost classifier. Fitness evaluator creation aborted.");
        }
        return null;
    }

    private Instances loadInstances(String datasetPath) {
        Instances instances = null;
        try {
            BufferedReader instancesReader = new BufferedReader(new FileReader(datasetPath));
            instances = new Instances(instancesReader);
            instancesReader.close(); // not safe
        } catch (Exception e) {
            Logger.getLogger("ECSBLog").error("Unable to load dataset.");
        }
        return instances;
    }

    private FitnessMetric buildFitnessMetric(String fitnessMetricName, String beta) {
        if ("GMMetric".equals(fitnessMetricName)) {
            return Metrics.getAGMMetric();
        } else if ("BaccMetric".equals(fitnessMetricName)) {
            return Metrics.getAGBaccMetric();
        } else if ("FMeasureMetric".equals(fitnessMetricName)) {
            return Metrics.getAFMeasureMetric(Double.valueOf(beta));
        } else if ("LinTPFNMetric".equals(fitnessMetricName)) {
            return Metrics.getALinTPFNMetric(Double.valueOf(beta));
        } else if ("LinTPPrecisionMetric".equals(fitnessMetricName)) {
            return Metrics.getALinTPPrecisionMetric(Double.valueOf(beta));
        } else if ("LinTPTNMetric".equals(fitnessMetricName)) {
            return Metrics.getALinTPTNMetric(Double.valueOf(beta));
        } else {
            Logger.getLogger("ECSBLog").error("Unable to create the fitness metric.");
            return null;
        }
    }

    private SelectionStrategy<? super Individual> buildStrategy() {
        final String strategyName = configuration.getProperty("selection_strategy");
        final double selectionParam = Double.parseDouble(configuration.getProperty("selection_param"));

        if (strategyName.contains("RankSelection")) {
            return new RankSelection();
        } else if (strategyName.contains("RouletteWheelSelection")) {
            return new RouletteWheelSelection();
        } else if (strategyName.contains("StochasticUniversalSampling")) {
            return new StochasticUniversalSampling();
        } else if (strategyName.contains("TournamentSelection")) {
            return new TournamentSelection(new Probability(selectionParam));
        } else if (strategyName.contains("TruncationSelection")) {
            return new TruncationSelection(selectionParam);
        } else {
            Logger.getLogger("ECSBLog").error("Unable to create the fitness metric.");
            return null;
        }
    }
}
