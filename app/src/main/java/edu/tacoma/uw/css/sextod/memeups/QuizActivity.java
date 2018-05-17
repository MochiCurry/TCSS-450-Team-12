/**
 * QuizActivity launches a quiz with questions taken from QuestionLibrary. The user is given 3 choices
 * for each question and only has one chance to answer correctly. At the end of the quiz, the score is
 * totaled and displayed with the ResultActivity.
 *
 * @author Travis Bain
 * @author Kerry Fergurson
 * @author Dirk Sexton
 * @version 1.0
 * @since 1.0
 */

package edu.tacoma.uw.css.sextod.memeups;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Quiz activity using questions from QuestionLibrary
 * author: Kerry Ferguson
 */
public class QuizActivity extends AppCompatActivity {
    //New radio buttons, question library to load questions, max number of questions
    RadioGroup radioGroup;
    RadioButton radioButton;
    private static int QUIZ_COUNT = 7;
    private QuestionLibrary mQuestionLibrary = new QuestionLibrary();

    private TextView mScoreView;
    private TextView mQuestionView;
    private Button mButtonChoice1;
    private Button mButtonChoice2;
    private Button mButtonChoice3;

    private String mAnswer = "Dank memes";
    private int mScore = 0;
    private int mQuestionNumber = 0;
    boolean clicked = false;
    int score = 0; // quiz score

    /**
     * On create, add the options bar the question and the three answers.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Toolbar toolbar = (Toolbar) findViewById(R.id.top_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.memeupstopicon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        radioGroup = findViewById(R.id.radioGroup);
//


        //mScoreView = (TextView)findViewById(R.id.score);
        mQuestionView = (TextView)findViewById(R.id.question);
        mButtonChoice1 = (Button)findViewById(R.id.choice1);
        mButtonChoice2 = (Button)findViewById(R.id.choice2);
        mButtonChoice3 = (Button)findViewById(R.id.choice3);
        updateQuestion();

        Button submitbutton = findViewById(R.id.submitbutton);
        /**
         * Listener for the button to submit your answer
         */
        submitbutton.setOnClickListener(new View.OnClickListener() {

            //Start of Button Listener for Button1
            // mButtonChoice1.setOnClickListener(new View.OnClickListener(){

            /**
             * On click for the submit button, check for correct answer and advance the quiz
             * @param v
             */
            @Override
            public void onClick(View v) {
                //Check which button is selected
                int radioId = radioGroup.getCheckedRadioButtonId();

                radioButton = findViewById(radioId);
                clicked = true;
                //If correct answer, increment score and display correct
                if (radioButton.getText() == mAnswer) {
                    score += 1;
                   // Toast.makeText(QuizActivity.this, "correct", Toast.LENGTH_SHORT).show();
                //else display wrong
                } else {
                    //Toast.makeText(QuizActivity.this, "wrong", Toast.LENGTH_SHORT).show();
                }
                //Check if end of the quiz
                if (mQuestionNumber == QUIZ_COUNT) {

                    //show result
                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                    intent.putExtra("Right_Answer_Count", score);
                    startActivity(intent);
                } else //Else load the next question
                {
                    updateQuestion();

                    clicked = false;
                }
            }
        });
    }

    /**
     * Function to check which button is selected
     * @param v
     */
    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);
    }

    /**
     * Function to update the question to the next set of choices
     */
    private void updateQuestion(){
        mQuestionView.setText(mQuestionLibrary.getQuestion(mQuestionNumber));
        mButtonChoice1.setText(mQuestionLibrary.getChoice1(mQuestionNumber));
        mButtonChoice2.setText(mQuestionLibrary.getChoice2(mQuestionNumber));
        mButtonChoice3.setText(mQuestionLibrary.getChoice3(mQuestionNumber));

        mAnswer = mQuestionLibrary.getCorrectAnswer(mQuestionNumber);
        mQuestionNumber++;
    }

    /**
     * Creates option menu at top of the screen
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topbarmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Determines which option is selected
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}