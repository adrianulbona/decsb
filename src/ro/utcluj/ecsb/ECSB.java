package ro.utcluj.ecsb;

import org.apache.log4j.Logger;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import ro.utcluj.ecsb.evaluation.EcsbModelEvaluator;
import ro.utcluj.ecsb.population.EcsbIndividual;
import ro.utcluj.ecsb.utils.EcsbFactory;
import ro.utcluj.ecsb.utils.EcsbUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Properties;

public class ECSB {
    public static String CONF_PATH = "./conf/";
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
        try {
            final Properties configuration;
            boolean distributed = false;
            if (args.length > 1 && args[0].equals("-d")){
                distributed = true;
                CONF_PATH = args[1];
                }
            else if (args.length > 0 && args[0].equals("-d")){
                distributed = true;
            }
            else if (args.length > 0){
                CONF_PATH = args[0];
            }
            configuration = EcsbUtils.loadConfiguration(distributed, CONF_PATH + "decsb.properties");
            EcsbUtils.initLogger(distributed, configuration.get("result_file").toString());


     final ECSB ecsb = new EcsbFactory(configuration).setUpECSB(distributed);
            Logger.getLogger(ECSB.class).info(EcsbUtils.propertiesToString(configuration));
            Logger.getLogger(ECSB.class).info("starting time: " + new Timestamp(System.currentTimeMillis()));
            ecsb.runEvolutionaryCostSensitiveBalancing();
            Logger.getLogger(ECSB.class).info("end time: " + new Timestamp(System.currentTimeMillis()));

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
