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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
 * Class to display the results of the quiz
 * author: Kerry Ferguson
 */
public class ResultActivity extends AppCompatActivity {

    private final static String RESULT_ADD_URL
            = "http://kferg9.000webhostapp.com/android/updateUser.php?cmd=quizresult";

    private Button returnButton;
    private Button shareButton;
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

        //build string
        StringBuilder sb = new StringBuilder(RESULT_ADD_URL);

        try {
            SharedPreferences mLoginEmail = getSharedPreferences(getString(R.string.LOGIN_PREFS)
                    , Context.MODE_PRIVATE);

            String email = mLoginEmail.getString("email", "");
//            Log.i(TAG, "email should be here");
//            Log.i(TAG, email);


            sb.append("&email=");
            sb.append(URLEncoder.encode(email, "UTF-8"));
            sb.append("&score=");
            sb.append(category);


            //Log.i(TAG sb.toString());
            Log.i(TAG, sb.toString());

        }
        catch(Exception e) {
//            Toast.makeText(view.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
//                    .show();
        }

        //should have string now, try updating result
        AddUserTask task = new AddUserTask();
        task.execute(new String[]{sb.toString()});



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
        
        shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Listener for the return button. Sends the user back to the main page.
             * @param v
             */
            @Override
            public void onClick(View v) {

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, "My meme level is " + totalScoreLabel.getText() + " on MemeUps!");

                startActivity(Intent.createChooser(share, "Choose where you want to share"));

            }
});

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
//        @Override
//        protected void onPostExecute(String result) {
//            // Something wrong with the network or the URL.
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                String status = (String) jsonObject.get("result");
//                if (status.equals("success")) {
//                    Toast.makeText(getApplicationContext(), "Success!"
//                            , Toast.LENGTH_LONG)
//                            .show();
//                    //On successful login, go to main page
//                    //openMainPage();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Error: "
//                                    + jsonObject.get("error")
//                            , Toast.LENGTH_LONG)
//                            .show();
//                }
//            } catch (JSONException e) {
//                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
//                        e.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        }
    }

}
