package bpasulyko.bowlingscorecard;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import bpasulyko.bowlingscorecard.dbHandlers.MainDbHandler;
import bpasulyko.bowlingscorecard.models.ScoreCard;

public class AddScores extends AppCompatActivity {

    public static String EXTRA_MESSAGE = "bpasulyko.bowlingscorecard.AddScores.MESSAGE";
    private TextView dateView;
    private Calendar calendar = Calendar.getInstance();
    private int year, month, day;
    private final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
    private MainDbHandler dbHandler;
    private ScoreCard scorecard;
    private EditText firstGame, secondGame, thirdGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_scores);
        dbHandler = new MainDbHandler(this, null, null, 1);
        Intent intent = getIntent();
        scorecard = (ScoreCard) intent.getSerializableExtra(GamesList.EXTRA_MESSAGE);
        dateView = (TextView) findViewById(R.id.datePicker);
        firstGame = (EditText) findViewById(R.id.firstGame);
        secondGame = (EditText) findViewById(R.id.secondGame);
        thirdGame = (EditText) findViewById(R.id.thirdGame);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        createToolbar();
        initializeInputs();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initializeInputs();
    }

    private void createToolbar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getResources().getString(R.string.new_game) + " - " + scorecard.getName());
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void initializeInputs() {
        firstGame.setText("");
        secondGame.setText("");
        thirdGame.setText("");
        showDate();
    }

    private void showDate() {
        dateView.setText(formatter.format(calendar.getTime()));
    }

    public void setDate(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, myDateListener, year, month, day);
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(year, monthOfYear, dayOfMonth);
            showDate();
        }
    };

    public void saveGames(View view) {
        long gameDate = calendar.getTimeInMillis();
        String firstGameScore = firstGame.getText().toString();
        String secondGameScore = secondGame.getText().toString();
        String thirdGameScore = thirdGame.getText().toString();

//        if (invalidScores(firstGameScore, secondGameScore, thirdGameScore)) {
//            Snackbar.make(this.findViewById(R.id.activity_main), "Fill in all scores!", Snackbar.LENGTH_SHORT).show();
//        } else {
            boolean gameAdded = dbHandler.addGame(
                    gameDate,
                    Double.parseDouble(firstGameScore),
                    Double.parseDouble(secondGameScore),
                    Double.parseDouble(thirdGameScore),
                    scorecard.getId());

            if (gameAdded) {
                Intent intent = new Intent(this, GamesList.class);
                intent.putExtra(EXTRA_MESSAGE, scorecard);
                startActivity(intent);
            } else {
                Snackbar.make(this.findViewById(R.id.activity_games_list), "Error occurred", Snackbar.LENGTH_SHORT).show();
            }
//        }
    }

    private boolean invalidScores(String firstGameScore, String secondGameScore, String thirdGameScore) {
        return firstGameScore.equals("") || secondGameScore.equals("") || thirdGameScore.equals("");
    }
}
