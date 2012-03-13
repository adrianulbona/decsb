package utcluj.ecsb.watchmaker.metrics;

/**
 * Created by IntelliJ IDEA.
 * User: adibo
 * Date: 18.12.2011
 * Time: 22:16
 * To change this template use File | Settings | File Templates.
 */
public abstract class FitnessMetricWithBeta implements FitnessMetric{
    protected double beta;
    protected FitnessMetricWithBeta(double beta) {
        this.beta = beta;
    }

    protected FitnessMetricWithBeta() {
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }
}
