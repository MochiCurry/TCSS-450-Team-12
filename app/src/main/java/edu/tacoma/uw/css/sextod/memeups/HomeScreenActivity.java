/**
 * This activity represents the main "home screen" of the MemeUps applicated, where the user is able
 * view the currently displayed meme, and choose between the different activities the application offers.
 *
 * 4 Buttons correspond to the Quiz, Find Matches, View Matches, and View Profile functionality.
 */

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
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * This class is the home page of the app where the users will be able to navigate to other
 * parts of the app. They will be able to access quiz, matching with people, view profiles
 * of matches, and to view and edit their profile.
 * @author Kerry Ferguson
 * @author Travis Bain
 * @author Dirk Sexton
 */
public class HomeScreenActivity extends AppCompatActivity {
    private Button mQuizButton;
    private Button mMatchFindButton;
    private Button mProfileButton;
    private Button mMatchViewButton;
    private ImageView mMemeImage;

    /* URL of the meme of the day
private String URL = "http://kferg9.000webhostapp.com/android/showDaily.php?id=1"; */
    private static final String URL = "https://qph.fs.quoracdn.net/main-qimg-2e3206445819b42a895ba8234a24ec71-c";
    private SharedPreferences mSharedPreferences;

    /**
     * Function for on creation of the activity. Sets up all the buttons.
     * @param savedInstanceState Saved state to be restored to
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        //Display the toolbar at the top with logo
        Toolbar toolbar = (Toolbar) findViewById(R.id.top_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.memeupstopicon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //Display the meme image using Picasso with the URL
        mMemeImage = findViewById(R.id.FEATURED_MEME);
        Picasso.get()
                .load(URL)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.memeupslogo)
                .into(mMemeImage);

        //Start shared preferences to remember if the user is logged in and what their email is
        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS)
                , Context.MODE_PRIVATE);

        getIntent().getIntExtra("loggedUser", 0);

        mQuizButton = findViewById(R.id.quizbutton);
        mQuizButton.setOnClickListener(new View.OnClickListener() {
            /**
             * On click, call method to open the quiz page.
             * @param v The view
             */
            @Override
            public void onClick(View v) {
                openQuizPage();
            }
        });

        mMatchFindButton = findViewById(R.id.matchbutton);
        mMatchFindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * On click, call method to open the match finding page.
             * @param v The View
             */
            public void onClick(View v) {
                openMatchFindingPage();
            }
        });

        mProfileButton = findViewById(R.id.profilebutton);
        mProfileButton.setOnClickListener(new View.OnClickListener() {
            /**
             * On click, call method to open the profile viewing page.
             * @param v The View
             */
            @Override
            public void onClick(View v) {
                openProfilePage();
            }
        });

        mMatchViewButton = findViewById(R.id.viewmatchbutton);
        mMatchViewButton.setOnClickListener(new View.OnClickListener() {
            /**
             * On click, call method to open the Match Viewing page.
             * @param v The View
             */
            @Override
            public void onClick(View v) {
                openViewMatchPage();
            }
        });

    }

    /**
     * onCreate for the options menu that will store the logout button
     * @param menu Menu that is being created
     * @return boolean for done creating
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu_course,menu); //logout button
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handle the logout button being selected
     * @param item Option that is selected
     * @return False to allow normal menu processing, true to handle it here
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //if option selected is logout, set sharedPreferences to logout
        if (item.getItemId() == R.id.action_logout) {
            SharedPreferences sharedPreferences =
                    getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), false)
                    .commit();

            //Start the login activity which is the beginning of the application
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();

        }
        return super.onOptionsItemSelected(item);

    }

    /**
     * Function to start the quiz activity.
     */
    public void openQuizPage() {
        Intent intent = new Intent(this, QuizActivity.class);
        startActivity(intent);
    }

    /**
     * Function to start the Match Finding activity
     */
    public void openMatchFindingPage() {

        //Set shared preferences to store that MatchActivity is beig run in Match Finding mode
        mSharedPreferences
                .edit()
                .putBoolean(getString(R.string.LOGGEDIN), true)
                .putString("listmode", "matchusers")
                .commit();

        Intent intent = new Intent(this, MatchActivity.class);
        startActivity(intent);
    }

    /**
     * Function to start the Match Viewing activity
     */
    public void openViewMatchPage() {
        //Set shared preferences to store that MatchActivity shoudl run in Match Viewing mode
        mSharedPreferences
                .edit()
                .putBoolean(getString(R.string.LOGGEDIN), true)
                .putString("listmode", "sentmatch")
                .commit();

        Intent intent = new Intent(this, MatchActivity.class);
        startActivity(intent);

    }

    /**
     * Function to start the Profile Page activity
     */
    public void openProfilePage() {
        Intent intent = new Intent(this, MyProfileActivity.class);
        startActivity(intent);

    }
}
