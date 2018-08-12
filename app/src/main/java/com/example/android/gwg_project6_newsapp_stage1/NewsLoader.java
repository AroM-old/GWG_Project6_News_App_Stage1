package com.example.android.gwg_project6_newsapp_stage1;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Loads a list of news using the AsyncTask
 */
public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private static final String LOG_TAG = NewsLoader.class.getSimpleName();

    //Query URL
    private String url;

    /**
     * Construct a new {@link NewsLoader}.
     * @param context of the activity
     * @param url to laod data from
     */
    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "TEST: News Activity onStartLoading() called");
        forceLoad();
    }

    // Background thread
    @Override
    public List<News> loadInBackground() {
        Log.i(LOG_TAG, "TEST: News Activity loadInBackground() called");
        if (url == null){
            return null;
        }

        // Network request to extract list of News Articles
        List<News> news = QueryUtils.fetchNewsData(url);
        return news;
    }
}
