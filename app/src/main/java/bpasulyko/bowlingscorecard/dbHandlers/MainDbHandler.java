package bpasulyko.bowlingscorecard.dbHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bpasulyko.bowlingscorecard.models.Game;
import bpasulyko.bowlingscorecard.models.ScoreCard;

public class MainDbHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "bowlingScorecard.db";
    private static final int DB_VERSION = 1;

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
                    + "FOREIGN KEY (" + BowlingScorecardContract.Game.COLUMN_SCORECARD_ID + ") REFERENCES " + BowlingScorecardContract.Scorecard.TABLE_NAME + "(" + BowlingScorecardContract.Scorecard.COLUMN_ID + "))";

    public MainDbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         db.execSQL(CREATE_SCORECARD_TABLE);
         db.execSQL(CREATE_GAME_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addGame(long gameDate, Double firstGameScore, Double secondGameScore, Double thirdGameScore, Integer scorecardId) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Game> games = getAllGames(db, scorecardId);
        ContentValues gameValues = new ContentValues();
        gameValues.put(BowlingScorecardContract.Game.COLUMN_GAME_DATE, gameDate);
        gameValues.put(BowlingScorecardContract.Game.COLUMN_FIRST_GAME, firstGameScore);
        gameValues.put(BowlingScorecardContract.Game.COLUMN_SECOND_GAME, secondGameScore);
        gameValues.put(BowlingScorecardContract.Game.COLUMN_THIRD_GAME, thirdGameScore);
        gameValues.put(BowlingScorecardContract.Game.COLUMN_GAME_TOTAL, (firstGameScore + secondGameScore + thirdGameScore));
        gameValues.put(BowlingScorecardContract.Game.COLUMN_AVERAGE, getAverage(firstGameScore, secondGameScore, thirdGameScore, games));
        gameValues.put(BowlingScorecardContract.Game.COLUMN_SCORECARD_ID, scorecardId);
        db.insert(BowlingScorecardContract.Game.TABLE_NAME, null, gameValues);
        db.close();
        return true;
    }

    private Double getAverage(Double firstGameScore, Double secondGameScore, Double thirdGameScore, List<Game> games) {
        Double total = 0d;
        List<Double> allScores = new ArrayList<>();
        for (Game game : games) {
            allScores.addAll(game.getScores());
        }
        allScores.add(firstGameScore);
        allScores.add(secondGameScore);
        allScores.add(thirdGameScore);
        for (Double score : allScores) {
            total += score;
        }
        return Math.floor(total / allScores.size());
    }

    public boolean isValidScorecardName(String scorecardName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("SELECT * FROM %s WHERE %s =  \"%s\"", BowlingScorecardContract.Scorecard.TABLE_NAME, BowlingScorecardContract.Scorecard.COLUMN_NAME, scorecardName);
        Cursor cursor = db.rawQuery(query, null);
        boolean isValid = !cursor.moveToFirst();
        cursor.close();
        db.close();
        return isValid;
    }

    public boolean addNewScorecard(String scorecardName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues scorecardValues = new ContentValues();
        scorecardValues.put(BowlingScorecardContract.Scorecard.COLUMN_NAME, scorecardName);
        db.insert(BowlingScorecardContract.Scorecard.TABLE_NAME, null, scorecardValues);
        db.close();
        return true;
    }

    public List<ScoreCard> getAllScorecards() {
        SQLiteDatabase db = this.getWritableDatabase();
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

    public List<Game> getAllGames(Integer scorecardId) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Game> games = getAllGames(db, scorecardId);
        db.close();
        return games;
    }

    @NonNull
    private List<Game> getAllGames(SQLiteDatabase db, Integer scoreCardId) {
        String selectString = String.format("SELECT %s, %s, %s, %s, %s, %s, %s FROM %s",
                BowlingScorecardContract.Game.COLUMN_ID,
                BowlingScorecardContract.Game.COLUMN_GAME_DATE,
                BowlingScorecardContract.Game.COLUMN_FIRST_GAME,
                BowlingScorecardContract.Game.COLUMN_SECOND_GAME,
                BowlingScorecardContract.Game.COLUMN_THIRD_GAME,
                BowlingScorecardContract.Game.COLUMN_GAME_TOTAL,
                BowlingScorecardContract.Game.COLUMN_AVERAGE,
                BowlingScorecardContract.Game.TABLE_NAME);
        String whereClause = (scoreCardId != null) ? " WHERE " + BowlingScorecardContract.Game.COLUMN_SCORECARD_ID + " = " + scoreCardId : "";
        String orderByClause = String.format(" ORDER BY %s DESC", BowlingScorecardContract.Game.COLUMN_GAME_DATE);
        String query = selectString + whereClause + orderByClause;
        Cursor cursor = db.rawQuery(query, null);
        List<Game> games = new ArrayList<>();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            int idColumn = cursor.getColumnIndex(BowlingScorecardContract.Game.COLUMN_ID);
            int dateColumn = cursor.getColumnIndex(BowlingScorecardContract.Game.COLUMN_GAME_DATE);
            int firstGameColumn = cursor.getColumnIndex(BowlingScorecardContract.Game.COLUMN_FIRST_GAME);
            int secondGameColumn = cursor.getColumnIndex(BowlingScorecardContract.Game.COLUMN_SECOND_GAME);
            int thirdGameColumn = cursor.getColumnIndex(BowlingScorecardContract.Game.COLUMN_THIRD_GAME);
            int totalColumn = cursor.getColumnIndex(BowlingScorecardContract.Game.COLUMN_GAME_TOTAL);
            int averageColumn = cursor.getColumnIndex(BowlingScorecardContract.Game.COLUMN_AVERAGE);
            do {
                Integer id = cursor.getInt(idColumn);
                long gameDate = cursor.getLong(dateColumn);
                List<Double> scores = Arrays.asList(cursor.getDouble(firstGameColumn), cursor.getDouble(secondGameColumn), cursor.getDouble(thirdGameColumn));
                Double total = cursor.getDouble(totalColumn);
                Double average = cursor.getDouble(averageColumn);
                games.add(new Game(id, gameDate, scores, total, average));
            } while(cursor.moveToNext());
            cursor.close();
        }
        return games;
    }

    public boolean deleteSelectedGame(Game game) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(BowlingScorecardContract.Game.TABLE_NAME, BowlingScorecardContract.Game.COLUMN_ID + " = ?",
                new String[]{String.valueOf(game.getId())});
        db.close();
        return rowsAffected > 0;
    }

