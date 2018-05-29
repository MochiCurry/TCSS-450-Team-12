package edu.tacoma.uw.css.sextod.memeups;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class HomeScreenActivity extends AppCompatActivity {
    private Button quizbutton;
    private Button matchbutton;
    private Button profilebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.top_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.memeupstopicon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        getIntent().getIntExtra("loggedUser", 0);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu_course,menu); //logout button
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            SharedPreferences sharedPreferences =
                    getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), false)
                    .commit();

            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();

        }
        return super.onOptionsItemSelected(item);

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
