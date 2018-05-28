/**
 * ResultActivity displays the results sent from the quiz. There is a button to take you back to the
 * home screen.
 *
 * @author Travis Bain
 * @author Kerry Fergurson
 * @author Dirk Sexton
 * @version 1.0
 * @since 1.0
 */

package edu.tacoma.uw.css.sextod.memeups;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Class to display the results of the quiz
 * author: Kerry Ferguson
 */
public class ResultActivity extends AppCompatActivity {

    private Button returnButton;
    private String level0 = "Scrub";
    private String level1 = "Squire";
    private String level2 = "Alright";
    private String level3 = "Spicy";
    private String level4 = "Hokage";


    /**
     * On create, read in the quiz score and display them
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // text for results
        TextView resultLabel = (TextView) findViewById(R.id.resultLabel);
        TextView totalScoreLabel = (TextView) findViewById(R.id.totalScoreLabel);

        // get the user score
        // int score = getIntent().getIntExtra("Right_Answer_Count", 0);
        int category = getIntent().getIntExtra("Right_Answer_Count", 0);

        SharedPreferences settings = getSharedPreferences("memeups", Context.MODE_PRIVATE);
        int totalScore = settings.getInt("totalScore", 0);
        // totalScore+=score;

        resultLabel.setText("Your meme level is");

        if(category == 0) {
            totalScoreLabel.setText(level0);
        }
        else if(category == 1) {
            totalScoreLabel.setText(level1);
        }
        else if(category == 2) {
            totalScoreLabel.setText(level2);
        }
        else if(category == 3) {
            totalScoreLabel.setText(level3);
        }
        else if(category == 4) {
            totalScoreLabel.setText(level4);
        }
        //resultLabel.setText(score + " / 7");
        //totalScoreLabel.setText("Total Score: " + score);

        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("totalScore", totalScore);
        editor.commit();

        returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Listener for the return button. Sends the user back to the main page.
             * @param v
             */
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                startActivity(intent);

            }
        });

    }

}