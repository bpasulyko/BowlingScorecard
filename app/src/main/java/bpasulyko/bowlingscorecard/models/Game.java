package bpasulyko.bowlingscorecard.models;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Game {
    private Integer id;
    private Date gameDate;
    private List<Double> scores;
    private Double total;
    private Double average;

    public Game(Date gameDate, List<Double> scores, Double total, Double average) {
        this.gameDate = gameDate;
        this.scores = scores;
        this.total = total;
        this.average = average;
    }

    public Game(Integer id, Date gameDate, List<Double> scores, Double total, Double average) {
        this.id = id;
        this.gameDate = gameDate;
        this.scores = scores;
        this.total = total;
        this.average = average;
    }

    public Integer getId() {
        return id;
    }

    public Date getGameDate() {
        return gameDate;
    }

    public void setGameDate(Date gameDate) {
        this.gameDate = gameDate;
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

    @Override
    public String toString() {
        final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
        DecimalFormat decimalFormat = new DecimalFormat("#");
        String scoreString = "";
        for (Double score : scores) {
            scoreString += String.format(" %s ", decimalFormat.format(score));
        }
        return formatter.format(gameDate) + ":  " + scoreString + " -- " + decimalFormat.format(average);
    }
}
