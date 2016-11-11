package bpasulyko.bowlingscorecard;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bpasulyko.bowlingscorecard.models.Game;

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
        if(convertView==null)
            vi = inflater.inflate(R.layout.game_row, null);

        TextView date = (TextView)vi.findViewById(R.id.game_date_list_item);
        TextView scores = (TextView)vi.findViewById(R.id.scores_sub_item);
        ImageView removeIcon = (ImageView) vi.findViewById(R.id.remove_icon);

        Game game = games.get(position);

        date.setText(game.getFormattedDateString());
        scores.setText(game.getScores().toString());
        if (visible) {
            removeIcon.setVisibility(View.VISIBLE);
        }
        return vi;
    }
}
