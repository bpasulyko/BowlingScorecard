package bpasulyko.bowlingscorecard.models.domain;

import java.util.ArrayList;
import java.util.List;

import bpasulyko.bowlingscorecard.models.Game;

public class Statistics {
    private List<Game> games;
    private List<Double> scores;
    
    public Statistics(List<Game> games) {
        this.games = games;
        this.scores = new ArrayList<>();
        for (Game game : games) {
            scores.addAll(game.getScores());
        }
    }

    public Double getRunningAverage() {
        Double total = 0d;
        for (Double score : scores) {
            total += score;
        }
        return total/scores.size();
    }

    public Double getBestAverage() {
        Double bestAvg = Double.MIN_VALUE;
        for (Game game : games) {
            if (game.getAverage() > bestAvg) {
                bestAvg = game.getAverage();
            }
        }
        return bestAvg;
    }

    public Double getBestGame() {
        Double bestGame = Double.MIN_VALUE;
        for (Double score : scores) {
            if (score > bestGame) {
                bestGame = score;
            }
        }
        return bestGame;
    }

    public Double getWorstGame() {
        Double worstGame = Double.MAX_VALUE;
        for (Double score : scores) {
            if (score < worstGame) {
                worstGame = score;
            }
        }
        return worstGame;
    }

    public Double getBest3GameTotal() {
        Double total = Double.MIN_VALUE;
        for (Game game : games) {
            if (game.getTotal() > total) {
                total = game.getTotal();
            }
        }
        return total;
    }

    public Double get3GameAverage() {
        List<Double> totals = new ArrayList<>();
        for (Game game : games) {
            totals.add(game.getTotal());
        }

        Double total = 0d;
        for (Double score : totals) {
            total += score;
        }
        return total / games.size();
    }

    public Double getPercentageOfGamesAboveAverage() {
        Double gamesOverAvg = 0d;
        Double runningAvg = getRunningAverage();
        for (Double score : scores) {
            if (score > runningAvg) {
                gamesOverAvg++;
            }
        }
        return (gamesOverAvg / scores.size())* 100d;
    }

    public Double getTotalGames() {
        return (double) scores.size();
    }
}
