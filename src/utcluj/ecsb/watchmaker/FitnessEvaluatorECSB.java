package utcluj.ecsb.watchmaker;

import org.uncommons.watchmaker.framework.FitnessEvaluator;
import utcluj.ecsb.watchmaker.metrics.FitnessMetric;
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

public class FitnessEvaluatorECSB implements FitnessEvaluator<Individual>{
    private Instances dataset;
    private Classifier costClassifier;
    private int numFolds;
    private String baseClassifierName;
    private FitnessMetric fitnessMetric;
    private int minorityClassIndex;

    public FitnessEvaluatorECSB(Instances dataset, Classifier costClassifier, int numFolds,
                                String baseClassifierName, FitnessMetric fitnessMetric, int minorityClassIndex) {
        this.dataset = dataset;
        this.costClassifier = costClassifier;
        this.numFolds = numFolds;
        this.baseClassifierName = baseClassifierName;
        this.fitnessMetric = fitnessMetric;
        this.minorityClassIndex = minorityClassIndex;
    }

    public Instances getDataset() {
        return dataset;
    }
    public void setDataset(Instances dataset) {
        this.dataset = dataset;
    }

    public Classifier getCostClassifier() {
        return costClassifier;
    }

    public void setCostClassifier(Classifier costClassifier) {
        this.costClassifier = costClassifier;
    }

    public double getFitness(Individual individual, List<? extends Individual> individuals) {

        Evaluation eval = null;

        CostMatrix costMatrix = new CostMatrix(dataset.numClasses());

	    costMatrix.setElement(0, 0, 0.0);
        costMatrix.setElement(1, 1, 0.0);
        costMatrix.setElement(0, 1, individual.getC1());
        costMatrix.setElement(1, 0, individual.getC2());

        Classifier baseClassifier = getBaseClassifier(baseClassifierName, individual.getParams()[0], individual.getParams()[1]);
        try {
            eval = new Evaluation(dataset, costMatrix);
            if (costClassifier instanceof CostSensitiveClassifier){
                ((CostSensitiveClassifier)costClassifier).setCostMatrix(costMatrix);
                ((CostSensitiveClassifier)costClassifier).setClassifier(baseClassifier);

                eval.crossValidateModel(costClassifier, dataset, numFolds, new Random(1));
            }
            else if (costClassifier instanceof MetaCost){
                ((MetaCost)costClassifier).setCostMatrix(costMatrix);
                ((MetaCost)costClassifier).setClassifier(baseClassifier);
                eval.crossValidateModel(costClassifier, dataset, numFolds, new Random(1));
            }
            else{
                eval.crossValidateModel(baseClassifier, dataset, numFolds , new Random(1));
            }
        } catch (Exception e) {
		    System.out.println(eval.toSummaryString());
	    }
        return fitnessMetric.computeFitness(eval,minorityClassIndex);
    }

    public boolean isNatural() {
        return true;
    }
    private Classifier getBaseClassifier(String baseClassifierName, double c, double e){
        Classifier baseClassifier = null;
        try {

            Class<?> cls = Class.forName(baseClassifierName);
            baseClassifier = (Classifier)cls.newInstance();

            if(baseClassifier.getClass().toString().contains("IBk")){
                baseClassifier.setOptions(weka.core.Utils.splitOptions("-K "+(int)(c/12.8 + 1)));
            }
            else if(baseClassifier.getClass().toString().contains("J48")){
                baseClassifier.setOptions(weka.core.Utils.splitOptions("-C "+(double)((c/320))+" -M "+(int)(e/30 + 1))); //M sa fie sub 5, ca de nu da erori, ci C sub 0.4
            }
            else if(baseClassifier.getClass().toString().contains("SMO")){
                baseClassifier.setOptions(weka.core.Utils.splitOptions("-C "+(double)(c/12.8 + 1) +" -K \"weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E "+(int)(e/12.8 + 1)+"\""));
            }
            else if(baseClassifier.getClass().toString().contains("Multilayer")){
                baseClassifier.setOptions(weka.core.Utils.splitOptions("-L "+(double)(c/128) +" -M "+(double)(e/128)));
            }
            else if(baseClassifier.getClass().toString().contains("Ada")){
                baseClassifier.setOptions(weka.core.Utils.splitOptions("-P "+ (int)c +" -I "+(int)(e/4 + 1)));
            }
        } catch (Exception e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return baseClassifier;
    }

}
