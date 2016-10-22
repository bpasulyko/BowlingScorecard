package bpasulyko.bowlingscorecard;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import bpasulyko.bowlingscorecard.dbHandlers.MainDbHandler;
import bpasulyko.bowlingscorecard.models.Game;

public class GamesList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_list);
        MainDbHandler dbHandler = new MainDbHandler(this, null, null, 1);
        List<Game> games = dbHandler.getAllGames();

        ListView gamesList = (ListView) findViewById(R.id.gamesList);
        ArrayAdapter<Game> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, games);
        gamesList.setAdapter(adapter);

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
}
