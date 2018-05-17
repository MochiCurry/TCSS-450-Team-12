/**
 * MainActivity is the greeting page when you start the application. It allows you to either input
 * an email address and a password if you are an existing user, which can be be submitted to login
 * using the sign in button, or you can click the registration button which launches a RegisterFragment
 * to allow the user to create a new account to be added to the database. The account credentials are
 * entered as a url to pass them into a php function which verifies the credentials against the user
 * database. If the email/password combination is valid, the user is taken to the main page.
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
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static android.content.ContentValues.TAG;

/**
 * This is the main activity that loads when you start the application. It implements RegisterListener
 * to start the Registeration fragment when the new user button is selected.
 */
public class MainActivity extends AppCompatActivity implements RegisterFragment.RegisterListener {
    //Constant for the login url and variables for buttons and fields
    private final static String LOGIN_URL
            = "http://kferg9.000webhostapp.com/android/login.php?";
    private Button signinbutton;
    private Button newuserbutton;
    private EditText registerEmail;
    private EditText registerPassword;
    private SharedPreferences mSharedPreferences; // for the device to remember login

    /**
     * This is the onCreate method that initializes values and starts the activity.
     * @param savedInstanceState Saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS)
                , Context.MODE_PRIVATE);
        if (!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {



            //Create our toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.top_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.memeupstopicon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //Set the sign in button
        signinbutton = findViewById(R.id.signinbutton);
        signinbutton.setOnClickListener(new View.OnClickListener() {
            /**
             * Listener for the sign in button
             * @param v The view
             */
            @Override
            public void onClick(View v) {
                //openMainPage();

                //Store the current values of the text fields
                registerEmail = (EditText) findViewById(R.id.registerEmail);
                registerPassword = (EditText) findViewById(R.id.registerPassword);

                //Pass in email and password to the url builder, and then call login function
                String url = buildCourseURL(v);
                login(url);
            }
        });

        //Set the register button
        newuserbutton = (Button) findViewById(R.id.newuserbutton);
        newuserbutton.setOnClickListener(new View.OnClickListener() {
            /**
             *Listener for the new user button
             * @param view The view
             */
            @Override
            public void onClick(View view) {
                //Get rid of elements from the Main Activity and start the RegisterFragment
                signinbutton.setVisibility(View.GONE);
                newuserbutton.setVisibility(View.GONE);
                registerEmail = (EditText) findViewById(R.id.registerEmail);
                registerEmail.setVisibility(View.GONE);
                registerPassword = (EditText) findViewById(R.id.registerPassword);
                registerPassword.setVisibility(View.GONE);
                RegisterFragment registerFragment = new RegisterFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, registerFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        } else {
            Intent i = new Intent(this, MainPageActivity.class);
            startActivity(i);
            finish();
        }

    }

    /**
     * Private class to handle asynchronous loading of data
     */
    private class AddUserTask extends AsyncTask<String, Void, String> {
        /**
         * PreExecute
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * This function reads and returns the response from the webpage.
         * @param urls url of the webpage
         * @return Returns the output from the webpage
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
                    response = "Unable to add course, Reason: "
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
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result
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
                            //On successful login, go to main page
                            openMainPage();
                } else {
                    Toast.makeText(getApplicationContext(), "Error: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Function to begin adding the new user asynchronously
     * @param url url to the php register function
     */
    @Override
    public void register(String url) {
        signinbutton.setVisibility(View.VISIBLE);
        newuserbutton.setVisibility(View.VISIBLE);
        registerEmail.setVisibility(View.VISIBLE);
        registerPassword.setVisibility(View.VISIBLE);
        AddUserTask task = new AddUserTask();
        task.execute(new String[]{url.toString()});

// Takes you back to the previous fragment by popping the current fragment out.
        getSupportFragmentManager().popBackStackImmediate();
    }

    /**
     * Function to begin logging in asynchronously
     * @param url to the php login verification function
     */
    public void login(String url)
    {
        mSharedPreferences
                .edit()
                .putBoolean(getString(R.string.LOGGEDIN), true)
                .commit();


        AddUserTask task = new AddUserTask();
        task.execute(new String[]{url.toString()});
    }

    /**
     * Creates options menu
     * @param menu
     * @return Returns boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Dropdown for options menu
     * @param item
     * @return Returns if the item is selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * Function to create an intent for the main page after logging in
     */
    public void openMainPage() {
        Intent intent = new Intent(this, MainPageActivity.class);
        startActivity(intent);

    }

    /**
     * Function to build the url using the email and password to pass into the php functions
     * @param v The view
     * @return Returns the url as a string
     */
    private String buildCourseURL(View v) {

        StringBuilder sb = new StringBuilder(LOGIN_URL);

        try {
            String email = registerEmail.getText().toString();
            sb.append("email=");
            sb.append(URLEncoder.encode(email, "UTF-8"));
            String password = registerPassword.getText().toString();
            sb.append("&password=");
            sb.append(URLEncoder.encode(password, "UTF-8"));


            //Log.i(TAG sb.toString());
            Log.i(TAG, sb.toString());

        }
        catch(Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }
}
