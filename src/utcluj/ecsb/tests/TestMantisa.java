package utcluj.ecsb.tests;

/**
 * Created by IntelliJ IDEA.
 * User: adibo
 * Date: 18.12.2011
 * Time: 15:17
 * To change this template use File | Settings | File Templates.
 */
public class TestMantisa {
    public static void main(String[] args) {
        float f = -118.625f;
        int intBits = Float.floatToIntBits(f);
        int rawIntBits = Float.floatToRawIntBits(f);
        System.out.printf("f = %f  intBits = %d  " +
                          "rawIntBits = %d%n", f, intBits, rawIntBits);
        float toFloat = Float.intBitsToFloat(intBits);
        System.out.printf("toFloat = %f%n", toFloat);

        int sign     = intBits & 0x80000000;
        int exponent = intBits & 0x7f800000;
        int mantissa = intBits & 0x007fffff;
        System.out.printf("sign = %d  exponent = %d  mantissa = %d%n",
                           sign, exponent, mantissa);

        String binarySign = Integer.toBinaryString(sign);
        String binaryExponent = Integer.toBinaryString(exponent);
        String binaryMantissa = Integer.toBinaryString(mantissa);
        System.out.printf("binarySign     = %s%nbinaryExponent = %s%n" +
                          "binaryMantissa = %s%n", binarySign,
                           binaryExponent, binaryMantissa);
    }
}
