package bpasulyko.bowlingscorecard.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import bpasulyko.bowlingscorecard.R;
import bpasulyko.bowlingscorecard.models.ScoreCard;

public class ScorecardListAdapter extends BaseAdapter {

    private List<ScoreCard> scorecards;
    private static LayoutInflater inflater=null;
    private boolean visible = false;

    public ScorecardListAdapter(Activity a, List<ScoreCard> scorecards, boolean visible) {
        this.scorecards = scorecards;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.visible = visible;
    }

    public int getCount() {
        return scorecards.size();
    }

    public ScoreCard getItem(int position) {
        return scorecards.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(convertView == null) {
            vi = inflater.inflate(R.layout.scorecard_row, null);
        }

        ScoreCard scorecard = scorecards.get(position);
        TextView heading = (TextView)vi.findViewById(R.id.game_date_list_item);
        ViewGroup removeIconView = (ViewGroup) vi.findViewById(R.id.remove_icon);
        heading.setText(scorecard.getName());

        if (visible) {
            int left = (int) (50 * vi.getResources().getDisplayMetrics().density);
            ViewGroup gameDetailsView = (ViewGroup) vi.findViewById(R.id.game_row);
            gameDetailsView.setPadding(left, 0, 0, 0);
            removeIconView.setVisibility(View.VISIBLE);
        }
        return vi;
    }
}
