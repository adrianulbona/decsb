package ro.utcluj.ecsb.preprocess;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import weka.core.Instances;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;

public class ArffSpliter {

    private final Instances instances;
    private final Path splitsDir;
    private final int numberOfSplits;

    public ArffSpliter(Instances instances, Path splitsDir, int numberOfSplits) {
        this.instances = instances;
        this.splitsDir = splitsDir;
        this.numberOfSplits = numberOfSplits;
    }

    public void split() throws IOException {
        FileSystem fs = FileSystem.get(new Configuration());

        fs.delete(splitsDir, true);

        instances.randomize(new Random(1));
        instances.stratify(numberOfSplits);

        for (int i = 0; i < numberOfSplits; i++){
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(fs.create(new Path(splitsDir, "train_split_" + i + ".arff"))));
            bw.write(instances.testCV(numberOfSplits, i).toString());
            bw.close();
        }
    }

}
