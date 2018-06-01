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
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.tacoma.uw.css.sextod.memeups.database.Match;

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
    private static final String MEME_URL = "http://kferg9.000webhostapp.com/android/list.php?cmd=meme&id=1";
    private SharedPreferences mSharedPreferences;
    private String mMemeUrl;

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

        MemeAsyncTask memeAsyncTask = new MemeAsyncTask();
        memeAsyncTask.execute(MEME_URL);


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
     * Called at the end of async task after parsing for meme URL, loads the meme into view
     */
    public void updateMeme()
    {
        Picasso.get()
                .load(mMemeUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.memeupslogo)
                .into(mMemeImage);
    }

    /**
     * Function to start the Profile Page activity
     */
    public void openProfilePage() {
        Intent intent = new Intent(this, MyProfileActivity.class);
        startActivity(intent);

    }
    private class MemeAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to download the meme, Reason: "
                            + e.getMessage();
                }
                finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }
        @Override
        protected void onPostExecute(String result) {
            //Log.i(TAG, "onPostExecute");

            if (result.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            try {
                JSONArray arr = new JSONArray(result);
                JSONObject obj = arr.getJSONObject(0);
                mMemeUrl = obj.getString("url");

                updateMeme();
            }
            catch (JSONException e) {
                if (e.getMessage().contains("End of input at character 0 of")) {
                    Toast.makeText(getApplicationContext(), "No meme found.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT)
                            .show();
                }
                return;
            }
        }

    }
}
