package edu.tacoma.uw.css.sextod.memeups;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainPage extends AppCompatActivity {
    private Button quizbutton;
    private Button matchbutton;
    private Button profilebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);


        quizbutton = findViewById(R.id.quizbutton);
        quizbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQuizPage();
            }
        });

        matchbutton = findViewById(R.id.matchbutton);
        matchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMatchPage();
            }
        });

        profilebutton = findViewById(R.id.profilebutton);
        profilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfilePage();
            }
        });
    }

    public void openQuizPage() {
        Intent intent = new Intent(this, QuizActivity.class);
        startActivity(intent);

    }

    public void openMatchPage() {
        Intent intent = new Intent(this, MatchActivity.class);
        startActivity(intent);

    }

    public void openProfilePage() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);

    }
}