//    public void updateDb() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DROP TABLE IF EXISTS " + BowlingScorecardContract.Scorecard.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + BowlingScorecardContract.Game.TABLE_NAME);
//        db.execSQL(CREATE_SCORECARD_TABLE);
//        db.execSQL(CREATE_GAME_TABLE);
//        ContentValues scorecard = new ContentValues();
//        scorecard.put(BowlingScorecardContract.Scorecard.COLUMN_NAME, "2016/17");
//        long id = db.insert(BowlingScorecardContract.Scorecard.TABLE_NAME, null, scorecard);
//        System.out.println("INSERTED ID = " + id);
//
//        List<Game> games = getAllGamesOLD(db);
//        for (Game game : games) {
//            ContentValues gameValues = new ContentValues();
//            gameValues.put(BowlingScorecardContract.Game.COLUMN_GAME_DATE, game.getGameDate());
//            gameValues.put(BowlingScorecardContract.Game.COLUMN_FIRST_GAME, game.getScores().get(0));
//            gameValues.put(BowlingScorecardContract.Game.COLUMN_SECOND_GAME, game.getScores().get(1));
//            gameValues.put(BowlingScorecardContract.Game.COLUMN_THIRD_GAME, game.getScores().get(2));
//            gameValues.put(BowlingScorecardContract.Game.COLUMN_GAME_TOTAL, game.getTotal());
//            gameValues.put(BowlingScorecardContract.Game.COLUMN_AVERAGE, game.getAverage());
//            gameValues.put(BowlingScorecardContract.Game.COLUMN_SCORECARD_ID, id);
//            long id1 = db.insert(BowlingScorecardContract.Game.TABLE_NAME, null, gameValues);
//            System.out.println("GAME ID = " + id1);
//        }
//        db.close();
//    }
}
