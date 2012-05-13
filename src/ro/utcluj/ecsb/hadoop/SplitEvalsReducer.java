package ro.utcluj.ecsb.hadoop;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.log4j.Logger;
import weka.associations.tertius.IndividualInstance;

import java.io.IOException;

public class SplitEvalsReducer extends Reducer<IntWritable, DoubleWritable, IntWritable, DoubleWritable> {

    @Override
    public void reduce(IntWritable key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
        double sum = 0.0;
        int size = 0;
        for (DoubleWritable value : values) {
            sum += value.get();
            size++;
        }
        Logger.getLogger(SplitEvalsReducer.class).info("Individual " + key + " --> agg: " + sum/size);
        context.write(key, new DoubleWritable(sum / size));
    }
}

