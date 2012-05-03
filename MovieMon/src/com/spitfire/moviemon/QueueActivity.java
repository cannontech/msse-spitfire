package com.spitfire.moviemon;

import com.spitfire.moviemon.data.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.view.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class QueueActivity extends ListActivity
{
    private ActionMode.Callback mActionModeCallback;
    private ProgressDialog progressDialog;
    private QueueActivity.MovieAdapter movieAdapter;
    private int _selectedItemIndex;
    private Member _member;
    private boolean watched;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);      
        setContentView(R.layout.list);
        Bundle extras = super.getIntent().getExtras();
        if (extras!=null){
            watched = extras.getString("watched") != null && extras.getString("watched").equals("true");
        }
        String title = "My Queue";

        if (watched) {
            title = "Watched Movies";
        }
        HeaderConfig headerConfig = new HeaderConfig(this, title);
               
        new QueueTask().execute();
      
        ListView lv = super.getListView();
        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            showMovieDetail(position);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            // Called when the user long-clicks on someView
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                startActionMode(mActionModeCallback);
                view.setSelected(true);
                _selectedItemIndex = position;

                return true;
            }
        });

        mActionModeCallback = new ActionMode.Callback() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate a menu resource providing context menu items
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.queue_menu, menu);

                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                boolean retVal = false;

                switch (item.getItemId()) {

                    case R.id.rate: {

                        showRating();
                        retVal = true;
                    }
                    break;

                    case R.id.remove: {
                        showRemove();
                        retVal = true;
                    }
                    break;

                    default:
                        break;
                }

                return retVal;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Here you can perform updates to the CAB due to
                // an invalidate() request
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Here you can make any necessary updates to the activity when
                // the CAB is removed. By default, selected items are deselected/unchecked.
            }
        };
    }

    private void showRating() {

        Intent intent = new Intent(this, UserRatingActivity.class);

        Movie selected = movieAdapter.getItem(_selectedItemIndex);
        intent.putExtra("selectedMovie", MovieMapper.toJson(selected));

        startActivity(intent);
    }

    private void showRemove() {

        Intent intent = new Intent(this, RemoveMovieFromQueue.class);

        Movie selected = movieAdapter.getItem(_selectedItemIndex);
        intent.putExtra("selectedMovie", MovieMapper.toJson(selected));

        startActivity(intent);
    }

    private void showMovieDetail(int position) {
        Movie selected = (Movie)this.getListAdapter().getItem(position);
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("selectedMovie", MovieMapper.toJson(selected));
        startActivity(intent);
    }

    private void updateList(List<Movie> movies) {
        // initialize the list view
        movieAdapter = new MovieAdapter(this, R.layout.list_item, movies);
        super.setListAdapter(movieAdapter);
    }

    private void updateMember(Member member) {

        _member = member;
    }

    private class QueueTask extends AsyncTask<String, Void, Member> {

        @Override
        protected void onPreExecute()  {
            super.onPreExecute();
            progressDialog = new ProgressDialog(QueueActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading Queue...");
            progressDialog.show();
        }
        
        @Override
        protected Member doInBackground(String... params) {

            MemberProxy proxy = new MemberProxy();

            try {
                return proxy.getDefaultMember();
            }
            finally {
                proxy.close();
            }
        }
        
        @Override
        protected void onPostExecute(Member result) {

            super.onPostExecute(result);
            ArrayList<Movie> movieList = new ArrayList<Movie>();

            for (Movie movie : result.getMovieQueue()) {

                if (watched) {
                    if (movie.getKey().getWasWatched()) {
                        movieList.add(movie);
                    }  
                }
                else {
                    if (!movie.getKey().getWasWatched()) {
                        movieList.add(movie);
                    }
                }
            }

            updateList(movieList);
            updateMember(result);
            progressDialog.cancel();
        }
    }

    /* We need to provide a custom adapter in order to use a custom list item view.
      */
    public class MovieAdapter extends ArrayAdapter<Movie> {

        public MovieAdapter(Context context, int textViewResourceId, List<Movie> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            Movie movie = getItem(position);
            View item = inflater.inflate(R.layout.list_item, parent, false);
            if (watched) {
                item = inflater.inflate(R.layout.watched_item, parent, false);
                if (movie.getKey() != null) {
                    ((RatingBar)item.findViewById(R.id.item_rating)).setRating(movie.getKey().getRating());
                    ((TextView)item.findViewById(R.id.item_comment)).setText(movie.getKey().getComment());
                    ((TextView)item.findViewById(R.id.watched_date)).setText(movie.getKey().getWatchedDateTime().toString());
                }
            }
            else {
                ((TextView)item.findViewById(R.id.item_mpaa)).setText(movie.getMPAARating());
                ((TextView)item.findViewById(R.id.item_runtime)).setText(movie.getRunTime());
            }

            ((TextView)item.findViewById(R.id.item_title)).setText(movie.getTitle());
            if (movie.getRelatedImages() != null && movie.getRelatedImages().size() > 0)
            {
                String imgUrl = movie.getRelatedImages().get(0).getUrl();
                try
                {
                    ((ImageView)item.findViewById(R.id.item_cover)).setImageDrawable(Drawable.createFromStream((InputStream) new URL(imgUrl).getContent(), "src"));
                }
                catch (Exception e)
                {
                    Log.e("IMAGE", e.toString());
                }
            }
            return item;
        }
    }
}