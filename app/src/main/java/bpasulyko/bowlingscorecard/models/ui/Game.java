package bpasulyko.bowlingscorecard.models.ui;

import android.support.annotation.NonNull;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Game {
    private Integer id;
    private Long date;
    private List<Double> scores;
    private Double total;
    private Double average;

    public Game(Long date, List<Double> scores, Double total, Double average) {
        this.date = date;
        this.scores = scores;
        this.total = total;
        this.average = average;
    }

    public Game(Integer id, Long date, List<Double> scores, Double total, Double average) {
        this.id = id;
        this.date = date;
        this.scores = scores;
        this.total = total;
        this.average = average;
    }

    public Integer getId() {
        return id;
    }

    public Long getGameDate() {
        return date;
    }

    public List<Double> getScores() {
        return scores;
    }

    public void setScores(List<Double> scores) {
        this.scores = scores;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public String getFormattedDateString() {
        final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
        return formatter.format(new Date(date));
    }

    @Override
    public String toString() {
        DecimalFormat decimalFormat = getDecimalFormat();
        String scoreString = "";
        for (Double score : scores) {
            scoreString += String.format(" %s ", decimalFormat.format(score));
        }
        return getFormattedDateString() + ":  " + scoreString + " -- " + decimalFormat.format(average);
    }

    public String getScoresString() {
        DecimalFormat decimalFormat = getDecimalFormat();
        List<Double> scores = getScores();
        String firstGame = decimalFormat.format(scores.get(0));
        String secondGame = decimalFormat.format(scores.get(1));
        String thirdGame = decimalFormat.format(scores.get(2));
        return String.format("%s | %s | %s", firstGame, secondGame, thirdGame);
    }

    @NonNull
    public static DecimalFormat getDecimalFormat() {
        return new DecimalFormat("#");
    }
}
