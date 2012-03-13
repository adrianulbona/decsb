package utcluj.ecsb.tests;

import org.junit.Test;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import utcluj.ecsb.watchmaker.CandidateFactoryECSB;
import utcluj.ecsb.watchmaker.Individual;
import utcluj.ecsb.watchmaker.MutationECSB;

import java.util.List;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: adibo
 * Date: 18.12.2011
 * Time: 20:53
 * To change this template use File | Settings | File Templates.
 */
public class MutationECSBTest {
    @Test
    public void testMutation() throws Exception {

        Random rng = new MersenneTwisterRNG();
        CandidateFactoryECSB candidateFactoryECSB = new CandidateFactoryECSB((float)127.0);
        List<Individual> population = candidateFactoryECSB.generateInitialPopulation(5,rng);
        MutationECSB mutationECSB = new MutationECSB(Probability.ONE);


        System.out.println("After mutation: ");
        for(Individual individual : mutationECSB.apply(population,rng)){
            System.out.println(individual);

        }
    }
}
