package ro.utcluj.ecsb.utils;

public enum EcsbSelectionStrategy {
    RankSelection("RankSelection"),
    TournamentSelection("TournamentSelection"),
    TruncationSelection("TruncationSelection"),
    RouletteWheelSelection("RouletteWheelSelection"),
    StochasticUniversalSampling("StochasticUniversalSampling");

    private final String strategyName;

    EcsbSelectionStrategy(String strategyName) {
        this.strategyName = strategyName;
    }

    public String getStrategyName() {
        return strategyName;
    }
}
