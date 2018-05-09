package edu.tacoma.uw.css.sextod.memeups;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfileActivity extends AppCompatActivity {

    private Button collectionbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        collectionbutton = findViewById(R.id.collectionbutton);
        collectionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCollectionPage();
            }
        });
    }

    public void openCollectionPage() {
        Intent intent = new Intent(this, CollectionActivity.class);
        startActivity(intent);

    }
}