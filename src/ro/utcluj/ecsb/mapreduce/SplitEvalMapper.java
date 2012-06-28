package ro.utcluj.ecsb.mapreduce;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.log4j.Logger;
import ro.utcluj.ecsb.evaluation.EcsbFitnessEvaluator;
import ro.utcluj.ecsb.population.EcsbIndividual;
import ro.utcluj.ecsb.utils.EcsbFactory;
import ro.utcluj.ecsb.utils.StringUtils;
import weka.core.Instances;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;
import java.util.Random;
import java.util.SortedMap;

public class SplitEvalMapper extends Mapper<NullWritable, BytesWritable, IntWritable, DoubleWritable> {

    @Override
    protected void map(NullWritable key, BytesWritable value, Context context) throws IOException, InterruptedException {
        Properties decsbProps = StringUtils.fromString(context.getConfiguration().get("decsb.props"));

        Reader reader = new InputStreamReader(new ByteArrayInputStream(value.getBytes()));

        Instances instances = new Instances(reader);

        final int numFolds = Integer.valueOf(decsbProps.getProperty("num_folds"));

        instances.setClassIndex(instances.numAttributes() - 1);

        instances.randomize(new Random(1));
        instances.stratify(numFolds);

        String populationString = context.getConfiguration().get("decsb.pop");

        SortedMap<Integer, EcsbIndividual> population = StringUtils.fromString(populationString);

        EcsbFitnessEvaluator fitnessEvaluator = new EcsbFactory(decsbProps).buildEvaluator(instances);

        for (Integer indexIndividual : population.keySet()){
            DoubleWritable fitnessValue = new DoubleWritable(fitnessEvaluator.getFitness(population.get(indexIndividual), null));
            Logger.getLogger(SplitEvalMapper.class).info(((FileSplit) context.getInputSplit()).getPath() +" : "+ indexIndividual + " --> " + fitnessValue);
            context.write(new IntWritable(indexIndividual), fitnessValue);
        }
    }
}
