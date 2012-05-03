package ro.utcluj.ecsb.metrics;

public enum Metric {
    BAcc("BAccMetric"),
    //FMeasure("FMeasureMetric"),
    GM("GMMetric");
    //LinTPPrecision("LinTPPrecisionMetric"),
    //LinTPTN("LinTPTNMetric");

    private final String metricName;

    Metric(String metricName) {
        this.metricName = metricName;
    }

    public String getMetricName() {
        return metricName;
    }
}
