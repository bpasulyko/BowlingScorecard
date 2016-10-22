package bpasulyko.bowlingscorecard;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import bpasulyko.bowlingscorecard.dbHandlers.MainDbHandler;

public class MainActivity extends AppCompatActivity {

    private EditText dateView;
    private Calendar calendar = Calendar.getInstance();
    private int year, month, day;
    private final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
    private MainDbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new MainDbHandler(this, null, null, 1);

        dateView = (EditText) findViewById(R.id.datePicker);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate();
    }

    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private void showDate() {
        dateView.setText(formatter.format(calendar.getTime()));
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
        EditText firstGame = (EditText) findViewById(R.id.firstGame);
        EditText secondGame = (EditText) findViewById(R.id.secondGame);
        EditText thirdGame = (EditText) findViewById(R.id.thirdGame);
        String firstGameScore = firstGame.getText().toString();
        String secondGameScore = secondGame.getText().toString();
        String thirdGameScore = thirdGame.getText().toString();

        boolean gameAdded = dbHandler.addGame(gameDate, firstGameScore, secondGameScore, thirdGameScore);
        String message = (gameAdded) ? "Game saved!" : "An error has occurred!";
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
