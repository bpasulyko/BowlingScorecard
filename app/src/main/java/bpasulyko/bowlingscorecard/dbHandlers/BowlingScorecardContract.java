package bpasulyko.bowlingscorecard.dbHandlers;

public class BowlingScorecardContract {
    private BowlingScorecardContract() {}

    public static class Scorecard {
        public static final String TABLE_NAME = "Scorecard";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_NAME = "Name";
    }

    public static class Game {
        public static final String TABLE_NAME = "Game";
        public static final String COLUMN_ID = "Id";
        public static final String COLUMN_GAME_DATE = "GameDate";
        public static final String COLUMN_FIRST_GAME = "FirstGame";
        public static final String COLUMN_SECOND_GAME = "SecondGame";
        public static final String COLUMN_THIRD_GAME = "ThirdGame";
        public static final String COLUMN_GAME_TOTAL = "GameTotal";
        public static final String COLUMN_AVERAGE = "Average";
        public static final String COLUMN_SCORECARD_ID = "ScorecardId";
    }
}
