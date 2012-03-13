package utcluj.ecsb.tests;

import org.junit.Test;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.TruncationSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import utcluj.ecsb.preprocessing.ConfigurationHandler;
import utcluj.ecsb.watchmaker.*;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: adibo
 * Date: 11.12.2011
 * Time: 20:07
 * To change this template use File | Settings | File Templates.
 */
public class ECSBTest {

    @Test
    public void test() throws Exception {

        ConfigurationHandler configurationHandler = new ConfigurationHandler("conf.xml");

        CandidateFactoryECSB candidateFactoryECSB = new CandidateFactoryECSB((float)127.0);


        CrossoverECSB crossoverECSB = new CrossoverECSB(5);
        MutationECSB mutationECSB = new MutationECSB(Probability.ONE);
        EvolutionPipeline<Individual> evolutionPipeline = new EvolutionPipeline<Individual>(
                Arrays.asList(crossoverECSB,mutationECSB));



        FitnessEvaluatorECSB fitnessEvaluator = new FitnessEvaluatorECSB(
                    configurationHandler.getInstances(),
                    configurationHandler.getCostClassifier(),
                    configurationHandler.getConfiguration().getNumFolds(),
                    configurationHandler.getBaseClassifierName(),
                    configurationHandler.getFitnessMetric(),
                    configurationHandler.getMinorityClassIndex());

        SelectionStrategy<Object> selection = new TruncationSelection(0.4);
        Random rng = new MersenneTwisterRNG();


        EvolutionEngine<Individual> engine
                    = new GenerationalEvolutionEngine<Individual>(candidateFactoryECSB,
                                              evolutionPipeline,
                                              fitnessEvaluator,
                                              selection,
                                              rng);





        engine.addEvolutionObserver(new EvolutionObserver<Individual>() {
            public void populationUpdate(PopulationData<? extends Individual> populationData) {
                System.out.printf("Generation %d: %s - pSize: %d - SD: %s\n",
                    populationData.getGenerationNumber(),
                    populationData.getBestCandidateFitness(),
                    populationData.getPopulationSize(),
                    populationData.getFitnessStandardDeviation());

            }});

        for(EvaluatedCandidate<Individual> evaluatedCandidate : engine.evolvePopulation(20, 10, new GenerationCount(100))){
            System.out.println(evaluatedCandidate.getCandidate());
            System.out.println(evaluatedCandidate.getFitness());
        }




    }
}
