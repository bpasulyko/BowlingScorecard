package bpasulyko.bowlingscorecard;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bpasulyko.bowlingscorecard.dbHandlers.MainDbHandler;
import bpasulyko.bowlingscorecard.models.Game;
import bpasulyko.bowlingscorecard.models.ScoreCard;

public class Stats extends AppCompatActivity {

    private MainDbHandler dbHandler;
    private List<Game> allGames;
    private List<Double> allScores;
    private TextView runningAvg;
    TextView bestAvg;
    TextView bestGame;
    TextView worstGame;
    TextView best3GameTotal;
    TextView threeGameAverage;
    TextView totalGames;
    TextView gamesOverAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        dbHandler = new MainDbHandler(this, null, null, 1);
        runningAvg = (TextView) findViewById(R.id.runningAvg);
        bestAvg = (TextView) findViewById(R.id.bestAvg);
        bestGame = (TextView) findViewById(R.id.bestGame);
        worstGame = (TextView) findViewById(R.id.worstGame);
        best3GameTotal = (TextView) findViewById(R.id.best3GameTotal);
        threeGameAverage = (TextView) findViewById(R.id.threeGameAverage);
        totalGames = (TextView) findViewById(R.id.numberOfGames);
        gamesOverAverage = (TextView) findViewById(R.id.gamesOverAvgerage);

        createToolbar();
        createSpinner();
        populateGamesAndScores(null);
    }

    private void populateGamesAndScores(Integer scorecardId) {
        allGames = dbHandler.getAllGames(scorecardId);
        allScores = new ArrayList<>();
        for (Game game : allGames) {
            allScores.addAll(game.getScores());
        }

        String runningAvg = (allGames.size() > 0) ? formatNumber(getRunningAverage()) : "--";
        String bestAvg = (allGames.size() > 0) ? formatNumber(getBestAverage()) : "--";
        String bestGame = (allGames.size() > 0) ? formatNumber(getBestGame()) : "--";
        String worstGame = (allGames.size() > 0) ? formatNumber(getWorstGame()) : "--";
        String best3GameTotal = (allGames.size() > 0) ? formatNumber(getBest3GameTotal()) : "--";
        String threeGameAverage = (allGames.size() > 0) ? formatNumber(get3GameAverage()) : "--";
        String totalGames = (allGames.size() > 0) ? formatNumber((double) allScores.size()) : "--";
        String gamesOverAverage = (allGames.size() > 0) ? String.format("%s%%", formatNumber(getPercentageOfGamesAboveAverage())) : "--";
        this.runningAvg.setText(runningAvg);
        this.bestAvg.setText(bestAvg);
        this.bestGame.setText(bestGame);
        this.worstGame.setText(worstGame);
        this.best3GameTotal.setText(best3GameTotal);
        this.threeGameAverage.setText(threeGameAverage);
        this.totalGames.setText(totalGames);
        this.gamesOverAverage.setText(gamesOverAverage);
    }

    private Double getRunningAverage() {
        return allGames.get(0).getAverage();
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
        List<Double> totals = new ArrayList<>();
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
        myToolbar.setElevation(0);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void createSpinner() {
        ScoreCard allOption = new ScoreCard(null, "All");
        List<ScoreCard> scorecards = new ArrayList<>();
        scorecards.add(allOption);
        scorecards.addAll(dbHandler.getAllScorecards());

        Spinner spinner = (Spinner) findViewById(R.id.scorecardsDropdown);
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, scorecards);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }

    private AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ScoreCard scorecard = (ScoreCard) parent.getItemAtPosition(position);
            populateGamesAndScores(scorecard.getId());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };
}
