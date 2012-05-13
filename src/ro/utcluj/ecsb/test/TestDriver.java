package ro.utcluj.ecsb.test;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import ro.utcluj.ecsb.hadoop.EvaluationJobFactory;
import ro.utcluj.ecsb.population.EcsbCandidateFactory;
import ro.utcluj.ecsb.utils.EcsbUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class TestDriver extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.printf("Usage: %s [generic options] <input> <output>\n",
                    getClass().getSimpleName());
            ToolRunner.printGenericCommandUsage(System.err);
            return -1;
        }

        Collection<Double> evaluations = new ArrayList<Double>();

        EvaluationJobFactory.buildJobAndEvaluate(
                new EcsbCandidateFactory((float) 128.0).generateInitialPopulation(25, new Random(1)),
                evaluations,
                EcsbUtils.loadConfiguration("decsb.properties"));



        System.out.println(evaluations);


        return 0;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new TestDriver(), args);
        System.exit(exitCode);
    }
}

