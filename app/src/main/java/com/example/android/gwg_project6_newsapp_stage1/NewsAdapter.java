package com.example.android.gwg_project6_newsapp_stage1;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsAdapter extends ArrayAdapter<News> {

    // Set constant variable for logs
    private static final String LOG_TAG = NewsAdapter.class.getSimpleName();

    public NewsAdapter(Activity context, ArrayList<News> news){
        super(context, 0 , news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if the view is being reused, otherwise inflate the view
        View listItemView = convertView;

        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.news_articles_list_item,parent,false);

            // Set background color to views
            if (position % 2 == 1){
                listItemView.setBackgroundResource(R.color.primaryLightColor);
            }else {
                listItemView.setBackgroundResource(R.color.secondaryLightColor);
            }
        }

        //Get newsArticles with position and stores in the variable
        News newsArticles = getItem(position);

        // Find the news thumbnail image with ID thumbnail_imageview
        ImageView imageThumbnail = listItemView.findViewById(R.id.thumbnail_imageview);
        Picasso.get().load(newsArticles.getThumbnailImage())
                .resize(300,300)
                .into(imageThumbnail);

        //Find the news section name with ID sectionName_textview
        TextView sectionName = listItemView.findViewById(R.id.sectionName_textview);
        sectionName.setText(newsArticles.getSectionName());

        //Find the news title with ID title_textview
        TextView headTitle = listItemView.findViewById(R.id.title_textview);
        headTitle.setText(newsArticles.getHeadTitle());

        //Find the news short content with ID shortContent_textview
        TextView shortDesc = listItemView.findViewById(R.id.shortContent_textview);
        shortDesc.setText(newsArticles.getShortDesc());

        //Find the news author name with ID authorName_textview
        TextView authorName = listItemView.findViewById(R.id.authorName_textview);
        authorName.setText(newsArticles.getAuthorName());

        // Find the news publication date with ID publishDate_textview
        // and format the date to a simple format
        TextView publicationDate = listItemView.findViewById(R.id.publishDate_textview);
        String newsDate = newsArticles.getPublicationDay();
        // New simple date format
        DateFormat guardianDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        try {
            Date newsDateParse = guardianDate.parse(newsDate);
            String newsDateString = formatDate(newsDateParse);
            publicationDate.setText(newsDateString);

        } catch (ParseException e) {
            Log.i(LOG_TAG, "Error formatting date.");
        }
        // Return populate ListView
        return listItemView;
    }

    /**
    *  Return the formatted date string (i. e. Jul 06, 2009)
    */
    private String formatDate(Date dateObject){
        DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
        return dateFormat.format(dateObject);
    }
}
