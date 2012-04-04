package utcluj.ecsb.watchmaker;

import org.uncommons.watchmaker.framework.CandidateFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;


/**
 * User: adibo
 * Date: 11.12.2011
 */
public class CandidateFactoryECSB implements CandidateFactory<Individual> {
    private float chromosomeMaxValue;

    public CandidateFactoryECSB(float chromosomeMaxValue) {
        this.chromosomeMaxValue = chromosomeMaxValue;
    }

    public List<Individual> generateInitialPopulation(int n, Random random) {

        List<Individual> population = new ArrayList<Individual>();

        for (int i = 0; i < n; i++) {
            Individual candidate;
            do {
                candidate = generateRandomCandidate(random);
            } while (!candidate.isValid());
            population.add(generateRandomCandidate(random));

        }
        return population;
    }

    public List<Individual> generateInitialPopulation(int i, Collection<Individual> individuals, Random random) {
        return generateInitialPopulation(i, random);
    }

    public Individual generateRandomCandidate(Random random) {
        //System.out.println(individual);
        return new Individual(
                random.nextFloat() * chromosomeMaxValue,
                random.nextFloat() * chromosomeMaxValue,
                new float[]{random.nextFloat() * chromosomeMaxValue, random.nextFloat() * chromosomeMaxValue});
    }
}
