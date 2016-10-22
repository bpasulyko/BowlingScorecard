package bpasulyko.bowlingscorecard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import bpasulyko.bowlingscorecard.dbHandlers.MainDbHandler;
import bpasulyko.bowlingscorecard.models.Game;

public class GamesList extends AppCompatActivity {

    private MainDbHandler dbHandler;
    private ListView gamesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_list);
        dbHandler = new MainDbHandler(this, null, null, 1);
        List<Game> games = dbHandler.getAllGames();

        gamesListView = (ListView) findViewById(R.id.gamesList);
        ArrayAdapter<Game> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, games);
        gamesListView.setAdapter(adapter);
        gamesListView.setOnItemClickListener(itemClickListener);

        Intent intent = getIntent();
        String message = intent.getStringExtra(AddScores.EXTRA_MESSAGE);
        if (message != null) {
            Snackbar.make(this.findViewById(R.id.activity_games_list), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    public void addScores(View view) {
        Intent intent = new Intent(this, AddScores.class);
        startActivity(intent);
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            final Game game = (Game) parent.getItemAtPosition(position);
            generateDeleteConfirmationDialog(game);
        }
    };

    private void generateDeleteConfirmationDialog(final Game game) {
        AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(this);
        confirmationDialog.setMessage("Delete Game?");
        confirmationDialog.setCancelable(true);

        DialogInterface.OnClickListener deleteGame = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                boolean gameDeleted = dbHandler.deleteSelectedGame(game);
                String message = (gameDeleted) ? "Game deleted!" : "Error deleting game!";
                List<Game> games = dbHandler.getAllGames();
                ArrayAdapter<Game> adapter = new ArrayAdapter<>(GamesList.this, android.R.layout.simple_list_item_1, games);
                gamesListView.setAdapter(adapter);
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
}
