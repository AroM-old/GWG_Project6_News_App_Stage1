package com.example.android.gwg_project6_newsapp_stage1;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to request and receiving news data from The Guardian API
 */
public class QueryUtils {

    /**
     * TAG for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create private constructor. No one must create an object from this class.
     * This Class are only helper methods that hold static variables and methods
     */
    private QueryUtils() {
    }

    /**
     * Query the guardian and return a list of objects.
     */
    public static List<News> fetchNewsData(String requestUrl) {
        Log.i(LOG_TAG, "TEST: News Activity fet() called");

        //Create URL new object
        URL url = createURL(requestUrl);

        //HTTP request to the URL and receive JSON response
        String jsonResponse = null;
        try {
            jsonResponse = makeHTTPTRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.");
        }

        // Extract fields from JSON and create a list of {@link News}
        List<News> newsArticles = extractFeatureFromJson(jsonResponse);

        // Return list of {@link News}
        return newsArticles;
    }

    /**
     * Return URL object from String url
     */
    private static URL createURL(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building URL", e);
        }
        return url;
    }

    /**
     * Perform an HTTP request to the given URL and return a String
     */

    private static String makeHTTPTRequest(URL url) throws IOException {
        String jsonResponse = "";

        //If url null then return early
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(1500 /* milliseconds */);
            urlConnection.connect();

            /**
             * if request was successful (code 200) then
             * read input stream and parse response
             */
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Guardian JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            /**
             *  Closing the input stream could throw an IOException, which is why
             *  the makeHttpRequest(URL url) method signature specifies than an IOException
             *  could be thrown.
             */
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractFeatureFromJson(String newsArticleJSON) {

        // if the JSON string is empty return early
        if (TextUtils.isEmpty(newsArticleJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        List<News> newsList = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject rootJsonResponse = new JSONObject(newsArticleJSON);

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of results (or news).
            JSONArray newsArray = rootJsonResponse.getJSONObject("response").getJSONArray("results");

            // For each news in the newsArray, create an {@link News} object
            for (int i = 0; i < newsArray.length(); i++) {

                // Get a single news at position i within the list of news
                JSONObject currentNews = newsArray.getJSONObject(i);

                // Extract the value for the key called "webTitle"
                String title = currentNews.getString("webTitle");

                // Extract the value for the key called "webUrl"
                String webUrl = currentNews.getString("webUrl");

                // Extract the value for the key called "sectionName"
                String sectionName = currentNews.getString("sectionName");

                // Extract the value for the key called "webPublicationDate"
                String originalPublicationDate = currentNews.getString("webPublicationDate");

                //Extract the value of the key called fields
                String thumbnail = currentNews.getJSONObject("fields").getString("thumbnail");

                //Extract the value of the key called fields
                String trailText = currentNews.getJSONObject("fields").getString("trailText");

                //Extract the value of the key called tags
                JSONArray tagsArray = currentNews.getJSONArray("tags");
                if (tagsArray.length() >= 1) {
                    for (int tagIndex = 0; tagIndex < 1; tagIndex++) {
                        JSONObject tagsObject = tagsArray.getJSONObject(tagIndex);
                        String authorFullName = tagsObject.getString("webTitle");


                        // Create a new {@link News} object with the title, section name, publication date,
                        // and url from the JSON response.
                        News news = new News(thumbnail, sectionName, title, trailText, authorFullName, originalPublicationDate, webUrl);
                        newsList.add(news);
                    }
                }

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }

        return newsList;
    }

}
