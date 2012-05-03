package ro.utcluj.ecsb.evaluation;

import org.apache.log4j.Logger;
import ro.utcluj.ecsb.population.EcsbIndividual;
import weka.classifiers.Classifier;
import weka.classifiers.CostMatrix;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.classifiers.meta.MetaCost;
import weka.core.Instances;

public class EcsbModelEvaluator extends EcsbEvaluator {

    private Instances testSet;

    public EcsbModelEvaluator(Instances trainSet, Instances testSet, Classifier costClassifier, String baseClassifierName) {
        this.trainSet = trainSet;
        this.testSet = testSet;
        this.costClassifier = costClassifier;
        this.baseClassifierName = baseClassifierName;
    }

    public void logIndividualEvaluation(EcsbIndividual individual) {
        Evaluation individualEvaluation = validate(individual);
        try {
            Logger.getLogger("ECSBLog").info("evaluation: " + individualEvaluation.toSummaryString()
                    + "\n" + individualEvaluation.toClassDetailsString());
        } catch (Exception e) {
            Logger.getLogger("ECSBLog").warn("Exception when trying to print evaluation details per class.");
        }
    }

    public Evaluation validate(EcsbIndividual individual) {
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
            } else if (costClassifier instanceof MetaCost) {
                ((MetaCost) costClassifier).setCostMatrix(costMatrix);
                ((MetaCost) costClassifier).setClassifier(baseClassifier);
            }
            costClassifier.buildClassifier(trainSet);
            evaluation.evaluateModel(costClassifier, testSet);

            return evaluation;
        } catch (Exception e) {
            Logger.getLogger("ECSBLog").error("Unable to compute fitness.");
            return null;
        }
    }
}
