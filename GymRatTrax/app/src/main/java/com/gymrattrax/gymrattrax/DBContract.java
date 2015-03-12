package com.gymrattrax.gymrattrax;

import android.provider.BaseColumns;

public final class DBContract {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "grt.db";
    private static final String TYPE_TEXT = " TEXT";
    private static final String TYPE_INTEGER = " INTEGER";
    private static final String TYPE_REAL = " REAL";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String FOREIGN_KEY = " FOREIGN KEY ";
    private static final String REFERENCES = " REFERENCES ";
    private static final String NOT_NULL = " NOT NULL";
    private static final String COMMA_SEP = ",";
    private static final String L_PAREN = " (";
    private static final String R_PAREN = " )";

    public static final boolean ALLOW_DEBUG = true;

    private DBContract() {}

    public static abstract class ProfileTable implements BaseColumns {
        public static final String TABLE_NAME = "profile";
        public static final String COLUMN_NAME_KEY = "key";
        public static final String COLUMN_NAME_VALUE = "value";

        public static final String KEY_NAME = "NAME";
        public static final String KEY_BIRTH_DATE = "BIRTH_DATE";
        public static final String KEY_SEX = "SEX";
        public static final String KEY_HEIGHT = "HEIGHT"; //in inches
        public static final String VAL_SEX_MALE = "M";
        public static final String VAL_SEX_FEMALE = "F";
        public static final String KEY_DATE_FORMAT = "DATE_FORMAT";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + L_PAREN +
                        COLUMN_NAME_KEY   + TYPE_TEXT + PRIMARY_KEY + COMMA_SEP +
                        COLUMN_NAME_VALUE + TYPE_TEXT + NOT_NULL    + R_PAREN;
        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
    public static abstract class WeightTable implements BaseColumns {
        public static final String TABLE_NAME = "weight";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_WEIGHT = "weight";
        public static final String COLUMN_NAME_BODY_FAT_PERCENTAGE = "body_fat_percentage";
        public static final String COLUMN_NAME_ACTIVITY_LEVEL = "activity_level";

        public static final double ACT_LVL_LITTLE = 1.2;
        public static final double ACT_LVL_LIGHT = 1.375;
        public static final double ACT_LVL_MOD = 1.55;
        public static final double ACT_LVL_HEAVY = 1.725;

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + L_PAREN +
                        COLUMN_NAME_DATE                + TYPE_TEXT + PRIMARY_KEY + COMMA_SEP +
                        COLUMN_NAME_WEIGHT              + TYPE_REAL + NOT_NULL    + COMMA_SEP +
                        COLUMN_NAME_BODY_FAT_PERCENTAGE + TYPE_REAL               + COMMA_SEP +
                        COLUMN_NAME_ACTIVITY_LEVEL      + TYPE_REAL + NOT_NULL    + R_PAREN;
        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
    public static abstract class ScheduleTable implements BaseColumns {
        public static final String TABLE_NAME = "schedule";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_LENGTH_IN_DAYS = "length_in_days";
        public static final String COLUMN_NAME_START_DATE = "start_date";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_ID             + TYPE_INTEGER + PRIMARY_KEY + COMMA_SEP +
                        COLUMN_NAME_LENGTH_IN_DAYS + TYPE_INTEGER + NOT_NULL    + COMMA_SEP +
                        COLUMN_NAME_START_DATE     + TYPE_TEXT    + NOT_NULL    + R_PAREN;
        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
    public static abstract class ExerciseTable implements BaseColumns {
        public static final String TABLE_NAME = "exercise";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_TYPE = "type";

        public static final String NAME_WALK = "walk";
        public static final String NAME_JOG = "jog";
        public static final String NAME_RUN = "run";
        public static final String NAME_LIFTING_LIGHT = "lift_light";
        public static final String NAME_LIFTING_VIGOROUS = "lift_vigor";
        public static final String TYPE_CARDIO = "C";
        public static final String TYPE_STRENGTH = "S";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_NAME + TYPE_TEXT + PRIMARY_KEY + COMMA_SEP +
                        COLUMN_NAME_TYPE + TYPE_TEXT + NOT_NULL    + R_PAREN;
        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
    public static abstract class WorkoutTable implements BaseColumns {
        public static final String TABLE_NAME = "workout";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_SCHEDULE = "schedule";
        public static final String COLUMN_NAME_EXERCISE = "exercise";
        public static final String COLUMN_NAME_RECORD_TYPE = "record_type";
        public static final String COLUMN_NAME_DATE_TIME = "date_time";
        public static final String COLUMN_NAME_CALORIES = "calories";
        public static final String COLUMN_NAME_PARAMETERS = "parameters";
        public static final String COLUMN_NAME_TIME_SPENT = "time_spent";

        public static final String VAL_PROPOSED = "P";
        public static final String VAL_COMPLETE = "C";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + L_PAREN +
                        COLUMN_NAME_ID          + TYPE_INTEGER + PRIMARY_KEY + COMMA_SEP +
                        COLUMN_NAME_SCHEDULE    + TYPE_INTEGER + NOT_NULL    + COMMA_SEP +
                        COLUMN_NAME_EXERCISE    + TYPE_TEXT    + NOT_NULL    + COMMA_SEP +
                        COLUMN_NAME_RECORD_TYPE + TYPE_TEXT    + NOT_NULL    + COMMA_SEP +
                        COLUMN_NAME_DATE_TIME   + TYPE_TEXT    + NOT_NULL    + COMMA_SEP +
                        COLUMN_NAME_CALORIES    + TYPE_INTEGER + NOT_NULL    + COMMA_SEP +
                        COLUMN_NAME_PARAMETERS  + TYPE_TEXT    + NOT_NULL    + COMMA_SEP +
                        COLUMN_NAME_TIME_SPENT  + TYPE_REAL                  + COMMA_SEP +
                        FOREIGN_KEY + L_PAREN + COLUMN_NAME_SCHEDULE + R_PAREN + REFERENCES +
                        ScheduleTable.TABLE_NAME + L_PAREN + ScheduleTable.COLUMN_NAME_ID   +
                        R_PAREN + R_PAREN;
        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}