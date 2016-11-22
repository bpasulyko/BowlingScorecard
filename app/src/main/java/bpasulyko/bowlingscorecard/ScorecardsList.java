package bpasulyko.bowlingscorecard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import bpasulyko.bowlingscorecard.dbHandlers.MainDbHandler;
import bpasulyko.bowlingscorecard.models.ScoreCard;

public class ScorecardsList extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "bpasulyko.bowlingscorecard.scorecardslist";
    private MainDbHandler dbHandler;
    private List<ScoreCard> scorecards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorecards_list);
        createToolbar();

        dbHandler = new MainDbHandler(this, null, null, 1);
        scorecards = dbHandler.getAllScorecards();

        ListView lv = (ListView) findViewById(R.id.scorecardsList);
        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, scorecards));
        lv.setOnItemClickListener(itemClickListener);
    }

    private void createToolbar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.scorecards);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            final ScoreCard scoreCard = (ScoreCard) parent.getItemAtPosition(position);
            Intent intent = new Intent(ScorecardsList.this, GamesList.class);
            intent.putExtra(EXTRA_MESSAGE, scoreCard);
            startActivity(intent);
        }
    };
}
