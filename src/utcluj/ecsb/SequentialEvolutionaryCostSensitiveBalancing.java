package utcluj.ecsb;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import utcluj.ecsb.preprocessing.ConfigurationHandler;
import utcluj.ecsb.watchmaker.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

public class SequentialEvolutionaryCostSensitiveBalancing {
    public static final String PROPERTIES_FILE = "decsb.properties";
    public static final String[] metrics = {"FMeasureMetric", "BaccMetric", "GMMetric"};

    public static void main(String[] args) {
        SequentialEvolutionaryCostSensitiveBalancing engine = new SequentialEvolutionaryCostSensitiveBalancing();

        PropertyConfigurator.configure("log4j.properties");

        Properties configurationProperties = engine.loadConfiguration(PROPERTIES_FILE);
        Logger.getLogger("ECSBLog").info(configurationProperties);

        for (String metric : metrics) {
            Logger.getLogger("ECSBLog").info(metric);
            configurationProperties.setProperty("fitness_metric", metric);
            engine.runEvolutionaryCostSensitiveBalancing(configurationProperties);
        }
    }

    private void runEvolutionaryCostSensitiveBalancing(Properties configurationProperties) {
        final ConfigurationHandler configurationHandler = new ConfigurationHandler(configurationProperties);

        final CandidateFactoryECSB candidateFactoryECSB = new CandidateFactoryECSB((float) 127.0);


        final CrossoverECSB crossoverECSB = new CrossoverECSB(5);
        final MutationECSB mutationECSB = new MutationECSB(Probability.EVENS);
        final EvolutionPipeline<Individual> evolutionPipeline;
        evolutionPipeline = new EvolutionPipeline<>(
                Arrays.asList(crossoverECSB, mutationECSB));


        final FitnessEvaluatorECSB fitnessEvaluator = new FitnessEvaluatorECSB(
                configurationHandler.getInstances(),
                configurationHandler.getCostClassifier(),
                configurationHandler.getNumFolds(),
                configurationHandler.getBaseClassifierName(),
                configurationHandler.getFitnessMetric(),
                configurationHandler.getMinorityClassIndex());

        final SelectionStrategy<Object> selection = new RouletteWheelSelection();
        final Random rng = new MersenneTwisterRNG();


        final EvolutionEngine<Individual> engine;
        engine = new SequentialEvolutionEngine<>(candidateFactoryECSB,
                evolutionPipeline,
                fitnessEvaluator,
                selection,
                rng);


        engine.addEvolutionObserver(new EvolutionObserver<Individual>() {
            private DecimalFormat decimalFormatter = new DecimalFormat("#.####");

            public void populationUpdate(PopulationData<? extends Individual> populationData) {

                final String message = String.format("generation %d: %s - SD: %s",
                        populationData.getGenerationNumber(),
                        decimalFormatter.format(populationData.getBestCandidateFitness()),
                        decimalFormatter.format(populationData.getFitnessStandardDeviation()));
                Logger.getLogger("ECSBLog").info(message);
            }
        });

        final int populationCount = Integer.parseInt(configurationProperties.getProperty("population_count"));
        final int eliteCount = Integer.parseInt(configurationProperties.getProperty("elite_count"));
        final int numberOfGenerations = Integer.parseInt(configurationProperties.getProperty("number_of_generations"));


        Individual bestIndividual = engine.evolve(populationCount, eliteCount, new GenerationCount(numberOfGenerations));
        Logger.getLogger("ECSBLog").info("best individual: " + bestIndividual);
        fitnessEvaluator.logIndividualEvaluation(bestIndividual);


    }

    private Properties loadConfiguration(String propertiesFile) {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(propertiesFile)) {
            props.load(in);
        } catch (IOException e) {
            Logger.getLogger("DECSBLog").error("Unable to load props file.");
        }
        return props;
    }
}
