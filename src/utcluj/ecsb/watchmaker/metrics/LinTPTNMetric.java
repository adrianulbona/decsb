package utcluj.ecsb.watchmaker.metrics;

import weka.classifiers.Evaluation;

/**
 * Created by IntelliJ IDEA.
 * User: adibo
 * Date: 18.12.2011
 * Time: 21:42
 * To change this template use File | Settings | File Templates.
 */



public class LinTPTNMetric extends FitnessMetricWithBeta{

    public double computeFitness(Evaluation evaluation, int minorityClassIndex) {
        return beta * evaluation.truePositiveRate(minorityClassIndex) +
                (1.0-beta)*evaluation.trueNegativeRate(minorityClassIndex);
    }
}
