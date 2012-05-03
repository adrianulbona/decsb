package ro.utcluj.ecsb.evaluation;

import org.apache.log4j.Logger;
import weka.classifiers.Classifier;
import weka.core.Instances;

public abstract class EcsbEvaluator {

    protected Instances trainSet;
    protected Classifier costClassifier;
    protected String baseClassifierName;

    protected Classifier getBaseClassifier(String baseClassifierName, double c, double e) {
        Classifier baseClassifier = null;
        try {

            Class<?> cls = Class.forName(baseClassifierName);
            baseClassifier = (Classifier) cls.newInstance();

            if (baseClassifier.getClass().toString().contains("IBk")) {
                baseClassifier.setOptions(weka.core.Utils.splitOptions("-K " + (int) (c / 12.8 + 1)));
            } else if (baseClassifier.getClass().toString().contains("J48")) {
                baseClassifier.setOptions(weka.core.Utils.splitOptions("-C " + (c / 320) + " -M " + (int) (e / 30 + 1))); //M sa fie sub 5, ca de nu da erori, ci C sub 0.4
            } else if (baseClassifier.getClass().toString().contains("SMO")) {
                baseClassifier.setOptions(weka.core.Utils.splitOptions("-C " + (c / 12.8 + 1) + " -K \"weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E " + (int) (e / 12.8 + 1) + "\""));
            } else if (baseClassifier.getClass().toString().contains("Multilayer")) {
                baseClassifier.setOptions(weka.core.Utils.splitOptions("-L " + c / 128 + " -M " + e / 128));
            } else if (baseClassifier.getClass().toString().contains("Ada")) {
                baseClassifier.setOptions(weka.core.Utils.splitOptions("-P " + (int) c + " -I " + (int) (e / 4 + 1)));
            }
        } catch (Exception ex) {
            Logger.getLogger("DECSBLog").error("Unable to to create base classifier.");
        }
        return baseClassifier;
    }
}
