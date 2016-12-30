package bpasulyko.bowlingscorecard.dbHandlers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import bpasulyko.bowlingscorecard.models.ScoreCard;
import bpasulyko.bowlingscorecard.models.ui.Game;

public class MainDbHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "bowlingScorecard.db";
    private static final int DB_VERSION = 1;
    private ScorecardDbHandler scorecardDbHandler;
    private GameDbHandler gameDbHandler;

    private static final String CREATE_SCORECARD_TABLE =
            "CREATE TABLE " + BowlingScorecardContract.Scorecard.TABLE_NAME + "("
                    + BowlingScorecardContract.Scorecard.COLUMN_ID + " INTEGER PRIMARY KEY,"
                    + BowlingScorecardContract.Scorecard.COLUMN_NAME + " TEXT)";

    private static final String CREATE_GAME_TABLE =
            "CREATE TABLE " + BowlingScorecardContract.Game.TABLE_NAME + "("
                    + BowlingScorecardContract.Game.COLUMN_ID + " INTEGER PRIMARY KEY,"
                    + BowlingScorecardContract.Game.COLUMN_GAME_DATE + " INTEGER,"
                    + BowlingScorecardContract.Game.COLUMN_FIRST_GAME + " REAL,"
                    + BowlingScorecardContract.Game.COLUMN_SECOND_GAME + " REAL,"
                    + BowlingScorecardContract.Game.COLUMN_THIRD_GAME + " REAL,"
                    + BowlingScorecardContract.Game.COLUMN_GAME_TOTAL + " REAL,"
                    + BowlingScorecardContract.Game.COLUMN_AVERAGE + " REAL,"
                    + BowlingScorecardContract.Game.COLUMN_SCORECARD_ID + " INTEGER,"
                    + " FOREIGN KEY (" + BowlingScorecardContract.Game.COLUMN_SCORECARD_ID + ") "
                    + " REFERENCES " + BowlingScorecardContract.Scorecard.TABLE_NAME + "(" + BowlingScorecardContract.Scorecard.COLUMN_ID + "))";

    public MainDbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VERSION);
        gameDbHandler = new GameDbHandler(this);
        scorecardDbHandler = new ScorecardDbHandler(this, gameDbHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         db.execSQL(CREATE_SCORECARD_TABLE);
         db.execSQL(CREATE_GAME_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /* SCORECARD */
    public boolean addNewScorecard(String scorecardName) {
        return scorecardDbHandler.addNewScorecard(scorecardName);
    }

    public List<ScoreCard> getAllScorecards() {
        return scorecardDbHandler.getAllScorecards();
    }

    public boolean deleteSelectedScorecard(ScoreCard scorecard) {
        return scorecardDbHandler.deleteSelectedScorecard(scorecard);
    }

    /* GAME */
    public boolean addGame(Integer scorecardId, Game game) {
        return gameDbHandler.addGame(scorecardId, game);
    }

    public boolean isValidScorecardName(String scorecardName) {
        return scorecardDbHandler.isValidScorecardName(scorecardName);
    }

    public List<Game> getAllGames(Integer scorecardId) {
        return gameDbHandler.getAllGames(scorecardId);
    }

    public boolean deleteSelectedGame(Game game) {
        return gameDbHandler.deleteSelectedGame(game);
    }

    public boolean updateGame(Integer scorecardId, Game game) {
        return gameDbHandler.updateGame(scorecardId, game);
    }
}
