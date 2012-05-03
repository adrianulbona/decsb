package ro.utcluj.ecsb.population;

import org.uncommons.watchmaker.framework.CandidateFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;


/**
 * User: adibo
 * Date: 11.12.2011
 */
public class EcsbCandidateFactory implements CandidateFactory<EcsbIndividual> {
    private float chromosomeMaxValue;

    public EcsbCandidateFactory(float chromosomeMaxValue) {
        this.chromosomeMaxValue = chromosomeMaxValue;
    }

    public List<EcsbIndividual> generateInitialPopulation(int n, Random random) {

        List<EcsbIndividual> population = new ArrayList<EcsbIndividual>();

        for (int i = 0; i < n; i++) {
            EcsbIndividual candidate;
            do {
                candidate = generateRandomCandidate(random);
            } while (!candidate.isValid());
            population.add(generateRandomCandidate(random));

        }
        return population;
    }

    public List<EcsbIndividual> generateInitialPopulation(int i, Collection<EcsbIndividual> individuals, Random random) {
        return generateInitialPopulation(i, random);
    }

    public EcsbIndividual generateRandomCandidate(Random random) {
        //System.out.println(individual);
        return new EcsbIndividual(
                random.nextFloat() * chromosomeMaxValue,
                random.nextFloat() * chromosomeMaxValue,
                new float[]{random.nextFloat() * chromosomeMaxValue, random.nextFloat() * chromosomeMaxValue});
    }
}
