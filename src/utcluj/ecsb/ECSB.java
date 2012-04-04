package utcluj.ecsb;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import utcluj.ecsb.watchmaker.EvolutionObserverECSB;
import utcluj.ecsb.watchmaker.FitnessEvaluatorECSB;
import utcluj.ecsb.watchmaker.Individual;
import utcluj.ecsb.watchmaker.preprocessing.EvolutionaryFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ECSB {

    public static void main(String[] args) {

        PropertyConfigurator.configure("log4j.properties");
        ECSB engine = new ECSB();

        try {
            if (args.length == 0) {
                throw new IOException();
            }
            engine.runEvolutionaryCostSensitiveBalancing(engine.loadConfiguration(args[0]));
        } catch (IOException e) {
            Logger.getLogger("ECSBLog").error("Unable to load configuration file.");
        }

    }

    private void runEvolutionaryCostSensitiveBalancing(Properties configuration) {

        Logger.getLogger("ECSBLog").info(configuration);

        final EvolutionaryFactory evolutionaryFactory = new EvolutionaryFactory(configuration);

        EvolutionEngine<Individual> engine = evolutionaryFactory.buildEvolutionEngine();

        engine.addEvolutionObserver(new EvolutionObserverECSB());

        final int populationCount = Integer.parseInt(configuration.getProperty("population_count"));
        final int eliteCount = Integer.parseInt(configuration.getProperty("elite_count"));
        final int numberOfGenerations = Integer.parseInt(configuration.getProperty("number_of_generations"));


        Individual bestIndividual = engine.evolve(populationCount, eliteCount, new GenerationCount(numberOfGenerations));

        Logger.getLogger("ECSBLog").info("best individual: " + bestIndividual);

        FitnessEvaluatorECSB fitnessEvaluator = evolutionaryFactory.buildEvaluator();

        fitnessEvaluator.logIndividualEvaluation(bestIndividual);
    }

    private Properties loadConfiguration(String propertiesFile) throws IOException {
        Properties props = new Properties();
        BufferedReader in = new BufferedReader(new FileReader(propertiesFile));
        try {
            props.load(in);
        } catch (IOException e) {
            Logger.getLogger("ECSBLog").error("Unable to load props file.");
        } finally {
            in.close();
        }
        return props;
    }
}
