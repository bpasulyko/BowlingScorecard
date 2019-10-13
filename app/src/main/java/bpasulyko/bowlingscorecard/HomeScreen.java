package bpasulyko.bowlingscorecard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        createToolbar();
    }

    private void createToolbar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.app_name);
        setSupportActionBar(myToolbar);
    }

    public void viewStats(View view) {
        Intent intent = new Intent(this, Stats.class);
        startActivity(intent);
    }

    public void viewAllScorecards(View view) {
        Intent intent = new Intent(this, ScorecardsList.class);
        startActivity(intent);
    }
}
