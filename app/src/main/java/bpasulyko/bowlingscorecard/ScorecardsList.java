package bpasulyko.bowlingscorecard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import bpasulyko.bowlingscorecard.adapters.ScorecardListAdapter;
import bpasulyko.bowlingscorecard.dbHandlers.MainDbHandler;
import bpasulyko.bowlingscorecard.models.ScoreCard;

public class ScorecardsList extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "bpasulyko.bowlingscorecard.scorecardslist";
    private MainDbHandler dbHandler;
    private List<ScoreCard> scorecards;
    private Boolean deleteMode = false;
    private ListView scorecardsListView;
    private EditText input;
    private AlertDialog addScorecardDialog;
    private TextView noScorecardsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorecards_list);
        createToolbar();
        dbHandler = new MainDbHandler(this, null, null, 1);

        noScorecardsText = (TextView) findViewById(R.id.noScorecards);
        scorecardsListView = (ListView) findViewById(R.id.scorecardsList);
        scorecardsListView.setOnItemClickListener(scorecardListClickListener);
        populateScorecardsList();

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

    private void populateScorecardsList() {
        scorecards = dbHandler.getAllScorecards();
        if (scorecards.size() == 0) {
            scorecardsListView.setVisibility(View.INVISIBLE);
            noScorecardsText.setVisibility(View.VISIBLE);
            deleteMode = false;
            invalidateOptionsMenu();
        } else {
            scorecardsListView.setVisibility(View.VISIBLE);
            noScorecardsText.setVisibility(View.INVISIBLE);
            scorecardsListView.setAdapter(new ScorecardListAdapter(this, scorecards, deleteMode));
        }
    }

    private void createNewScorecardDialog() {
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.add_scorecard, null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        dialog.setView(view);
        dialog.setTitle("New Scorecard");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Save", saveScorecard);
        dialog.setNegativeButton("Cancel", cancel);
        input = (EditText) view.findViewById(R.id.scorecard_name);
        addScorecardDialog = dialog.create();
    }

    private AdapterView.OnItemClickListener scorecardListClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            final ScoreCard scoreCard = (ScoreCard) parent.getItemAtPosition(position);
            if(deleteMode) {
                generateDeleteConfirmationDialog(scoreCard);
            } else {
                Intent intent = new Intent(ScorecardsList.this, GamesList.class);
                intent.putExtra(EXTRA_MESSAGE, scoreCard);
                startActivity(intent);
            }
        }
    };

    private void generateDeleteConfirmationDialog(final ScoreCard scorecard) {
        DialogInterface.OnClickListener deleteGame = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                boolean scorecardDeleted = dbHandler.deleteSelectedScorecard(scorecard);
                String message = (scorecardDeleted) ? "Scorecard deleted!" : "Error deleting scorecard";
                populateScorecardsList();
                Snackbar.make(ScorecardsList.this.findViewById(R.id.activity_scorecards_list), message, Snackbar.LENGTH_SHORT).show();
            }
        };
        DialogInterface.OnClickListener cancel = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        };

        AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        confirmationDialog.setMessage("Delete " + scorecard.getName() + "?");
        confirmationDialog.setCancelable(true);
        confirmationDialog.setPositiveButton("Yes", deleteGame);
        confirmationDialog.setNegativeButton("No", cancel);
        AlertDialog confirmationAlert = confirmationDialog.create();
        confirmationAlert.show();
    }

    public void addScorecard(View view) {
        input.setText("");
        addScorecardDialog.show();
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
                    populateScorecardsList();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_bar_games, menu);
        int deleteIcon = (deleteMode) ? R.drawable.ic_clear : R.drawable.ic_delete;
        menu.findItem(R.id.action_delete).setIcon(deleteIcon);
        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.add_scorecard_button);
        int visibility = (deleteMode) ? View.INVISIBLE : View.VISIBLE;
        addButton.setVisibility(visibility);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (scorecards.size() > 0) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    deleteMode = !deleteMode;
                    scorecardsListView.setAdapter(new ScorecardListAdapter(this, scorecards, deleteMode));
                    invalidateOptionsMenu();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
