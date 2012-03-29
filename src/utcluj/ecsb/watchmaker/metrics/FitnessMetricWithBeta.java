package utcluj.ecsb.watchmaker.metrics;

/**
 * User: adibo
 * Date: 18.12.2011
 */
public abstract class FitnessMetricWithBeta implements FitnessMetric {
    protected double beta;

    public FitnessMetricWithBeta(double beta) {
        this.beta = beta;
    }
}
