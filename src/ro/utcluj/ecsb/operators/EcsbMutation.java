package ro.utcluj.ecsb.operators;

import org.uncommons.maths.binary.BitString;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.operators.BitStringMutation;
import ro.utcluj.ecsb.population.EcsbIndividual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * User: adibo
 * Date: 11.12.2011
 * Time: 19:06
 */

public class EcsbMutation implements EvolutionaryOperator<EcsbIndividual> {
    private BitStringMutation bitStringMutation;

    public EcsbMutation(Probability mutationProbability) {
        super();
        this.bitStringMutation = new BitStringMutation(mutationProbability);
    }

    public List<EcsbIndividual> apply(List<EcsbIndividual> individuals, Random random) {

        List<EcsbIndividual> individualsAfterMutation = new ArrayList<EcsbIndividual>();
        for (EcsbIndividual individual : individuals) {
            individualsAfterMutation.add(mutate(individual, random));
        }
        return individualsAfterMutation;
    }

    public EcsbIndividual mutate(EcsbIndividual individual, Random random) {
        float[] individualAsFloatArray = individual.asFloatArray();
        int floatArrayLength = individualAsFloatArray.length;

        float[] individualAfterMutationAsFloatArray = new float[floatArrayLength];

        int individualAsInt;
        int sign;
        int exponent;
        int chromosome;
        BitString bs;

        List<BitString> bitStringsAfterMutation;

        for (int k = 0; k < floatArrayLength; k++) {
            individualAsInt = Float.floatToIntBits(individualAsFloatArray[k]);
            sign = individualAsInt & 0x80000000;
            exponent = individualAsInt & 0x7f800000;

            chromosome = individualAsInt & 0x007fffff;   //mantissa
            bs = new BitString(Long.toBinaryString(0x00800000 | chromosome).substring(1));

            bitStringsAfterMutation = bitStringMutation.apply(Arrays.asList(bs), random);

            individualAfterMutationAsFloatArray[k] = Float.intBitsToFloat(sign | exponent | Integer.parseInt(bitStringsAfterMutation.get(0).toString(), 2));
        }

        return EcsbIndividual.fromFloatArray(individualAfterMutationAsFloatArray);
    }
}
