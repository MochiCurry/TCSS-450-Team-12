package edu.tacoma.uw.css.sextod.memeups;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.tacoma.uw.css.sextod.memeups.database.Match;

public class MatchActivity2 extends AppCompatActivity implements MatchListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.top_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.memeupstopicon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
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
    public void onListFragmentInteraction(Match course) {

    }

}
