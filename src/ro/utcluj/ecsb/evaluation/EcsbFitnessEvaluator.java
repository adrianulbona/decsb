package ro.utcluj.ecsb.evaluation;

import org.apache.log4j.Logger;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import ro.utcluj.ecsb.metrics.FitnessMetric;
import ro.utcluj.ecsb.population.EcsbIndividual;
import weka.classifiers.Classifier;
import weka.classifiers.CostMatrix;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.classifiers.meta.MetaCost;
import weka.core.Instances;

import java.util.List;
import java.util.Random;


/**
 * User: adibo
 * Date: 11.12.2011
 * Time: 19:06
 */

public class EcsbFitnessEvaluator extends EcsbEvaluator implements FitnessEvaluator<EcsbIndividual> {

    private int numFolds;
    private FitnessMetric fitnessMetric;
    private int minorityClassIndex;

    public EcsbFitnessEvaluator(Instances trainSet, Classifier costClassifier, int numFolds,
                                String baseClassifierName, FitnessMetric fitnessMetric, int minorityClassIndex) {
        this.trainSet = trainSet;
        this.costClassifier = costClassifier;
        this.numFolds = numFolds;
        this.baseClassifierName = baseClassifierName;
        this.fitnessMetric = fitnessMetric;
        this.minorityClassIndex = minorityClassIndex;
    }

    public double getFitness(EcsbIndividual individual, List<? extends EcsbIndividual> individuals) {
        Evaluation individualEvaluation = evaluateIndividual(individual);
        return fitnessMetric.computeFitness(individualEvaluation, minorityClassIndex);
    }

    public Evaluation evaluateIndividual(EcsbIndividual individual) {

        Evaluation evaluation;

        CostMatrix costMatrix = new CostMatrix(trainSet.numClasses());

        costMatrix.setElement(0, 0, 0.0);
        costMatrix.setElement(1, 1, 0.0);
        costMatrix.setElement(0, 1, individual.getC1());
        costMatrix.setElement(1, 0, individual.getC2());

        Classifier baseClassifier = getBaseClassifier(baseClassifierName, individual.getParams()[0], individual.getParams()[1]);
        try {
            evaluation = new Evaluation(trainSet, costMatrix);
            if (costClassifier instanceof CostSensitiveClassifier) {
                ((CostSensitiveClassifier) costClassifier).setCostMatrix(costMatrix);
                ((CostSensitiveClassifier) costClassifier).setClassifier(baseClassifier);

                evaluation.crossValidateModel(costClassifier, trainSet, numFolds, new Random(1));
            } else if (costClassifier instanceof MetaCost) {
                ((MetaCost) costClassifier).setCostMatrix(costMatrix);
                ((MetaCost) costClassifier).setClassifier(baseClassifier);
                evaluation.crossValidateModel(costClassifier, trainSet, numFolds, new Random(1));
            } else {
                evaluation.crossValidateModel(baseClassifier, trainSet, numFolds, new Random(1));
            }
            return evaluation;
        } catch (Exception e) {
            Logger.getLogger("ECSBLog").error("Unable to compute fitness.");
            return null;
        }
    }

    public boolean isNatural() {
        return true;
    }

    public int getMinorityClassIndex() {
        return minorityClassIndex;
    }

}
