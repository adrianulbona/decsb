package ro.utcluj.ecsb.metrics;

import weka.classifiers.Evaluation;


/**
 * User: adibo
 * Date: 11.12.2011
 */
public interface FitnessMetric {
    public double computeFitness(Evaluation evaluation, int minorityClassIndex);
}
