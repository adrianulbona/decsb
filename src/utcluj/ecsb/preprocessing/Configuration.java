package utcluj.ecsb.preprocessing;


/**
 * Created by IntelliJ IDEA.
 * User: adibo
 * Date: 12.12.2011
 * Time: 16:05
 * To change this template use File | Settings | File Templates.
 */
public class Configuration {
    private String datasetPath;
    private String[] baseClassifiersNames;
    private int baseClassifierUsed;
    private int numFolds;
    private String[] costClassifiersNames;
    private int costClassifierUsed;
    private String[] FitnessMetricsNames;
    private int FitnessMetricUsed;
    private double beta;

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public String[] getFitnessMetricsNames() {
        return FitnessMetricsNames;
    }

    public void setFitnessMetricsNames(String[] fitnessMetricsNames) {
        FitnessMetricsNames = fitnessMetricsNames;
    }

    public int getFitnessMetricUsed() {
        return FitnessMetricUsed;
    }

    public void setFitnessMetricUsed(int fitnessMetricUsed) {
        FitnessMetricUsed = fitnessMetricUsed;
    }

    private boolean useReweight;

    public String getDatasetPath() {
        return datasetPath;
    }

    public void setDatasetPath(String datasetPath) {
        this.datasetPath = datasetPath;
    }

    public String[] getBaseClassifiersNames() {
        return baseClassifiersNames;
    }

    public void setBaseClassifiersNames(String[] baseClassifiersNames) {
        this.baseClassifiersNames = baseClassifiersNames;
    }

    public int getBaseClassifierUsed() {
        return baseClassifierUsed;
    }

    public void setBaseClassifierUsed(int baseClassifierUsed) {
        this.baseClassifierUsed = baseClassifierUsed;
    }

    public int getNumFolds() {
        return numFolds;
    }

    public void setNumFolds(int numFolds) {
        this.numFolds = numFolds;
    }

    public String[] getCostClassifiersNames() {
        return costClassifiersNames;
    }

    public void setCostClassifiersNames(String[] costClassifiersNames) {
        this.costClassifiersNames = costClassifiersNames;
    }

    public int getCostClassifierUsed() {
        return costClassifierUsed;
    }

    public void setCostClassifierUsed(int costClassifierUsed) {
        this.costClassifierUsed = costClassifierUsed;
    }

    public boolean isUseReweight() {
        return useReweight;
    }

    public void setUseReweight(boolean useReweight) {
        this.useReweight = useReweight;
    }
}
