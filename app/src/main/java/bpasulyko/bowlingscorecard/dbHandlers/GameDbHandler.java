package bpasulyko.bowlingscorecard.dbHandlers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import bpasulyko.bowlingscorecard.models.ui.Game;

class GameDbHandler {

    private final MainDbHandler dbHandler;
    private final String selectString = String.format("SELECT %s, %s, %s, %s, %s, %s, %s FROM %s",
            BowlingScorecardContract.Game.COLUMN_ID,
            BowlingScorecardContract.Game.COLUMN_GAME_DATE,
            BowlingScorecardContract.Game.COLUMN_FIRST_GAME,
            BowlingScorecardContract.Game.COLUMN_SECOND_GAME,
            BowlingScorecardContract.Game.COLUMN_THIRD_GAME,
            BowlingScorecardContract.Game.COLUMN_GAME_TOTAL,
            BowlingScorecardContract.Game.COLUMN_AVERAGE,
            BowlingScorecardContract.Game.TABLE_NAME);

    GameDbHandler(MainDbHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    boolean addGame(Integer scorecardId, Game gameToAdd) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        gameToAdd.setAverage(getAllGames(db, scorecardId));
        ContentValues gameValues = new ContentValues();
        gameValues.put(BowlingScorecardContract.Game.COLUMN_GAME_DATE, gameToAdd.getGameDate());
        gameValues.put(BowlingScorecardContract.Game.COLUMN_FIRST_GAME, gameToAdd.getFirstGame());
        gameValues.put(BowlingScorecardContract.Game.COLUMN_SECOND_GAME, gameToAdd.getSecondGame());
        gameValues.put(BowlingScorecardContract.Game.COLUMN_THIRD_GAME, gameToAdd.getThirdGame());
        gameValues.put(BowlingScorecardContract.Game.COLUMN_GAME_TOTAL, gameToAdd.getTotal());
        gameValues.put(BowlingScorecardContract.Game.COLUMN_AVERAGE, gameToAdd.getAverage());
        gameValues.put(BowlingScorecardContract.Game.COLUMN_SCORECARD_ID, scorecardId);
        db.insert(BowlingScorecardContract.Game.TABLE_NAME, null, gameValues);
        db.close();
        return true;
    }

    List<Game> getAllGames(Integer scorecardId) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        List<Game> games = getAllGames(db, scorecardId);
        db.close();
        return games;
    }

    @NonNull
    List<Game> getAllGames(SQLiteDatabase db, Integer scoreCardId) {
        String whereClause = (scoreCardId != null) ? " WHERE " + BowlingScorecardContract.Game.COLUMN_SCORECARD_ID + " = " + scoreCardId : "";
        String orderByClause = String.format(" ORDER BY %s DESC", BowlingScorecardContract.Game.COLUMN_GAME_DATE);
        String query = selectString + whereClause + orderByClause;
        Cursor cursor = db.rawQuery(query, null);
        return toGames(cursor);
    }

    @NonNull
    private List<Game> getAllFullSeriesGames(SQLiteDatabase db, Integer scoreCardId) {
        String whereClause = " WHERE " + BowlingScorecardContract.Game.COLUMN_SCORECARD_ID + " = " + scoreCardId +
                " AND " + BowlingScorecardContract.Game.COLUMN_FIRST_GAME + " > 0 " +
                " AND " + BowlingScorecardContract.Game.COLUMN_SECOND_GAME + " > 0 " +
                " AND " + BowlingScorecardContract.Game.COLUMN_THIRD_GAME + " > 0 ";
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
            Double firstGame = cursor.getDouble(firstGameColumn);
            Double secondGame = cursor.getDouble(secondGameColumn);
            Double thirdGame = cursor.getDouble(thirdGameColumn);
            Double total = cursor.getDouble(totalColumn);
            Double average = cursor.getDouble(averageColumn);
            games.add(new Game(id, gameDate, firstGame, secondGame, thirdGame, total, average));
        } while(cursor.moveToNext());
        cursor.close();
        return games;
    }

    public boolean updateGame(Integer scorecardId, Game game) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        game.setAverage(getAllFullSeriesGames(db, scorecardId));
        ContentValues gameValues = new ContentValues();
        gameValues.put(BowlingScorecardContract.Game.COLUMN_FIRST_GAME, game.getFirstGame());
        gameValues.put(BowlingScorecardContract.Game.COLUMN_SECOND_GAME, game.getSecondGame());
        gameValues.put(BowlingScorecardContract.Game.COLUMN_THIRD_GAME, game.getThirdGame());
        gameValues.put(BowlingScorecardContract.Game.COLUMN_GAME_TOTAL, game.getTotal());
        gameValues.put(BowlingScorecardContract.Game.COLUMN_AVERAGE, game.getAverage());
        int rowsAffected = db.update(
                BowlingScorecardContract.Game.TABLE_NAME,
                gameValues,
                BowlingScorecardContract.Game.COLUMN_ID + " = ? ",
                new String[]{String.valueOf(game.getId())}
        );
        db.close();
        return rowsAffected > 0;
    }
}
