package bpasulyko.bowlingscorecard.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Game {
    private Integer id;
    private Date gameDate;
    private List<Integer> scores;
    private Integer total;
    private Integer average;

    public Game(Date gameDate, List<Integer> scores, Integer total, Integer average) {
        this.gameDate = gameDate;
        this.scores = scores;
        this.total = total;
        this.average = average;
    }

    public Game(Integer id, Date gameDate, List<Integer> scores, Integer total, Integer average) {
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

    public List<Integer> getScores() {
        return scores;
    }

    public void setScores(List<Integer> scores) {
        this.scores = scores;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getAverage() {
        return average;
    }

    public void setAverage(Integer average) {
        this.average = average;
    }

    @Override
    public String toString() {
        final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
        return formatter.format(gameDate) + ":  " + scores.toString() + " -- " + average;
    }
}
