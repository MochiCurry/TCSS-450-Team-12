package edu.tacoma.uw.css.sextod.memeups;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * This activity will open on button click from ProfileViewFragment. It is used to open up the
 * SMS app so that the users can communicate with each other.
 *
 * @author Kerry Ferguson
 * @author Travis Bain
 * @author Dirk Sexton
 */
public class SMSActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        Button startBtn = (Button) findViewById(R.id.button);
        startBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendSMS();
            }
        });
    }

    /**
     * To open up the new intent that contains the SMS app.
     */
    protected void sendSMS() {
        Log.i("Send SMS", "");
//        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
//
//        smsIntent.setData(Uri.parse("smsto:"));
//        smsIntent.setType("vnd.android-dir/mms-sms");
//        smsIntent.putExtra("address"  , new String ("01234"));
//        smsIntent.putExtra("sms_body"  , "Test ");

        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));

        try {
            startActivity(sendIntent);
            finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SMSActivity.this,
                    "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }
}