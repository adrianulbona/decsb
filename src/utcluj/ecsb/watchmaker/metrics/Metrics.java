package utcluj.ecsb.watchmaker.metrics;

import weka.classifiers.Evaluation;


/**
 * User: adibo
 * Date: 27.03.2011
 */
public class Metrics {

    private Metrics() {
    }

    public static FitnessMetric getAGMMetric() {
        return new FitnessMetric() {

            @Override
            public double computeFitness(Evaluation evaluation, int minorityClassIndex) {
                return Math.sqrt(evaluation.truePositiveRate(minorityClassIndex) * evaluation.trueNegativeRate(minorityClassIndex));
            }
        };
    }

    public static FitnessMetric getAGBaccMetric() {
        return new FitnessMetric() {

            @Override
            public double computeFitness(Evaluation evaluation, int minorityClassIndex) {
                return (evaluation.truePositiveRate(minorityClassIndex) + evaluation.trueNegativeRate(minorityClassIndex)) / 2;
            }
        };
    }

    public static FitnessMetric getAFMeasureMetric(double beta) {
        return new FitnessMetricWithBeta(beta) {

            @Override
            public double computeFitness(Evaluation evaluation, int minorityClassIndex) {
                if (Math.pow(beta, 2) * evaluation.precision(minorityClassIndex) + evaluation.recall(minorityClassIndex) == 0.0) {
                    return 0.0;
                } else {
                    return (1 + Math.pow(beta, 2)) * evaluation.precision(minorityClassIndex) * evaluation.recall(minorityClassIndex) /
                            (Math.pow(beta, 2) * evaluation.precision(minorityClassIndex) + evaluation.recall(minorityClassIndex));
                }
            }
        };
    }

    public static FitnessMetric getALinTPFNMetric(double beta) {
        return new FitnessMetricWithBeta(beta) {

            @Override
            public double computeFitness(Evaluation evaluation, int minorityClassIndex) {
                return beta * evaluation.truePositiveRate(minorityClassIndex) +
                        (1.0 - beta) * evaluation.falseNegativeRate(minorityClassIndex);
            }
        };
    }

    public static FitnessMetric getALinTPPrecisionMetric(double beta) {
        return new FitnessMetricWithBeta(beta) {

            @Override
            public double computeFitness(Evaluation evaluation, int minorityClassIndex) {
                return beta * evaluation.truePositiveRate(minorityClassIndex) +
                        (1.0 - beta) * evaluation.precision(minorityClassIndex);
            }
        };
    }

    public static FitnessMetric getALinTPTNMetric(double beta) {
        return new FitnessMetricWithBeta(beta) {

            @Override
            public double computeFitness(Evaluation evaluation, int minorityClassIndex) {
                return beta * evaluation.truePositiveRate(minorityClassIndex) +
                        (1.0 - beta) * evaluation.trueNegativeRate(minorityClassIndex);
            }
        };
    }
}
