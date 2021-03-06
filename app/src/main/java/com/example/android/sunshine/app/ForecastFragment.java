package com.example.android.sunshine.app;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.sunshine.app.data.WeatherContract;
import com.example.android.sunshine.app.services.SunshineService;

/**
 * Encapsulates fetching the forecast and displaying it as a {@link ListView} layout.
 */
public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri);
    }

    // For the forecast view we're showing only a small subset of the stored data.
    // Specify the columns we need.
    private static final String[] FORECAST_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_COORD_LAT,
            WeatherContract.LocationEntry.COLUMN_COORD_LONG
    };

    // These indices are tied to FORECAST_COLUMNS.
    // If FORECAST_COLUMNS changes, these must change.

    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE = 1;
    static final int COL_WEATHER_DESC = 2;
    static final int COL_WEATHER_MAX_TEMP = 3;
    static final int COL_WEATHER_MIN_TEMP = 4;
    static final int COL_LOCATION_SETTING = 5;
    static final int COL_WEATHER_CONDITION_ID = 6;
    static final int COL_COORD_LAT = 7;
    static final int COL_COORD_LONG = 8;


    private static final int FORECAST_LOADER = 0;
    private ForecastAdapter mForecastAdapter;
    private static final String SELECTED_KEY = "selected_position";
    private int mPosition = ListView.INVALID_POSITION;
    private ListView mListView;
    private boolean mUseTodayLayout;


    static public final String INTENT_DATA_KEY = "PositionInList";
    static public final String PREFS_NAME = "pref_general";



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

        if (id == R.id.action_refresh)
        {
            updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        // The CursorAdapter will take data from our cursor and populate the ListView
        mForecastAdapter = new ForecastAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) rootView.findViewById(R.id.listview_forecast);
        mListView.setAdapter(mForecastAdapter);


        // We'll call our MainActivity
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);

                if (cursor != null)
                {
                    String locationSetting = Utility.getPreferredLocation(getActivity());

                    Uri targetUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
                            locationSetting, cursor.getLong(COL_WEATHER_DATE)
                    );

                    ((Callback) getActivity()).onItemSelected(targetUri);
                }

                mPosition = position;
            }
        });


        // s'il y a une position sauvegardée, la restaurer dans mPosition.
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    // 1. récupérer la location.
    // 2. lancer l'asynctask.
    public void updateWeather() // elle synchronize seulement la base locale.
    {
        /*
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

        //FetchWeatherTask weatherTask = new FetchWeatherTask();
        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity());
        weatherTask.execute(cityLocation);
        */

        SunshineService.startActionFoo(this.getActivity(),"bennn", "binnggg");

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String locationSetting = Utility.getPreferredLocation(getActivity());

        // Sort order:  Ascending, by date.
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                locationSetting, System.currentTimeMillis());

        Loader<Cursor> chiLoader = new CursorLoader(
                        getActivity(),
                        weatherForLocationUri,
                        FORECAST_COLUMNS,
                        null,
                        null, sortOrder
                );

        return chiLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mForecastAdapter.swapCursor(data);

        if (mPosition != ListView.INVALID_POSITION)
        {
            mListView.setItemChecked(mPosition, true);
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mForecastAdapter.swapCursor(null);
    }

    // since we read the location when we create the loader, all we need to do is restart things
    public void onLocationChanged( ) {
        updateWeather();
        getLoaderManager().restartLoader(FORECAST_LOADER, null, this);
    }

// Ici les méthodes du loaderListner.

    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    public void setUseTodayLayout(boolean useTodayLayout) {
        mUseTodayLayout = useTodayLayout;
        if (mForecastAdapter != null) {
            mForecastAdapter.setUseTodayLayout(mUseTodayLayout);
            //mForecastAdapter.notifyDataSetChanged();
        }
    }



}