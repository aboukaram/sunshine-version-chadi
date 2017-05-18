package com.example.android.sunshine.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailFragment.DETAIL_URI, getIntent().getData());

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.weather_detail_container, fragment)
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_detail, menu);

        /*
        // program the sharing bit.
        MenuItem shareItem = menu.findItem(R.id.action_share);
        ShareActionProvider myShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        Intent myShareIntent = new Intent(Intent.ACTION_SEND);
        myShareIntent.setType("text/plain");
        String TextToShare = getIntent().getDataString();

        myShareIntent.putExtra(Intent.EXTRA_TEXT, TextToShare);
        myShareActionProvider.setShareIntent(myShareIntent);
*/


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "yahooo2!!!", duration);
            toast.show();

            startActivity(new Intent(this, SettingsActivity.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
