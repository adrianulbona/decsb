package utcluj.ecsb.tests;

import org.junit.Test;
import org.uncommons.maths.random.MersenneTwisterRNG;
import utcluj.ecsb.watchmaker.CandidateFactoryECSB;
import utcluj.ecsb.watchmaker.CrossoverECSB;
import utcluj.ecsb.watchmaker.Individual;

import java.util.List;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: adibo
 * Date: 18.12.2011
 * Time: 19:57
 * To change this template use File | Settings | File Templates.
 */
public class CrossoverECSBTest {
    @Test
    public void testCrossover() throws Exception {
        Random rng = new MersenneTwisterRNG();
        CandidateFactoryECSB candidateFactoryECSB = new CandidateFactoryECSB((float)127.0);
        List<Individual> population = candidateFactoryECSB.generateInitialPopulation(5,rng);
        CrossoverECSB crossoverECSB = new CrossoverECSB(5);


        System.out.println("After crossover: ");
        for(Individual individual : crossoverECSB.apply(population,rng)){
            System.out.println(individual);

        }

    }

}
