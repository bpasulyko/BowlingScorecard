package bpasulyko.bowlingscorecard;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import bpasulyko.bowlingscorecard.dbHandlers.MainDbHandler;
import bpasulyko.bowlingscorecard.models.ScoreCard;
import bpasulyko.bowlingscorecard.models.ui.Game;
import bpasulyko.bowlingscorecard.models.ui.StatisticsViewModel;

public class Stats extends AppCompatActivity {

    private MainDbHandler dbHandler;
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
        List<Game> allGames = dbHandler.getAllGames(scorecardId);
        this.runningAvg.setText(new StatisticsViewModel(allGames).getRunningAverage());
        this.bestAvg.setText(new StatisticsViewModel(allGames).getBestAverage());
        this.bestGame.setText(new StatisticsViewModel(allGames).getBestGame());
        this.worstGame.setText(new StatisticsViewModel(allGames).getWorstGame());
        this.best3GameTotal.setText(new StatisticsViewModel(allGames).getBest3GameTotal());
        this.threeGameAverage.setText(new StatisticsViewModel(allGames).get3GameAverage());
        this.totalGames.setText(new StatisticsViewModel(allGames).getTotalGames());
        this.gamesOverAverage.setText(new StatisticsViewModel(allGames).getPercentageOfGamesAboveAverage());
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
