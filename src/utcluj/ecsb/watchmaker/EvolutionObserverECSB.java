package utcluj.ecsb.watchmaker;

import org.apache.log4j.Logger;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.PopulationData;

import java.text.DecimalFormat;

public class EvolutionObserverECSB implements EvolutionObserver<Individual> {

    private final DecimalFormat decimalFormatter = new DecimalFormat("#.####");

    @Override
    public void populationUpdate(PopulationData<? extends Individual> populationData) {

        final String message = String.format("generation %d: %s - SD: %s",
                populationData.getGenerationNumber(),
                decimalFormatter.format(populationData.getBestCandidateFitness()),
                decimalFormatter.format(populationData.getFitnessStandardDeviation()));
        Logger.getLogger("ECSBLog").info(message);
    }
}
