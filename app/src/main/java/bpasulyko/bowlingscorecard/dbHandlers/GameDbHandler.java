package bpasulyko.bowlingscorecard.dbHandlers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bpasulyko.bowlingscorecard.models.ui.Game;

class GameDbHandler {

    private final MainDbHandler dbHandler;

    GameDbHandler(MainDbHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    boolean addGame(long gameDate, Double firstGameScore, Double secondGameScore, Double thirdGameScore, Integer scorecardId) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
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

    List<Game> getAllGames(Integer scorecardId) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        List<Game> games = getAllGames(db, scorecardId);
        db.close();
        return games;
    }

    @NonNull
    List<Game> getAllGames(SQLiteDatabase db, Integer scoreCardId) {
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
        return toGames(cursor);
    }

    boolean deleteSelectedGame(Game game) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        int rowsAffected = deleteGame(db, game);
        db.close();
        return rowsAffected > 0;
    }

    int deleteGame(SQLiteDatabase db, Game game) {
        return db.delete(BowlingScorecardContract.Game.TABLE_NAME, BowlingScorecardContract.Game.COLUMN_ID + " = ?",
                new String[]{String.valueOf(game.getId())});
    }

    private List<Game> toGames(Cursor cursor) {
        List<Game> games = new ArrayList<>();
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
        return games;
    }
}
