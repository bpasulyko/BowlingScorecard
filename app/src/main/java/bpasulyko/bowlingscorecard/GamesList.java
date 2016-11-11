package bpasulyko.bowlingscorecard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import bpasulyko.bowlingscorecard.dbHandlers.MainDbHandler;
import bpasulyko.bowlingscorecard.models.Game;

public class GamesList extends AppCompatActivity {

    private MainDbHandler dbHandler;
    private ListView gamesListView;
    private List<Game> games;
    private Boolean deleteMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_list);
        createToolbar();

        dbHandler = new MainDbHandler(this, null, null, 1);
        games = dbHandler.getAllGames();

        gamesListView = (ListView) findViewById(R.id.gamesList);
        gamesListView.setAdapter(new GameListAdapter(this, games, deleteMode));
        gamesListView.setOnItemClickListener(itemClickListener);

        Intent intent = getIntent();
        String message = intent.getStringExtra(AddScores.EXTRA_MESSAGE);
        if (message != null) {
            Snackbar.make(this.findViewById(R.id.activity_games_list), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void createToolbar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.games);
        setSupportActionBar(myToolbar);
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (deleteMode) {
                final Game game = (Game) parent.getItemAtPosition(position);
                generateDeleteConfirmationDialog(game);
            }
        }
    };

    private void generateDeleteConfirmationDialog(final Game game) {
        AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(this);
        confirmationDialog.setMessage("Delete " + game.getFormattedDateString() + " game?");
        confirmationDialog.setCancelable(true);

        DialogInterface.OnClickListener deleteGame = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                boolean gameDeleted = dbHandler.deleteSelectedGame(game);
                String message = (gameDeleted) ? "Game deleted!" : "Error deleting game!";
                games = dbHandler.getAllGames();
                gamesListView.setAdapter(new GameListAdapter(GamesList.this, games, deleteMode));
                Snackbar.make(GamesList.this.findViewById(R.id.activity_games_list), message, Snackbar.LENGTH_SHORT).show();
            }
        };
        DialogInterface.OnClickListener cancel = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        };

        confirmationDialog.setPositiveButton("Yes", deleteGame);
        confirmationDialog.setNegativeButton("No", cancel);
        AlertDialog confirmationAlert = confirmationDialog.create();
        confirmationAlert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_bar_games, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, AddScores.class);
                startActivity(intent);
                return true;

            case R.id.action_delete:
                if (deleteMode) {
                    deleteMode = false;
                    item.setIcon(R.drawable.ic_delete);
                    gamesListView.setAdapter(new GameListAdapter(this, games, deleteMode));
                } else {
                    deleteMode = true;
                    item.setIcon(R.drawable.ic_clear);
                    gamesListView.setAdapter(new GameListAdapter(this, games, deleteMode));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
