package utcluj.ecsb.watchmaker;

import org.uncommons.maths.binary.BitString;
import org.uncommons.watchmaker.framework.operators.AbstractCrossover;
import org.uncommons.watchmaker.framework.operators.BitStringCrossover;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: adibo
 * Date: 16.12.2011
 * Time: 00:49
 * To change this template use File | Settings | File Templates.
 */
public class CrossoverECSB extends AbstractCrossover<Individual> {
    private BitStringCrossover bitStringCrossover;

    public CrossoverECSB(int crossoverPoints) {
        super(crossoverPoints);
        bitStringCrossover = new BitStringCrossover(crossoverPoints);
    }

    @Override
    protected List<Individual> mate(Individual individual1, Individual individual2, int i, Random random) {

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

        for (int k = 0; k < floatArrayLength;k++){
            individual1AsInt = Float.floatToIntBits(individual1AsFloatArray[k]);

            sign1     = individual1AsInt & 0x80000000;
            exponent1 = individual1AsInt & 0x7f800000;
            chromosome1 = individual1AsInt & 0x007fffff;   //mantissa

            individual2AsInt = Float.floatToIntBits(individual2AsFloatArray[k]);
            sign2     = individual2AsInt & 0x80000000;
            exponent2 = individual2AsInt & 0x7f800000;
            chromosome2 = individual2AsInt & 0x007fffff;   //mantissa

            bs1 = new BitString(Long.toBinaryString(0x00800000 | chromosome1).substring(1));
            bs2 = new BitString(Long.toBinaryString(0x00800000 | chromosome2).substring(1));


            bitStringsAfterCrossover = bitStringCrossover.apply(Arrays.asList(bs1,bs2),random);

            son1AsFloatArray[k] = Float.intBitsToFloat(sign1 | exponent1 | (int)Integer.parseInt(bitStringsAfterCrossover.get(0).toString(),2));
            son2AsFloatArray[k] = Float.intBitsToFloat(sign2 | exponent2 | (int)Integer.parseInt(bitStringsAfterCrossover.get(1).toString(),2));
        }

        return Arrays.asList(Individual.fromFloatArray(son1AsFloatArray),Individual.fromFloatArray(son2AsFloatArray));
    }

}
