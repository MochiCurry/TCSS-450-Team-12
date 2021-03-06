package edu.tacoma.uw.css.sextod.memeups.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import edu.tacoma.uw.css.sextod.memeups.R;


/**
 * This class is to run sqlLite for matches the user inherits while using the app.
 * It will create a database on the app and display it when the user is not online.
 * @author Kerry Ferguson
 * @author Travis Bain
 * @author Dirk Sexton
 */
public class MatchDB {


    /**
     * Constant for DB Version string
     */
    public static final int DB_VERSION = 1;
    /**
     * Constant for DB Name string
     */
    public static final String DB_NAME = "Course.db";

    private static final String COURSE_TABLE = "Course";

    private CourseDBHelper mCourseDBHelper;
    private SQLiteDatabase mSQLiteDatabase;

    /**
     * Constructor for MatchDB
     * @param context The Context
     */
    public MatchDB(Context context) {
        mCourseDBHelper = new CourseDBHelper(
                context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mCourseDBHelper.getWritableDatabase();
    }

    /**
     * Inserts the course into the local sqlite table. Returns true if successful, false otherwise.
     * @param email Email field
     * @param first First name field
     * @param last Last name field
     * @param username Username field
     * @param bio Bio field
     * @param display Display pic field
     * @param meme Meme pic field
     * @param score Score field
     * @return Returns if completed
     */
    public boolean insertCourse(String email, String first, String last, String username, String bio,
                                String display,
                                String meme, int score) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("first", first);
        contentValues.put("last", last);
        contentValues.put("username", username);
        contentValues.put("bio", bio);
        contentValues.put("display_url", display);
        contentValues.put("meme_url", meme);

        contentValues.put("score_category", score);

        long rowId = mSQLiteDatabase.insert("Course", null, contentValues);
        return rowId != -1;
    }
    /**
     * Delete all the data from the COURSE_TABLE
     */
    public void deleteCourses() {
        mSQLiteDatabase.delete(COURSE_TABLE, null, null);
    }

    /**
     * Returns the list of courses from the local Course table.
     * @return list
     */
    public List<Match> getCourses() {

        String[] columns = {
                "email", "first", "last", "username", "bio",
                "display_url",
                "meme_url", "score_category"
        };

        Cursor c = mSQLiteDatabase.query(
                COURSE_TABLE,  // The table to query
                columns,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        c.moveToFirst();
        List<Match> list = new ArrayList<Match>();
        for (int i=0; i<c.getCount(); i++) {
            String email = c.getString(0);
            String first = c.getString(1);
            String last = c.getString(2);
            String username = c.getString(3);
            String bio = c.getString(4);
            String display_url = c.getString(5);
            String meme_url = c.getString(6);
            int score = c.getInt(7);

            Match course = new Match(email, first, last, username, bio,
                    display_url,
                    meme_url, score);
            list.add(course);
            c.moveToNext();
        }

        return list;
    }

    /**
     * Helper class for the CourseDB
     */
    class CourseDBHelper extends SQLiteOpenHelper {

        private final String CREATE_COURSE_SQL;

        private final String DROP_COURSE_SQL;

        /**
         * This class is a helper that will locate the locally stored database stored through strings.
         * @param context The context
         * @param name Name
         * @param factory Factory
         * @param version Version
         */
        public CourseDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            CREATE_COURSE_SQL = context.getString(R.string.CREATE_COURSE_SQL);
            DROP_COURSE_SQL = context.getString(R.string.DROP_COURSE_SQL);

        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_COURSE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_COURSE_SQL);
            onCreate(sqLiteDatabase);
        }
    }



}
