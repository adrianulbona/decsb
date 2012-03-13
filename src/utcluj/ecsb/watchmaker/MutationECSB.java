package utcluj.ecsb.watchmaker;

import org.uncommons.maths.binary.BitString;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.operators.BitStringMutation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: adibo
 * Date: 18.12.2011
 * Time: 20:14
 * To change this template use File | Settings | File Templates.
 */
public class MutationECSB implements EvolutionaryOperator<Individual>{
    private BitStringMutation bitStringMutation;

    public MutationECSB(Probability mutationProbability) {
        super();
        this.bitStringMutation = new BitStringMutation(mutationProbability);
    }

    public List<Individual> apply(List<Individual> individuals, Random random) {

        List<Individual> individualsAfterMutation = new ArrayList<Individual>();
        for(Individual individual : individuals){
            individualsAfterMutation.add(mutate(individual,random));
        }
        return individualsAfterMutation;
    }

    public Individual mutate(Individual individual,Random random){
        float[] individualAsFloatArray = individualAsFloatArray = individual.asFloatArray();;
        int floatArrayLength = individualAsFloatArray.length;

        float[] individualAfterMutationAsFloatArray = new float[floatArrayLength];

        int individualAsInt;
        int sign;
        int exponent;
        int chromosome;
        BitString bs;

        List<BitString> bitStringsAfterMutation;

        for (int k = 0; k < floatArrayLength;k++){
            individualAsInt = Float.floatToIntBits(individualAsFloatArray[k]);
            sign     = individualAsInt & 0x80000000;
            exponent = individualAsInt & 0x7f800000;

            chromosome = individualAsInt & 0x007fffff;   //mantissa
            bs = new BitString(Long.toBinaryString(0x00800000 | chromosome).substring(1));

            bitStringsAfterMutation = bitStringMutation.apply(Arrays.asList(bs),random);

            individualAfterMutationAsFloatArray[k] = Float.intBitsToFloat(sign | exponent | (int)Integer.parseInt(bitStringsAfterMutation.get(0).toString(),2));
        }

        return Individual.fromFloatArray(individualAfterMutationAsFloatArray);
    }
}
