package utcluj.ecsb.watchmaker.metrics;

import weka.classifiers.Evaluation;

/**
 * Created by IntelliJ IDEA.
 * User: adibo
 * Date: 18.12.2011
 * Time: 21:42
 * To change this template use File | Settings | File Templates.
 */




public class FMeasureMetric extends FitnessMetricWithBeta{

    public double computeFitness(Evaluation evaluation, int minorityClassIndex) {
        return (1+Math.pow(beta, 2))*evaluation.precision(minorityClassIndex) * evaluation.recall(minorityClassIndex)/
               (Math.pow(beta, 2) * evaluation.precision(minorityClassIndex) + evaluation.recall(minorityClassIndex));
    }
}
