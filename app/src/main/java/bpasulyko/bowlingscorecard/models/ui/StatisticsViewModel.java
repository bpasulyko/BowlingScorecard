package bpasulyko.bowlingscorecard.models.ui;

import java.util.List;

import bpasulyko.bowlingscorecard.models.Game;
import bpasulyko.bowlingscorecard.models.domain.Statistics;

public class StatisticsViewModel {
    private final Statistics statistics;
    private final List<Game> games;

    public StatisticsViewModel(List<Game> games) {
        this.games = games;
        this.statistics = new Statistics(games);
    }

    public String getRunningAverage() {
        return (games.size() > 0) ? formatNumber(statistics.getRunningAverage()) : "--";
    }

    public String getBestAverage() {
        return (games.size() > 0) ? formatNumber(statistics.getBestAverage()) : "--";
    }

    public String getBestGame() {
        return (games.size() > 0) ? formatNumber(statistics.getBestGame()) : "--";
    }

    public String getWorstGame() {
        return (games.size() > 0) ? formatNumber(statistics.getWorstGame()) : "--";
    }

    public String getBest3GameTotal() {
        return (games.size() > 0) ? formatNumber(statistics.getBest3GameTotal()) : "--";
    }

    public String get3GameAverage() {
        return (games.size() > 0) ? formatNumber(statistics.get3GameAverage()) : "--";
    }

    public String getPercentageOfGamesAboveAverage() {
        return (games.size() > 0) ? String.format("%s%%", formatNumber(statistics.getPercentageOfGamesAboveAverage())) : "--";
    }

    public String getTotalGames() {
        return (games.size() > 0) ? formatNumber((statistics.getTotalGames())) : "--";
    }

    private String formatNumber(Double number) {
        return Game.getDecimalFormat().format(number);
    }
}
