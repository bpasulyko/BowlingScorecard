package bpasulyko.bowlingscorecard.dbHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import bpasulyko.bowlingscorecard.models.Game;

public class MainDbHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "bowlingScorecard.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_SCORES = "Scores";
    private static final String COLUMN_SCORES_ID = "id";
    private static final String COLUMN_GAME_DATE = "gameDate";
    private static final String COLUMN_FIRST_GAME = "firstGame";
    private static final String COLUMN_SECOND_GAME = "secondGame";
    private static final String COLUMN_THIRD_GAME = "thirdGame";
    private static final String COLUMN_GAME_TOTAL = "gameTotal";
    private static final String COLUMN_AVERAGE = "average";
    private static final String CREATE_SCORES_TABLE =
            "CREATE TABLE " + TABLE_SCORES + "("
            + COLUMN_SCORES_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_GAME_DATE + " INTEGER,"
            + COLUMN_FIRST_GAME + " INTEGER,"
            + COLUMN_SECOND_GAME + " INTEGER,"
            + COLUMN_THIRD_GAME + " INTEGER,"
            + COLUMN_GAME_TOTAL + " INTEGER,"
            + COLUMN_AVERAGE + " INTEGER)";

    public MainDbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SCORES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addGame(long gameDate, Integer firstGameScore, Integer secondGameScore, Integer thirdGameScore) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues gameValues = new ContentValues();
        gameValues.put(COLUMN_GAME_DATE, gameDate);
        gameValues.put(COLUMN_FIRST_GAME, firstGameScore);
        gameValues.put(COLUMN_SECOND_GAME, secondGameScore);
        gameValues.put(COLUMN_THIRD_GAME, thirdGameScore);
        gameValues.put(COLUMN_GAME_TOTAL, (firstGameScore + secondGameScore + thirdGameScore));
        gameValues.put(COLUMN_AVERAGE, 0);
        db.insert(TABLE_SCORES, null, gameValues);
        db.close();
        return true;
    }

    public List<Game> getAllGames() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_SCORES;
        Cursor cursor = db.rawQuery(query, null);
        List<Game> games = new ArrayList<>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            int idColumn = cursor.getColumnIndex(COLUMN_SCORES_ID);
            int dateColumn = cursor.getColumnIndex(COLUMN_GAME_DATE);
            int firstGameColumn = cursor.getColumnIndex(COLUMN_FIRST_GAME);
            int secondGameColumn = cursor.getColumnIndex(COLUMN_SECOND_GAME);
            int thirdGameColumn = cursor.getColumnIndex(COLUMN_SECOND_GAME);
            int totalColumn = cursor.getColumnIndex(COLUMN_GAME_TOTAL);
            int averageColumn = cursor.getColumnIndex(COLUMN_AVERAGE);
            do {
                Date gameDate = new Date(cursor.getLong(dateColumn));
                List<Integer> scores = Arrays.asList(cursor.getInt(firstGameColumn), cursor.getInt(secondGameColumn), cursor.getInt(thirdGameColumn));
                Integer total = cursor.getInt(totalColumn);
                Integer average = cursor.getInt(averageColumn);
                games.add(new Game(idColumn, gameDate, scores, total, average));
            } while(cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return games;
    }
}