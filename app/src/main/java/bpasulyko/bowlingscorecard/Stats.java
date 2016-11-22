package bpasulyko.bowlingscorecard;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bpasulyko.bowlingscorecard.dbHandlers.MainDbHandler;
import bpasulyko.bowlingscorecard.models.Game;

public class Stats extends AppCompatActivity {

    private List<Game> allGames;
    List<Double> allScores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        createToolbar();
        MainDbHandler dbHandler = new MainDbHandler(this, null, null, 1);
        allGames = dbHandler.getAllGames(null);
        for (Game game : allGames) {
            allScores.addAll(game.getScores());
        }

        TextView runningAvg = (TextView) findViewById(R.id.runningAvg);
        TextView bestAvg = (TextView) findViewById(R.id.bestAvg);
        TextView bestGame = (TextView) findViewById(R.id.bestGame);
        TextView worstGame = (TextView) findViewById(R.id.worstGame);
        TextView best3GameTotal = (TextView) findViewById(R.id.best3GameTotal);
        TextView threeGameAverage = (TextView) findViewById(R.id.threeGameAverage);
        TextView totalGames = (TextView) findViewById(R.id.numberOfGames);
        TextView gamesOverAverage = (TextView) findViewById(R.id.gamesOverAvgerage);

        runningAvg.setText(formatNumber(getRunningAverage()));
        bestAvg.setText(formatNumber(getBestAverage()));
        bestGame.setText(formatNumber(getBestGame()));
        worstGame.setText(formatNumber(getWorstGame()));
        best3GameTotal.setText(formatNumber(getBest3GameTotal()));
        threeGameAverage.setText(formatNumber(get3GameAverage()));
        totalGames.setText(formatNumber((double) allScores.size()));
        gamesOverAverage.setText(String.format("%s%%", formatNumber(getPercentageOfGamesAboveAverage())));
    }

    private Double getRunningAverage() {
        return allGames.get(allGames.size() - 1).getAverage();
    }

    private Double getBestAverage() {
        Double bestAvg = Double.MIN_VALUE;
        for (Game game : allGames) {
            if (game.getAverage() > bestAvg) {
                bestAvg = game.getAverage();
            }
        }
        return bestAvg;
    }

    private Double getBestGame() {
        Double bestGame = Double.MIN_VALUE;
        for (Double score : allScores) {
            if (score > bestGame) {
                bestGame = score;
            }
        }
        return bestGame;
    }

    private Double getWorstGame() {
        Double worstGame = Double.MAX_VALUE;
        for (Double score : allScores) {
            if (score < worstGame) {
                worstGame = score;
            }
        }
        return worstGame;
    }

    private Double getBest3GameTotal() {
        Double total = Double.MIN_VALUE;
        for (Game game : allGames) {
            if (game.getTotal() > total) {
                total = game.getTotal();
            }
        }
        return total;
    }

    private Double get3GameAverage() {
        List<Double> totals = new ArrayList<>();;
        for (Game game : allGames) {
            totals.add(game.getTotal());
        }

        Double total = 0d;
        for (Double score : totals) {
            total += score;
        }
        return total / allGames.size();
    }

    private Double getPercentageOfGamesAboveAverage() {
        Double gamesOverAvg = 0d;
        Double runningAvg = getRunningAverage();
        for (Double score : allScores) {
            if (score > runningAvg) {
                gamesOverAvg++;
            }
        }
        return (gamesOverAvg / allScores.size())* 100d;
    }

    private String formatNumber(Double number) {
        return Game.getDecimalFormat().format(number);
    }

    private void createToolbar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.statistics);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }
}
