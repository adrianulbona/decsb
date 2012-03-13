package utcluj.ecsb.tests;

import org.junit.Test;
import utcluj.ecsb.watchmaker.CandidateFactoryECSB;
import utcluj.ecsb.watchmaker.Individual;

import java.util.List;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: adibo
 * Date: 14.12.2011
 * Time: 19:17
 * To change this template use File | Settings | File Templates.
 */
public class CandidateFactoryECSBTest {
    @Test
    public void testGenerateInitialPopulation() throws Exception {
        List<Individual> populationECSB = (new CandidateFactoryECSB((float)127.0)).generateInitialPopulation(10, new Random(1));

        for(Individual individual : populationECSB){
            System.out.println(individual);
        }
    }
}
