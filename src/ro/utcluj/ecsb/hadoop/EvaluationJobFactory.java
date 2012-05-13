package ro.utcluj.ecsb.hadoop;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import ro.utcluj.ecsb.population.EcsbIndividual;
import ro.utcluj.ecsb.utils.StringUtils;

import java.io.IOException;
import java.util.*;

public class EvaluationJobFactory {

    public final static Path OUTPUT_PATH = new Path("output");

    public static Job buildJobAndEvaluate(Iterable<? extends EcsbIndividual> population,
                                          Collection<Double> evaluations,
                                Properties props) throws IOException, ClassNotFoundException, InterruptedException {

        Job job = new Job();
        job.setJarByClass(EvaluationJobFactory.class);

        final SortedMap<Integer, EcsbIndividual> indexedIndividuals = new TreeMap<Integer, EcsbIndividual>();

        int i = 0;
        for (EcsbIndividual individual : population){
            i++;
            indexedIndividuals.put(i, individual);
        }

        configureJob(indexedIndividuals, job, props);

        boolean succeeded = job.waitForCompletion(true);
        if (!succeeded) {
            throw new IllegalStateException("Job failed!");
        }
        importEvaluations(job, evaluations);

        return job;
    }

    private static void configureJob(SortedMap<Integer, EcsbIndividual> population, Job job,
                              Properties props) throws IOException {

        OUTPUT_PATH.getFileSystem(job.getConfiguration()).delete(OUTPUT_PATH, true);

        job.getConfiguration().set("decsb.props", StringUtils.toString(props));

        job.getConfiguration().set("decsb.pop", StringUtils.toString(population));

        job.setInputFormatClass(WholeFileInputFormat.class);

        job.setOutputFormatClass(SequenceFileOutputFormat.class);

        WholeFileInputFormat.addInputPath(job, new Path(props.getProperty("train.splits.dir")));

        FileOutputFormat.setOutputPath(job, OUTPUT_PATH);

        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(DoubleWritable.class);

        job.setMapperClass(SplitEvalMapper.class);
        job.setReducerClass(SplitEvalsReducer.class);


        //conf.set("mapred.input.dir", inpath.toString());
        //conf.set("mapred.output.dir", outpath.toString());

    }


    public static void importEvaluations(Job job, Collection<Double> evaluations) throws IOException {
        FileSystem fs = FileSystem.get(OUTPUT_PATH.toUri(), job.getConfiguration());

        SequenceFile.Sorter sorter = new SequenceFile.Sorter(fs, IntWritable.class, DoubleWritable.class, job.getConfiguration());

        //TODO: find something smarter;
        Path reducerOutput = new Path(OUTPUT_PATH, "part-r-00000");
        Path sortedOutput = new Path(OUTPUT_PATH, "output.sorted");

        sorter.sort(reducerOutput, sortedOutput);

        SequenceFile.Reader reader = new SequenceFile.Reader(fs, sortedOutput, job.getConfiguration());

        IntWritable currentKey = new IntWritable();
        DoubleWritable currentValue = new DoubleWritable();

        while (reader.next(currentKey)){
            reader.getCurrentValue(currentValue);
            evaluations.add(currentValue.get());
        }
    }

}
