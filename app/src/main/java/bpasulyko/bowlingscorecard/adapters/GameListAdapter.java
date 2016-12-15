package bpasulyko.bowlingscorecard.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import bpasulyko.bowlingscorecard.R;
import bpasulyko.bowlingscorecard.models.ui.Game;

public class GameListAdapter extends BaseAdapter {

    private List<Game> games;
    private static LayoutInflater inflater=null;
    private boolean visible = false;

    public GameListAdapter(Activity a, List<Game> games, boolean visible) {
        this.games = games;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.visible = visible;
    }

    public int getCount() {
        return games.size();
    }

    public Game getItem(int position) {
        return games.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(convertView == null) {
            vi = inflater.inflate(R.layout.game_row, null);
        }

        Game game = games.get(position);
        TextView heading = (TextView)vi.findViewById(R.id.game_date_list_item);
        TextView subHeading = (TextView)vi.findViewById(R.id.scores_sub_item);
        ViewGroup removeIconView = (ViewGroup) vi.findViewById(R.id.remove_icon);

        heading.setText(game.getFormattedDateString() + ":  " + game.getScoresString());
        DecimalFormat df = Game.getDecimalFormat();
        Double total = game.getTotal();
        String average = df.format(Math.floor(total / 3));
        subHeading.setText(String.format("Total: %s  Average: %s  Running Avg: %s", df.format(total), average, df.format(game.getAverage())));

        if (visible) {
            int left = (int) (50 * vi.getResources().getDisplayMetrics().density);
            ViewGroup gameDetailsView = (ViewGroup) vi.findViewById(R.id.game_row);
            gameDetailsView.setPadding(left, 0, 0, 0);
            removeIconView.setVisibility(View.VISIBLE);
        }
        return vi;
    }
}
