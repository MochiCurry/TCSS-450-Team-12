package edu.tacoma.uw.css.sextod.memeups;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ProfileActivity extends AppCompatActivity implements ProfileEditFragment.CourseAddListener
{

    private Button collectionbutton;
    private EditText registerBio;
    private ImageView profilePic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.top_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.memeupstopicon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


//        profilePic = findViewById(R.id.profilePic);


        registerBio = (EditText) findViewById(R.id.biotext);
        profilePic = (ImageView) findViewById(R.id.profilePic);

        collectionbutton = findViewById(R.id.collectionbutton);
        collectionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //clear the elements in activity
                collectionbutton.setVisibility(View.GONE);
                registerBio.setVisibility(View.GONE);
                profilePic.setVisibility(View.GONE);


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


}