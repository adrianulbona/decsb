package ro.utcluj.ecsb.operators;

import org.uncommons.maths.binary.BitString;
import org.uncommons.watchmaker.framework.operators.AbstractCrossover;
import org.uncommons.watchmaker.framework.operators.BitStringCrossover;
import ro.utcluj.ecsb.population.EcsbIndividual;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * User: adibo
 * Date: 11.12.2011
 */
public class EcsbCrossover extends AbstractCrossover<EcsbIndividual> {
    private BitStringCrossover bitStringCrossover;

    public EcsbCrossover(int crossoverPoints) {
        super(crossoverPoints);
        bitStringCrossover = new BitStringCrossover(crossoverPoints);
    }

    @Override
    protected List<EcsbIndividual> mate(EcsbIndividual individual1, EcsbIndividual individual2, int i, Random random) {

        float[] individual1AsFloatArray = individual1.asFloatArray();
        float[] individual2AsFloatArray = individual2.asFloatArray();

        int floatArrayLength = individual1AsFloatArray.length;

        float[] son1AsFloatArray = new float[floatArrayLength];
        float[] son2AsFloatArray = new float[floatArrayLength];

        int individual1AsInt;
        int sign1;
        int exponent1;

        int individual2AsInt;
        int sign2;
        int exponent2;


        int chromosome1;
        int chromosome2;

        BitString bs1;
        BitString bs2;

        List<BitString> bitStringsAfterCrossover;

        for (int k = 0; k < floatArrayLength; k++) {
            individual1AsInt = Float.floatToIntBits(individual1AsFloatArray[k]);

            sign1 = individual1AsInt & 0x80000000;
            exponent1 = individual1AsInt & 0x7f800000;
            chromosome1 = individual1AsInt & 0x007fffff;   //mantissa

            individual2AsInt = Float.floatToIntBits(individual2AsFloatArray[k]);
            sign2 = individual2AsInt & 0x80000000;
            exponent2 = individual2AsInt & 0x7f800000;
            chromosome2 = individual2AsInt & 0x007fffff;   //mantissa

            bs1 = new BitString(Long.toBinaryString(0x00800000 | chromosome1).substring(1));
            bs2 = new BitString(Long.toBinaryString(0x00800000 | chromosome2).substring(1));


            bitStringsAfterCrossover = bitStringCrossover.apply(Arrays.asList(bs1, bs2), random);

            son1AsFloatArray[k] = Float.intBitsToFloat(sign1 | exponent1 | Integer.parseInt(bitStringsAfterCrossover.get(0).toString(), 2));
            son2AsFloatArray[k] = Float.intBitsToFloat(sign2 | exponent2 | Integer.parseInt(bitStringsAfterCrossover.get(1).toString(), 2));
        }

        return Arrays.asList(EcsbIndividual.fromFloatArray(son1AsFloatArray), EcsbIndividual.fromFloatArray(son2AsFloatArray));
    }

}
