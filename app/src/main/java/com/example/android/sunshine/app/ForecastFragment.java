package com.example.android.sunshine.app;

import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.sunshine.app.data.WeatherContract;

import static android.widget.CursorAdapter.FLAG_AUTO_REQUERY;

/**
 * Encapsulates fetching the forecast and displaying it as a {@link ListView} layout.
 */
public class ForecastFragment extends Fragment {

    private ForecastAdapter mForecastAdapter;

    static public final String INTENT_DATA_KEY = "PositionInList";
    static public final String PREFS_NAME = "pref_general";

    class SettingsObserver extends ContentObserver {
        SettingsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            // if changes occurred in either of the watched Uris updateSettings()
            // updateSettings() is a normal void method that will be called to handle all changes
            // or you could just do your work here but presumable the same work will need to be done
            // on load of your class as well... but you get the picture

            String locationSetting = Utility.getPreferredLocation(getActivity());

            // Sort order:  Ascending, by date.
            String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
            Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(locationSetting, System.currentTimeMillis());
            Cursor cur = getActivity().getContentResolver().query(weatherForLocationUri, null, null, null, sortOrder);

            Log.i("blablaaaaa", "mmmmmm", null);
            mForecastAdapter.swapCursor(cur);

        }
    }


    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public void onStart(){
        super.onStart();
        updateWeather();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        String locationSetting = Utility.getPreferredLocation(getActivity());

        // Sort order:  Ascending, by date.
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                locationSetting, System.currentTimeMillis());

        Cursor cur = getActivity().getContentResolver().query(weatherForLocationUri, null, null, null, sortOrder);
        SettingsObserver observer = new SettingsObserver(new Handler());
        cur.registerContentObserver(observer);

        // The CursorAdapter will take data from our cursor and populate the ListView
        // However, we cannot use FLAG_AUTO_REQUERY since it is deprecated, so we will end
        // up with an empty list the first time we run.
        mForecastAdapter = new ForecastAdapter(getActivity(), cur, 0);


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);

        /*
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        CharSequence text = mForecastAdapter.getItem(position);
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(getActivity(), text, duration);
                        toast.show();

                        Intent myIntent = new Intent(getActivity(), DetailActivity.class);
                        myIntent.putExtra(Intent.EXTRA_TEXT, text);
                        startActivity(myIntent);
                    }
                }
        );*/



        return rootView;
    }

    public void updateWeather()
    {

        //FetchWeatherTask weatherTask = new FetchWeatherTask();
        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity());
        // Restore preferences
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        //R.string.pref_location_key

        String prefLocKey = getActivity().getString(R.string.pref_location_key);
        String prefLocDefaultValue = getActivity().getString(R.string.pref_location_default_value);

        String cityLocation = PreferenceManager
                .getDefaultSharedPreferences(getActivity())
                .getString(prefLocKey, prefLocDefaultValue);


        // ( "pref_location_key", false);

        Toast.makeText(getActivity(), cityLocation, Toast.LENGTH_SHORT).show();
        weatherTask.execute(cityLocation);

    }

// Ici les méthodes du loaderListner.






}