package com.spitfire.moviemon;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.spitfire.moviemon.data.Movie;
import com.spitfire.moviemon.data.MovieMapper;
import com.spitfire.moviemon.data.Review;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class ReviewListActivity extends ListActivity {
    private Movie movie;

    private ProgressDialog progressDialog;
    private ReviewListActivity.ReviewAdapter reviewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);      
        setContentView(R.layout.list);
        
        Bundle extras = super.getIntent().getExtras();


        ListView lv = super.getListView();
        lv.setTextFilterEnabled(true);
        movie = MovieMapper.movieFromJson(extras.getString("selectedMovie"));
        HeaderConfig headerConfig = new HeaderConfig(this, movie.getTitle() + " Reviews");
        updateList(movie.getReviews());

    }



    private void updateList(List<Review> reviews)
    {
        // initialize the list view
        reviewAdapter = new ReviewAdapter(this, R.layout.review_list_item, reviews);
        super.setListAdapter(reviewAdapter);
    }
    /* We need to provide a custom adapter in order to use a custom list item view.
      */
    public class ReviewAdapter extends ArrayAdapter<Review> {

        public ReviewAdapter(Context context, int textViewResourceId, List<Review> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View item = inflater.inflate(R.layout.review_list_item, parent, false);

            Review review = getItem(position);
            ((TextView)item.findViewById(R.id.item_provider)).setText(review.getReviewProviderName());
            ((TextView)item.findViewById(R.id.item_critic)).setText(review.getCritic());
            ((TextView)item.findViewById(R.id.item_comment)).setText(review.getComment());

            return item;
        }
    }
    
}