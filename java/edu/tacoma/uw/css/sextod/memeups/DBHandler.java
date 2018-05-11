package edu.tacoma.uw.css.sextod.memeups;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.tacoma.uw.css.sextod.memeups.quiz.Quiz;

/**
 * Created by kferg on 5/7/2018.
 */

public class DBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "quiz";

    // Contacts table name
    private static final String TABLE_QUIZ_DETAIL = "quizDetails";

    // Contacts Table Columns names
    private static final String KEY_QUESTIONS = "questions";
    private static final String KEY_CHOICES = "choices";
    private static final String KEY_CORRECT_ANSWERS = "correctAnswers";

    public DBHandler(Context contex) {
        super(contex, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_STUDENT_DETAIL_TABLE = "CREATE TABLE " + TABLE_QUIZ_DETAIL + "("
                + KEY_QUESTIONS + " INTEGER PRIMARY KEY,"
                + KEY_CHOICES + " TEXT,"
                + KEY_CORRECT_ANSWERS + " TEXT,";

        db.execSQL(CREATE_STUDENT_DETAIL_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_DETAIL);

        // Create tables again
        onCreate(db);
    }


    // Adding new Question Information
    void addNewQuestions(Quiz newQ) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_QUESTIONS, newQ.get_questions());
        values.put(KEY_CHOICES, newQ.get_choices());
        values.put(KEY_CORRECT_ANSWERS, newQ.get_answers());


        // Inserting Row
        db.insert(TABLE_QUIZ_DETAIL, null, values);
        db.close(); // Closing database connection
    }

    public boolean deleteQuestions(int delQ){

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_QUIZ_DETAIL, KEY_QUESTIONS + "=" + delQ, null) > 0;

    }



}
