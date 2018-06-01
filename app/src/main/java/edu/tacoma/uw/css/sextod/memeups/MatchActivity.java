/**
 * MatchActivity handles both finding and viewing matches separately. Depending on the mode stored in
 * SharedPreferences, the list is populated (using MatchListFragment) with either potential users to match
 * with, or with users you have already matched with. These users can be selected to view their profile
 * using ProfileViewFragment.
 */

package edu.tacoma.uw.css.sextod.memeups;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import edu.tacoma.uw.css.sextod.memeups.database.Match;

import static android.content.ContentValues.TAG;

/**
 * This activity will display the people the users are capable of matching
 * with depending on their quiz score. It will only show people with the same
 * category of score. Alternatively, this activity can also display the users
 * that you have currently matched with, in order to message them.
 * @author Kerry Ferguson
 * @author Travis Bain
 * @author Dirk Sexton
 */
public class MatchActivity extends AppCompatActivity implements
        MatchListFragment.OnListFragmentInteractionListener,
        ProfileViewFragment.MatchListener,
        EmailFragment.EmailListener {

    //Initial url to be processed, will be followed by the command
    private final static String MATCH_URL
            = "http://kferg9.000webhostapp.com/android/addMatch.php?";

    /**
     * Handles match requesting in the ViewProfile fragment. Accepts an email and builds the url
     * using that email to add the match into the database using a PHP file.
     * @param email email to be used for url to find the UserID to be matched to.
     */
    @Override
    public void matchRequest(String email)
    {
        //match with given email
        StringBuilder sb = new StringBuilder(MATCH_URL);

        //Try to create a match by building and running the URL
        try {
            //Use SharedPreferences to get the current user's email
            SharedPreferences mLoginEmail = getSharedPreferences(getString(R.string.LOGIN_PREFS)
                    , Context.MODE_PRIVATE);

            //Insert the user's email, followed by the match's email
            String email1 = mLoginEmail.getString("email", "");
            sb.append("email1=");
            sb.append(URLEncoder.encode(email1, "UTF-8"));
            sb.append("&email2=");
            sb.append(URLEncoder.encode(email, "UTF-8"));


            //Log.i(TAG sb.toString());
            Log.i(TAG, sb.toString());

            //Attempt the add the match.
            addMatch(sb.toString());
        }
        catch(Exception e) {
            //Toast.makeText(view.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    //.show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        Toolbar toolbar = (Toolbar) findViewById(R.id.top_bar);
        //setSupportActionBar(toolbar);

       // Toolbar toolbar = (Toolbar) findViewById(R.id.top_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.memeupstopicon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        MatchListFragment courseListFragment = new MatchListFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, courseListFragment)
                .commit();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(Match user) {
        ProfileViewFragment profileViewFragment = new ProfileViewFragment();
        Bundle args = new Bundle();
        args.putSerializable(ProfileViewFragment.COURSE_ITEM_SELECTED, user);
        profileViewFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, profileViewFragment)
                .addToBackStack(null)
                .commit();

    }

    public void onEmailListener(String email)
    {
        EmailFragment emailFragment = new EmailFragment();



        Bundle args = new Bundle();
        args.putSerializable("sendEmail", email);
        emailFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, emailFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Function to add a match, starts the task to execute the URL
     * @param url URL to be executed
     */
    public void addMatch(String url) {

        AddMatchTask task = new AddMatchTask();
        task.execute(new String[]{url.toString()});

// Takes you back to the previous fragment by popping the current fragment out.
        //getSupportFragmentManager().popBackStackImmediate();
    }


    /**
     * AsyncTask to handle adding a match in the background using the given URL
     */
    private class AddMatchTask extends AsyncTask<String, Void, String> {
        /**
         * On pre execute before handing the URL
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Attemps to run the URL in the background and receives a response.
         * @param urls URL to be handled for creating a match
         * @return The result receieved by the webpage after the URL is executed
         */
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
                    response = "Unable to add match, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. If not, display a success message
         *
         * @param result Result receieved from the URL
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Toast.makeText(getApplicationContext(), "Success!"
                            , Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to add: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                //This error is given if you try to create a match that already exists (primary key)
                if (e.getMessage().contains("End of input at character 0 of")) {
                    Toast.makeText(getApplicationContext(), "You already matched with this user!", Toast.LENGTH_LONG).show();
                } else
                {
                    Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                            e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }
    }
}
