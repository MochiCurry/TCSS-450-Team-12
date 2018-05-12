package edu.tacoma.uw.css.sextod.memeups;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView resultLabel = (TextView) findViewById(R.id.resultLabel);
        TextView totalScoreLabel = (TextView) findViewById(R.id.totalScoreLabel);

        int score = getIntent().getIntExtra("Right_Answer_Count", 0);

        SharedPreferences settings = getSharedPreferences("memeups", Context.MODE_PRIVATE);
        int totalScore = settings.getInt("totalScore", 0);
        totalScore+=score;

        resultLabel.setText(score + " / 7");
        totalScoreLabel.setText("Total Score: " + score);

        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("totalScore", totalScore);
        editor.commit();




    }

}
