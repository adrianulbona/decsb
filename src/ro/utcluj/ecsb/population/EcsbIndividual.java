package ro.utcluj.ecsb.population;

import java.text.DecimalFormat;

/**
 * User: adibo
 * Date: 11.12.2011
 * Time: 19:06
 */

public class EcsbIndividual {
    private float c1;
    private float c2;
    private float[] params;

    public EcsbIndividual(float c1, float c2, float[] params) {
        this.c1 = c1;
        this.c2 = c2;
        this.params = params;
    }

    public EcsbIndividual() {
    }

    public float getC1() {
        return c1;
    }

    public void setC1(float c1) {
        this.c1 = c1;
    }

    public float getC2() {
        return c2;
    }

    public void setC2(float c2) {
        this.c2 = c2;
    }

    public float[] getParams() {
        return params;
    }

    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");
        return "#c1: " + df.format(c1) + "\t#c2:" + df.format(c2) + "\t#p1:" + df.format(params[0]) + "\t#p2:" + df.format(params[1]);
    }

    public float[] asFloatArray() {
        float[] floatArray = new float[2 + params.length];
        floatArray[0] = c1;
        floatArray[1] = c2;
        int i = 2;
        for (float f : params) {
            floatArray[i++] = f;
        }
        return floatArray;
    }

    public static EcsbIndividual fromFloatArray(float[] individualAsFloatArray) {
        EcsbIndividual individual;
        if (individualAsFloatArray.length < 2) {
            individual = null;
        } else {
            individual = new EcsbIndividual();

            individual.setC1(individualAsFloatArray[0]);
            individual.setC2(individualAsFloatArray[1]);

            if (individualAsFloatArray.length > 2) { // if we have params
                individual.params = new float[individualAsFloatArray.length - 2];
                System.arraycopy(individualAsFloatArray, 2, individual.params, 0, individualAsFloatArray.length - 2);
            }
        }
        return individual;
    }

    public boolean isValid() {
        return !(this.c1 == 0.0 || this.c2 == 0.0);
    }
}
