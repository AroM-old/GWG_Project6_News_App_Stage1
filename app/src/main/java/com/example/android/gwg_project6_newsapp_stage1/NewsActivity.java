package com.example.android.gwg_project6_newsapp_stage1;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    /**
     * Constant value for the news logs
     */
    private static final String LOG_TAG = NewsActivity.class.getSimpleName();

    /**
     * URL for news data from the Guardian API
     */
    private static final String REQUEST_URL = "https://content.guardianapis.com/search?";

    /**
     * Constant value for the news loader ID
     */
    private static final int LOADER_ID = 1;

    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST: News Activity onCreate() called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        //Find a reference to the {@link ListView} in the layout
        ListView lv = findViewById(R.id.list);

        // Display message when not news are available
        LinearLayout lvIsEmpty = findViewById(R.id.isEmpty);
        lv.setEmptyView(lvIsEmpty);

        // Create a new {@link ArrayAdapter}of News Articles
        adapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        lv.setAdapter(adapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected news.
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Find the current news that was clicked on
                News news = adapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri uriNews = Uri.parse(news.getWebsiteLink());

                // Create a new intent to view the news URI
                Intent webSite = new Intent(Intent.ACTION_VIEW, uriNews);

                // Send the intent to launch a new activity
                startActivity(webSite);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            //Check for log activity
            Log.i(LOG_TAG, "TEST: News Activity initLoader called");

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            setEmptyStatus(R.string.notInternet);
        }

    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "TEST: News Activity onCreateLoader() called");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String sectionNews = sharedPreferences.getString(getString(R.string.settings_section_key), getString(R.string.settings_section_default));

        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", sectionNews);
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("show-fields", "headline, trailText, thumbnail, byline");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("order-by", "newest");
        uriBuilder.appendQueryParameter("api-key", "8a8cf61f-07ec-422f-a722-96d9a5b336bc");

        Log.i(LOG_TAG, "................TEST URL constructor: " + uriBuilder.toString());
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        Log.i(LOG_TAG, "TEST: News Activity onLoadFinished() called");

        // Hide loading indicator because the data has been loaded
        setEmptyStatus(R.string.emptyScreen);

        /** If there is a valid list of {@link News}s, then add them to the adapter's
         * data set. This will trigger the ListView to update.
         */
        adapter.clear();
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);

        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        Log.i(LOG_TAG, "TEST: News Activity onLoaderReset() called");
        adapter.clear();
    }

    // Display empty status information
    private void setEmptyStatus(int resource) {
        View loadIndicator = findViewById(R.id.progressBar);
        loadIndicator.setVisibility(View.GONE);

        // Set text on view
        ImageView imageView = findViewById(R.id.isEmpty_imageView);
        imageView.setVisibility(View.VISIBLE);
        TextView emptyState = findViewById(R.id.isEmpty_textView);
        emptyState.setText(resource);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}