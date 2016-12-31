package bpasulyko.bowlingscorecard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import bpasulyko.bowlingscorecard.adapters.GameListAdapter;
import bpasulyko.bowlingscorecard.dbHandlers.MainDbHandler;
import bpasulyko.bowlingscorecard.models.ScoreCard;
import bpasulyko.bowlingscorecard.models.ui.Game;

public class GamesList extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "bradyp.bowlingscorecard.gameslist";

    private MainDbHandler dbHandler;
    private ListView gamesListView;
    private List<Game> games;
    private Boolean deleteMode = false;
    private ScoreCard scorecard = null;
    private TextView noGamesText;
    private EditText firstGameInput;
    private EditText secondGameInput;
    private EditText thirdGameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_list);
        dbHandler = new MainDbHandler(this, null, null, 1);
        Intent intent = getIntent();
        ScoreCard savedScorecard = (ScoreCard) intent.getSerializableExtra(AddScores.EXTRA_MESSAGE);
        noGamesText = (TextView) findViewById(R.id.noGames);
        gamesListView = (ListView) findViewById(R.id.gamesList);
        gamesListView.setOnItemClickListener(gameClickListener);
        if (savedScorecard != null) {
            scorecard = savedScorecard;
            Snackbar.make(this.findViewById(R.id.activity_games_list), "Game saved!", Snackbar.LENGTH_SHORT).show();
        } else {
            scorecard = (ScoreCard) intent.getSerializableExtra(ScorecardsList.EXTRA_MESSAGE);
        }
        createToolbar();
        populateGamesList();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        populateGamesList();
    }

    private void createToolbar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(scorecard.getName());
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void populateGamesList() {
        if (scorecard != null) {
            games = dbHandler.getAllGames(scorecard.getId());
            if (games.size() == 0) {
                gamesListView.setVisibility(View.INVISIBLE);
                noGamesText.setVisibility(View.VISIBLE);
                deleteMode = false;
                invalidateOptionsMenu();
            } else {
                gamesListView.setVisibility(View.VISIBLE);
                noGamesText.setVisibility(View.INVISIBLE);
                gamesListView.setAdapter(new GameListAdapter(this, games, deleteMode));
            }
        }
    }

    private AdapterView.OnItemClickListener gameClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            final Game game = (Game) parent.getItemAtPosition(position);
            if (deleteMode) {
                generateDeleteConfirmationDialog(game);
            } else if (!game.isFullSeries()) {
                generateEditGameDialog(game);
            }
        }
    };

    private void generateEditGameDialog(final Game game) {
        final View editGamesView = getEditGamesView(game);
        DialogInterface.OnClickListener updateGame = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String firstGame = firstGameInput.getText().toString();
                String secondGame = secondGameInput.getText().toString();
                String thirdGame = thirdGameInput.getText().toString();

                dialog.cancel();
                game.setFirstGame((!firstGame.isEmpty()) ? Double.parseDouble(firstGame) : null);
                game.setSecondGame((!secondGame.isEmpty()) ? Double.parseDouble(secondGame) : null);
                game.setThirdGame((!thirdGame.isEmpty()) ? Double.parseDouble(thirdGame) : null);
                game.setTotal();
                boolean gameSaved = dbHandler.updateGame(scorecard.getId(), game);
                String message = (gameSaved) ? "Game updated!" : "Error saving game!";
                populateGamesList();
                Snackbar.make(GamesList.this.findViewById(R.id.activity_games_list), message, Snackbar.LENGTH_SHORT).show();
            }
        };

        AlertDialog.Builder dialog = new AlertDialog.Builder(GamesList.this, R.style.AlertDialogCustom);
        dialog.setView(editGamesView);
        dialog.setTitle(game.getFormattedDateString());
        dialog.setCancelable(true);
        dialog.setPositiveButton("Save", updateGame);
        dialog.setNegativeButton("Cancel", cancel);
        dialog.create();
        dialog.show();
    }

    @NonNull
    private View getEditGamesView(Game game) {
        LayoutInflater inflater = GamesList.this.getLayoutInflater();
        View editGamesView = inflater.inflate(R.layout.edit_games, null);
        firstGameInput = (EditText) editGamesView.findViewById(R.id.firstGame);
        secondGameInput = (EditText) editGamesView.findViewById(R.id.secondGame);
        thirdGameInput = (EditText) editGamesView.findViewById(R.id.thirdGame);

        firstGameInput.setText((game.getFirstGame() != null && game.getFirstGame() > 0) ? game.getFirstGame().toString() : "");
        secondGameInput.setText((game.getSecondGame() != null && game.getSecondGame() > 0) ? game.getSecondGame().toString() : "");
        thirdGameInput.setText((game.getThirdGame() != null && game.getThirdGame() > 0) ? game.getThirdGame().toString() : "");
        return editGamesView;
    }

    private void generateDeleteConfirmationDialog(final Game game) {
        DialogInterface.OnClickListener deleteGame = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                boolean gameDeleted = dbHandler.deleteSelectedGame(game);
                String message = (gameDeleted) ? "Game deleted!" : "Error deleting game!";
                populateGamesList();
                Snackbar.make(GamesList.this.findViewById(R.id.activity_games_list), message, Snackbar.LENGTH_SHORT).show();
            }
        };

        AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        confirmationDialog.setMessage("Delete " + game.getFormattedDateString() + " game?");
        confirmationDialog.setCancelable(true);
        confirmationDialog.setPositiveButton("Yes", deleteGame);
        confirmationDialog.setNegativeButton("No", cancel);
        confirmationDialog.create();
        confirmationDialog.show();
    }

    private DialogInterface.OnClickListener cancel = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    };

    public void addScores(View view) {
        Intent intent = new Intent(this, AddScores.class);
        intent.putExtra(EXTRA_MESSAGE, scorecard);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_bar_games, menu);
        int deleteIcon = (deleteMode) ? R.drawable.ic_clear : R.drawable.ic_delete;
        menu.findItem(R.id.action_delete).setIcon(deleteIcon);
        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.add_scores_button);
        int visibility = (deleteMode) ? View.INVISIBLE : View.VISIBLE;
        addButton.setVisibility(visibility);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (games.size() > 0) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    deleteMode = !deleteMode;
                    gamesListView.setAdapter(new GameListAdapter(this, games, deleteMode));
                    invalidateOptionsMenu();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
