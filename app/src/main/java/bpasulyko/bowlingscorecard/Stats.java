package bpasulyko.bowlingscorecard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bpasulyko.bowlingscorecard.dbHandlers.MainDbHandler;
import bpasulyko.bowlingscorecard.models.Game;

public class Stats extends AppCompatActivity {

    private MainDbHandler dbHandler;
    private List<Game> allGames;
    List<Double> allScores = new ArrayList<>();
    private TextView runningAvg;
    private TextView bestAvg;
    private TextView bestGame;
    private TextView worstGame;
    private TextView best3GameTotal;
    private TextView threeGameAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        createToolbar();
        dbHandler = new MainDbHandler(this, null, null, 1);
        allGames = dbHandler.getAllGames();
        for (Game game : allGames) {
            allScores.addAll(game.getScores());
        }

        runningAvg = (TextView) findViewById(R.id.runningAvg);
        bestAvg = (TextView) findViewById(R.id.bestAvg);
        bestGame = (TextView) findViewById(R.id.bestGame);
        worstGame = (TextView) findViewById(R.id.worstGame);
        best3GameTotal = (TextView) findViewById(R.id.best3GameTotal);
        threeGameAverage = (TextView) findViewById(R.id.threeGameAverage);

        runningAvg.setText(getRunningAverage());
        bestAvg.setText(getBestAverage());
        bestGame.setText(getBestGame());
        worstGame.setText(getWorstGame());
        best3GameTotal.setText(getBest3GameTotal());
        threeGameAverage.setText(get3GameAverage());
    }

    private String getRunningAverage() {
        return Game.getDecimalFormat().format(allGames.get(allGames.size() - 1).getAverage());
    }

    private String getBestAverage() {
        Double bestAvg = Double.MIN_VALUE;
        for (Game game : allGames) {
            if (game.getAverage() > bestAvg) {
                bestAvg = game.getAverage();
            }
        }
        return Game.getDecimalFormat().format(bestAvg);
    }

    private String getBestGame() {
        Double bestGame = Double.MIN_VALUE;
        for (Double score : allScores) {
            if (score > bestGame) {
                bestGame = score;
            }
        }
        return Game.getDecimalFormat().format(bestGame);
    }

    private String getWorstGame() {
        Double worstGame = Double.MAX_VALUE;
        for (Double score : allScores) {
            if (score < worstGame) {
                worstGame = score;
            }
        }
        return Game.getDecimalFormat().format(worstGame);
    }

    private String getBest3GameTotal() {
        Double total = Double.MIN_VALUE;
        for (Game game : allGames) {
            if (game.getTotal() > total) {
                total = game.getTotal();
            }
        }
        return Game.getDecimalFormat().format(total);
    }

    private String get3GameAverage() {
        List<Double> totals = new ArrayList<>();;
        for (Game game : allGames) {
            totals.add(game.getTotal());
        }

        Double total = 0d;
        for (Double score : totals) {
            total += score;
        }
        return Game.getDecimalFormat().format(total / allGames.size());
    }

    private void createToolbar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.statistics);
        setSupportActionBar(myToolbar);
    }

    public void viewAllGames(View view) {
        Intent intent = new Intent(this, GamesList.class);
        startActivity(intent);
    }
}
