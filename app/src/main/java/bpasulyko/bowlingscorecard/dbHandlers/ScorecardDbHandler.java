package bpasulyko.bowlingscorecard.dbHandlers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import bpasulyko.bowlingscorecard.models.ScoreCard;
import bpasulyko.bowlingscorecard.models.ui.Game;

class ScorecardDbHandler {

    private final MainDbHandler dbHandler;
    private final GameDbHandler gameDbHandler;

    ScorecardDbHandler(MainDbHandler dbHandler, GameDbHandler gameDbHandler) {
        this.dbHandler = dbHandler;
        this.gameDbHandler = gameDbHandler;
    }

    boolean isValidScorecardName(String scorecardName) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        String query = String.format("SELECT * FROM %s WHERE %s =  \"%s\"", BowlingScorecardContract.Scorecard.TABLE_NAME, BowlingScorecardContract.Scorecard.COLUMN_NAME, scorecardName);
        Cursor cursor = db.rawQuery(query, null);
        boolean isValid = !cursor.moveToFirst();
        cursor.close();
        db.close();
        return isValid;
    }

    boolean addNewScorecard(String scorecardName) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues scorecardValues = new ContentValues();
        scorecardValues.put(BowlingScorecardContract.Scorecard.COLUMN_NAME, scorecardName);
        db.insert(BowlingScorecardContract.Scorecard.TABLE_NAME, null, scorecardValues);
        db.close();
        return true;
    }

    List<ScoreCard> getAllScorecards() {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        List<ScoreCard> scorecards = getAllScorecards(db);
        db.close();
        return scorecards;
    }

    private List<ScoreCard> getAllScorecards(SQLiteDatabase db) {
        String query = "SELECT " + BowlingScorecardContract.Scorecard.COLUMN_ID + ", " +
                BowlingScorecardContract.Scorecard.COLUMN_NAME +
                " FROM " + BowlingScorecardContract.Scorecard.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        List<ScoreCard> scorecards = new ArrayList<>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            int idColumn = cursor.getColumnIndex(BowlingScorecardContract.Scorecard.COLUMN_ID);
            int nameColumn = cursor.getColumnIndex(BowlingScorecardContract.Scorecard.COLUMN_NAME);
            do {
                Integer id = cursor.getInt(idColumn);
                String name = cursor.getString(nameColumn);
                scorecards.add(new ScoreCard(id, name));
            } while(cursor.moveToNext());
            cursor.close();
        }
        return scorecards;
    }

    boolean deleteSelectedScorecard(ScoreCard scorecard) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        List<Game> games = gameDbHandler.getAllGames(db, scorecard.getId());
        int gamesDeleted = 0;

        db.beginTransaction();
        for (Game game : games) {
            gamesDeleted += gameDbHandler.deleteGame(db, game);
        }
        int rowsAffected = db.delete(BowlingScorecardContract.Scorecard.TABLE_NAME, BowlingScorecardContract.Game.COLUMN_ID + " = ?",
                new String[]{String.valueOf(scorecard.getId())});
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return rowsAffected > 0 && gamesDeleted == games.size();
    }
}
