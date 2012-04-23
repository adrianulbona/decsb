package utcluj.ecsb.watchmaker.preprocessing;

public enum Classifier {
    IBk("weka.classifiers.lazy.IBk"),
    J48("weka.classifiers.trees.J48"),
    NaiveBayes("weka.classifiers.bayes.NaiveBayes"),
    AdaBoostM1("weka.classifiers.meta.AdaBoostM1");
    //SMO("weka.classifiers.functions.SMO");

    private final String className;
    Classifier(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}
