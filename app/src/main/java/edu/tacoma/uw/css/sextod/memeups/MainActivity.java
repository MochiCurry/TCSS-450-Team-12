package edu.tacoma.uw.css.sextod.memeups;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
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

public class MainActivity extends AppCompatActivity implements RegisterFragment.RegisterListener {
    private final static String LOGIN_URL
            = "http://kferg9.000webhostapp.com/android/login.php?";

    private Button signinbutton;
    private Button newuserbutton;
    private EditText registerEmail;
    private EditText registerPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.top_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.memeupstopicon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        signinbutton = findViewById(R.id.signinbutton);
        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openMainPage();

                registerEmail = (EditText) findViewById(R.id.registerEmail);
                registerPassword = (EditText) findViewById(R.id.registerPassword);

                String url = buildCourseURL(v);
                login(url);
            }
        });

        newuserbutton = (Button) findViewById(R.id.newuserbutton);
        newuserbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    }

    private class AddUserTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

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

    public void login(String url)
    {
        AddUserTask task = new AddUserTask();
        task.execute(new String[]{url.toString()});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void openMainPage() {
        Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);

    }

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
