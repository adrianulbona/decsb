package utcluj.ecsb;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import utcluj.ecsb.watchmaker.EvolutionObserverECSB;
import utcluj.ecsb.watchmaker.FitnessEvaluatorECSB;
import utcluj.ecsb.watchmaker.Individual;
import utcluj.ecsb.watchmaker.Utils;
import utcluj.ecsb.watchmaker.preprocessing.EvolutionaryFactory;
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
            engine.runEvolutionaryCostSensitiveBalancing(Utils.loadConfiguration(args[0]));
        } catch (IOException e) {
            Logger.getLogger("ECSBLog").error("Unable to load configuration file.");
        }

    }

    public void runEvolutionaryCostSensitiveBalancing(Properties configuration) {

        final EvolutionaryFactory evolutionaryFactory = new EvolutionaryFactory(configuration);

        EvolutionEngine<Individual> engine = evolutionaryFactory.buildEvolutionEngine();

        FitnessEvaluatorECSB fitnessEvaluator = evolutionaryFactory.buildEvaluator();
        engine.addEvolutionObserver(new EvolutionObserverECSB(fitnessEvaluator));

        final int populationCount = Integer.parseInt(configuration.getProperty("population_count"));
        final int eliteCount = Integer.parseInt(configuration.getProperty("elite_count"));
        final int numberOfGenerations = Integer.parseInt(configuration.getProperty("number_of_generations"));


        Individual bestIndividual = engine.evolve(populationCount, eliteCount, new GenerationCount(numberOfGenerations));

        Logger.getLogger("ECSBLog").info("best individual: " + bestIndividual);

        fitnessEvaluator.logIndividualEvaluation(bestIndividual);
    }
}
