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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import edu.tacoma.uw.css.sextod.memeups.database.Match;

import static android.content.ContentValues.TAG;

public class ProfileActivity extends AppCompatActivity implements ProfileEditFragment.CourseAddListener
{
    private final static String GET_USER_URL
            = "http://kferg9.000webhostapp.com/android/list.php?cmd=singleuser";



    private Button collectionbutton;
    private TextView registerBio;
    private TextView aboutMe;
    private TextView favoriteMeme;
    private ImageView profilePic;
    private ImageView memePic;
    private Match mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.top_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.memeupstopicon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        try
        {
            StringBuilder sb = new StringBuilder(GET_USER_URL);

            SharedPreferences mLoginEmail = getSharedPreferences(getString(R.string.LOGIN_PREFS)
                    , Context.MODE_PRIVATE);

            String email = mLoginEmail.getString("email", "");
            sb.append("&email=");
            sb.append(URLEncoder.encode(email, "UTF-8"));

            Log.i(TAG, sb.toString());

            CourseAsyncTask courseAsyncTask = new CourseAsyncTask();
            courseAsyncTask.execute(new String[]{sb.toString()});


        }catch(Exception e) {
//            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
//                    .show();
        }

//        profilePic = findViewById(R.id.profilePic);


        registerBio = (TextView) findViewById(R.id.biotext);
        aboutMe = (TextView) findViewById(R.id.textView3);
        favoriteMeme = (TextView) findViewById(R.id.textView6);


        profilePic = (ImageView) findViewById(R.id.profilePic);
        memePic = (ImageView) findViewById(R.id.memePic);

        collectionbutton = findViewById(R.id.collectionbutton);
        collectionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //clear the elements in activity
                collectionbutton.setVisibility(View.GONE);
                registerBio.setVisibility(View.GONE);
                profilePic.setVisibility(View.GONE);
                memePic.setVisibility(View.GONE);
                aboutMe.setVisibility(View.GONE);
                favoriteMeme.setVisibility(View.GONE);


                ProfileEditFragment profileEditFragment = new ProfileEditFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, profileEditFragment)
                        .addToBackStack(null)
                        .commit();

                // openCollectionPage();
            }
        });

    }

    public void openCollectionPage() {
        Intent intent = new Intent(this, CollectionActivity.class);
        startActivity(intent);

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
    public void addCourse(String url) {

    }

    private class CourseAsyncTask extends AsyncTask<String, Void, String> {
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
                    response = "Unable to download the list of user matches, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "onPostExecute");

            if (result.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            try {
                mUser = Match.parseUserJSON(result);
                if (mUser != null) {
                    Log.i(TAG, "mUser not null");
//            mCourseIdTextView.setText(course.getCourseId());
//            mCourseShortDescTextView.setText(course.getShortDescription());
                    registerBio.setText(mUser.getmBio());
                    Log.i(TAG, mUser.getDISPLAY());

                    //bad urls so the url fields are not empty, else picasso will crash
                    String displayUrl = "i", memeUrl = "i";

                    //display profile image
                    if(!mUser.getDISPLAY().isEmpty())
                    {
                        displayUrl = mUser.getDISPLAY();
                    }
                    if(!mUser.getMEME().isEmpty())
                    {
                        memeUrl = mUser.getMEME();
                    }
                    Picasso.get()
                            .load(displayUrl)
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.memeupslogo)
                            .into(profilePic);
                    Picasso.get()
                            .load(memeUrl)
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.memeupslogo)
                            .into(memePic);
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                return;
            }
        }
    }


}