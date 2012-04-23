package utcluj.ecsb.watchmaker;

import org.apache.log4j.Logger;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.PopulationData;
import weka.classifiers.Evaluation;

import java.text.DecimalFormat;

public class EvolutionObserverECSB implements EvolutionObserver<Individual> {

    private final DecimalFormat decimalFormatter;

    private final FitnessEvaluatorECSB fitnessEvaluator;

    private final int minorityClassIndex;

    public EvolutionObserverECSB(FitnessEvaluatorECSB fitnessEvaluator) {
        this.fitnessEvaluator = fitnessEvaluator;
        this.minorityClassIndex = fitnessEvaluator.getMinorityClassIndex();
        this.decimalFormatter = new DecimalFormat("#.###");
    }

    @Override
    public void populationUpdate(PopulationData<? extends Individual> populationData) {
        Evaluation evaluation = fitnessEvaluator.evaluateIndividual(populationData.getBestCandidate());

        final String message = String.format("%d\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s",
                populationData.getGenerationNumber(),
                decimalFormatter.format(populationData.getBestCandidateFitness()),
                decimalFormatter.format(populationData.getFitnessStandardDeviation()),
                decimalFormatter.format(populationData.getMeanFitness()),
                decimalFormatter.format(evaluation.truePositiveRate(this.minorityClassIndex)),
                decimalFormatter.format(evaluation.falsePositiveRate(this.minorityClassIndex)),
                decimalFormatter.format(evaluation.trueNegativeRate(this.minorityClassIndex)),
                decimalFormatter.format(evaluation.fMeasure(this.minorityClassIndex)),
                decimalFormatter.format(evaluation.precision(this.minorityClassIndex))
        );

        Logger.getLogger("ECSBLog").info(message);
    }
    public static String getHeader(){
        return "\tFitness\tSDev\tMean\tTrueP\tFalseP\tTrueN\tFMeas\tPrecision";
    }
}
