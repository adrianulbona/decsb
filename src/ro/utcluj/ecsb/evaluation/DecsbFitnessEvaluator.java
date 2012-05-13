package ro.utcluj.ecsb.evaluation;


import org.apache.log4j.Logger;
import ro.utcluj.ecsb.hadoop.EvaluationJobFactory;
import ro.utcluj.ecsb.population.EcsbIndividual;

import java.util.List;
import java.util.Properties;

public class DecsbFitnessEvaluator extends STFitnessEvaluator<EcsbIndividual>{

    private final Properties props;

    public DecsbFitnessEvaluator(Properties props) {
        this.props = props;
    }

    @Override
        protected void evaluate(List<? extends EcsbIndividual> population, List<Double> evaluations) {
        try {
            EvaluationJobFactory.buildJobAndEvaluate(population, evaluations, props);
            //Logger.getLogger(DecsbFitnessEvaluator.class).info("Evaluations: " + evaluations);
        } catch (Exception e) {
            Logger.getLogger(DecsbFitnessEvaluator.class).error("Unable to run distributed evaluations.");
        }

    }

    @Override
        public boolean isNatural() {
            return true;
        }


}
