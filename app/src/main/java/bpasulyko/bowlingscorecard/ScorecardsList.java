package bpasulyko.bowlingscorecard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import bpasulyko.bowlingscorecard.dbHandlers.MainDbHandler;
import bpasulyko.bowlingscorecard.models.ScoreCard;

public class ScorecardsList extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "bpasulyko.bowlingscorecard.scorecardslist";
    private MainDbHandler dbHandler;
    private List<ScoreCard> scorecards;
    private ListView scorecardsListView;
    private EditText input;
    private AlertDialog addScorecardInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorecards_list);
        createToolbar();

        dbHandler = new MainDbHandler(this, null, null, 1);
        scorecards = dbHandler.getAllScorecards();

        scorecardsListView = (ListView) findViewById(R.id.scorecardsList);
        scorecardsListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, scorecards));
        scorecardsListView.setOnItemClickListener(itemClickListener);
        input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        createNewScorecardDialog();
    }

    private void createToolbar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.scorecards);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void createNewScorecardDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        input.setTextColor(getResources().getColor(R.color.background));
        dialog.setMessage("New Scorecard:");
        dialog.setCancelable(true);
        dialog.setView(input);
        dialog.setPositiveButton("Save", saveScorecard);
        dialog.setNegativeButton("Cancel", cancel);
        addScorecardInput = dialog.create();
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

    public void addScorecard(View view) {
        input.setText("");
        addScorecardInput.show();
    }

    private DialogInterface.OnClickListener saveScorecard = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            String scorecardName = input.getText().toString();
            if (scorecardName.equals("")) {
                Snackbar.make(ScorecardsList.this.findViewById(R.id.activity_scorecards_list), "Enter a name for this scorecard!", Snackbar.LENGTH_SHORT).show();
            } else {
                boolean validScorecardName = dbHandler.isValidScorecardName(scorecardName);
                if (validScorecardName) {
                    dialog.cancel();
                    boolean scorecardAdded = dbHandler.addNewScorecard(scorecardName);
                    String message = (scorecardAdded) ? scorecardName + " saved!" : "An error occurred!";
                    scorecards = dbHandler.getAllScorecards();
                    scorecardsListView.setAdapter(new ArrayAdapter<>(ScorecardsList.this, android.R.layout.simple_list_item_1, scorecards));
                    Snackbar.make(ScorecardsList.this.findViewById(R.id.activity_scorecards_list), message, Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(ScorecardsList.this.findViewById(R.id.activity_scorecards_list), "Name is already in use", Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    };

    private DialogInterface.OnClickListener cancel = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    };
}
