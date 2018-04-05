package edu.tacoma.uw.css.kferg9.aboutme2;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final String TAB = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Log.d(TAG, "onCreate: started");
       // ImageView firstImage = (ImageView) findViewById(R.id.image_view_id);

       // int imageResource = getResources().getIdentifier("@drawable/thaiCurry", null, this.getPackageName());
       // firstImage.setImageResource(imageResource);
    }

    public void showText(View view) {

        Intent intent = new Intent(this, DisplayTextActivity.class);
        startActivity(intent);
    }
    public void showImage(View view) {
        Intent intent = new Intent(this, DisplayImageActivity.class);
        startActivity(intent);
    }
    public void showWeb(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://developer.android.com/index.html"));
        startActivity(intent);
    }
    public void showToast(View view) {
        String str = "eat my curry";
        Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        toast.show();
    }
    public void showDialog(View view) {
        // Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Do you like curry?")
                .setTitle("eat my curry");

        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        // Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}


