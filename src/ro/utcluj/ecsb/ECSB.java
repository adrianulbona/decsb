package ro.utcluj.ecsb;

import org.apache.log4j.Logger;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import ro.utcluj.ecsb.evaluation.EcsbModelEvaluator;
import ro.utcluj.ecsb.population.EcsbIndividual;
import ro.utcluj.ecsb.utils.EcsbFactory;
import ro.utcluj.ecsb.utils.Utils;

import java.io.IOException;

public class ECSB {

    final private EvolutionEngine<EcsbIndividual> engine;
    final private int populationCount;
    final private int eliteCount;
    final private int numberOfGenerations;
    final private EcsbModelEvaluator validator;

    public ECSB(EvolutionEngine<EcsbIndividual> engine, int numberOfGenerations, int eliteCount, int populationCount, EcsbModelEvaluator validator) {
        this.engine = engine;
        this.numberOfGenerations = numberOfGenerations;
        this.eliteCount = eliteCount;
        this.populationCount = populationCount;
        this.validator = validator;
    }


    public static void main(String[] args) {

        Utils.initLogger();

        try {
            if (args.length == 0) {
                final ECSB ecsb = new EcsbFactory(Utils.loadConfiguration(System.getProperty("user.dir")
                        + "/decsb.properties")).setUpECSB();
                ecsb.runEvolutionaryCostSensitiveBalancing();
            } else {
                final ECSB ecsb = new EcsbFactory(Utils.loadConfiguration(args[0])).setUpECSB();
                ecsb.runEvolutionaryCostSensitiveBalancing();
            }

        } catch (IOException e) {
            Logger.getLogger("ECSBLog").error("Unable to load configuration file.");
        }

    }

    public void runEvolutionaryCostSensitiveBalancing() {

        EcsbIndividual bestIndividual = engine.evolve(populationCount, eliteCount, new GenerationCount(numberOfGenerations));

        Logger.getLogger("ECSBLog").info("best individual: \t" + bestIndividual);

        validator.logIndividualEvaluation(bestIndividual);
    }

}
