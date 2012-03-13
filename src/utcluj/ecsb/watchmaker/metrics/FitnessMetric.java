package utcluj.ecsb.watchmaker.metrics;

import weka.classifiers.Evaluation;

/**
 * Created by IntelliJ IDEA.
 * User: adibo
 * Date: 18.12.2011
 * Time: 21:38
 * To change this template use File | Settings | File Templates.
 */
public interface FitnessMetric {
    double computeFitness(Evaluation evaluation, int minorityClassIndex);
}
