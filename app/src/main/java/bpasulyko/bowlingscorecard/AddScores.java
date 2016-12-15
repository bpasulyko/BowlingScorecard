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
import bpasulyko.bowlingscorecard.models.ui.Game;

public class AddScores extends AppCompatActivity {

    public static String EXTRA_MESSAGE = "bpasulyko.bowlingscorecard.AddScores.MESSAGE";
    private TextView dateView;
    private Calendar calendar = Calendar.getInstance();
    private int year, month, day;
    private final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
    private MainDbHandler dbHandler;
    private ScoreCard scorecard;
    private EditText firstGameInput, secondGameInput, thirdGameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_scores);
        dbHandler = new MainDbHandler(this, null, null, 1);
        Intent intent = getIntent();
        scorecard = (ScoreCard) intent.getSerializableExtra(GamesList.EXTRA_MESSAGE);
        dateView = (TextView) findViewById(R.id.datePicker);
        firstGameInput = (EditText) findViewById(R.id.firstGame);
        secondGameInput = (EditText) findViewById(R.id.secondGame);
        thirdGameInput = (EditText) findViewById(R.id.thirdGame);
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
        firstGameInput.setText("");
        secondGameInput.setText("");
        thirdGameInput.setText("");
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
        String firstGame = firstGameInput.getText().toString();
        String secondGame = secondGameInput.getText().toString();
        String thirdGame = thirdGameInput.getText().toString();

        boolean gameAdded = dbHandler.addGame(scorecard.getId(), new Game(
            gameDate,
            (!firstGame.equals("")) ? Double.parseDouble(firstGame) : null,
            (!secondGame.equals("")) ? Double.parseDouble(secondGame) : null,
            (!thirdGame.equals("")) ? Double.parseDouble(thirdGame) : null
        ));

        if (gameAdded) {
            Intent intent = new Intent(this, GamesList.class);
            intent.putExtra(EXTRA_MESSAGE, scorecard);
            startActivity(intent);
        } else {
            Snackbar.make(this.findViewById(R.id.activity_games_list), "Error occurred", Snackbar.LENGTH_SHORT).show();
        }
    }
}
