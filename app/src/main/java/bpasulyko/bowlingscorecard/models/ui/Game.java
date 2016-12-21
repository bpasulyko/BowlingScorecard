package bpasulyko.bowlingscorecard.models.ui;

import android.support.annotation.NonNull;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Game {
    private Integer id;
    private Long date;
    private List<Double> scores;
    private Double firstGame;
    private Double secondGame;
    private Double thirdGame;
    private Double total;
    private Double average;

    public Game(Long date, Double firstGame, Double secondGame, Double thirdGame) {
        this.date = date;
        this.firstGame = firstGame;
        this.secondGame = secondGame;
        this.thirdGame = thirdGame;
        setScores();
        setTotal();
    }

    public Game(Integer id, Long date, Double firstGame, Double secondGame, Double thirdGame, Double total, Double average) {
        this.id = id;
        this.date = date;
        this.firstGame = firstGame;
        this.secondGame = secondGame;
        this.thirdGame = thirdGame;
        this.total = total;
        this.average = average;
        setScores();
    }

    public Integer getId() {
        return id;
    }

    public Long getGameDate() {
        return date;
    }

    public Double getFirstGame() {
        return firstGame;
    }

    public Double getSecondGame() {
        return secondGame;
    }

    public Double getThirdGame() {
        return thirdGame;
    }

    public List<Double> getScores() {
        return scores;
    }

    private void setScores() {
        scores = new ArrayList<>();
        if (firstGame != null) scores.add(firstGame);
        if (secondGame != null) scores.add(secondGame);
        if (thirdGame != null) scores.add(thirdGame);
    }

    public Double getTotal() {
        return total;
    }

    private void setTotal() {
        total = 0d;
        if (firstGame != null) total += firstGame;
        if (secondGame != null) total += secondGame;
        if (thirdGame != null) total += thirdGame;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public boolean isFullSeries() {
        return firstGame > 0 && secondGame > 0 && thirdGame > 0;
    }

    public String getFormattedDateString() {
        final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
        return formatter.format(new Date(date));
    }

    public String getScoresString() {
        DecimalFormat decimalFormat = getDecimalFormat();
        String firstGame = (this.firstGame != null && this.firstGame > 0) ? decimalFormat.format(this.firstGame) : "--";
        String secondGame = (this.secondGame != null && this.secondGame > 0) ? decimalFormat.format(this.secondGame) : "--";
        String thirdGame = (this.thirdGame != null && this.thirdGame > 0) ? decimalFormat.format(this.thirdGame) : "--";
        return String.format("%s | %s | %s", firstGame, secondGame, thirdGame);
    }

    @NonNull
    public static DecimalFormat getDecimalFormat() {
        return new DecimalFormat("#");
    }
}