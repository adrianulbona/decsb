package utcluj.ecsb.watchmaker.metrics;

import weka.classifiers.Evaluation;

/**
 * Created by IntelliJ IDEA.
 * User: adibo
 * Date: 18.12.2011
 * Time: 21:42
 * To change this template use File | Settings | File Templates.
 */



public class BaccMetric implements FitnessMetric{
    public double computeFitness(Evaluation evaluation, int minorityClassIndex) {
        return (evaluation.truePositiveRate(minorityClassIndex)+evaluation.trueNegativeRate(minorityClassIndex))/2;
    }
}
