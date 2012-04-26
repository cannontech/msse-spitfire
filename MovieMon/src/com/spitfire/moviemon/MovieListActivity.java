package com.spitfire.moviemon;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import android.net.Uri;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.spitfire.moviemon.data.Movie;
import com.spitfire.moviemon.data.MovieMapper;

public class MovieListActivity extends ListActivity
{
    private String URL_BASE = "http://movieman.apphb.com/api/Movies/genre/";

    private ProgressDialog progressDialog;
    private MovieListActivity.MovieAdapter movieAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);      
        setContentView(R.layout.list);
        
        Bundle extras = super.getIntent().getExtras();
        String genre = extras.getString("genre");
        HeaderConfig headerConfig = new HeaderConfig(this, genre);

        new GenreTask().execute(genre);
      
        ListView lv = super.getListView();
        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showMovieDetail(position);
            }
        });
        
    }
    
    private class GenreTask extends AsyncTask<String, Void, List<Movie>>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MovieListActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
                    
        }
        
        @Override
        protected List<Movie> doInBackground(String... params)
        {
            AndroidHttpClient client = null;
            try
            {
                client = AndroidHttpClient.newInstance("MovieMon", null);
                HttpUriRequest request = new HttpGet(URL_BASE + Uri.encode(params[0]));
                HttpResponse response = client.execute(request);
                List<Movie> movies = MovieMapper.listFromJson(new InputStreamReader(response.getEntity().getContent()));
                client.close();
                return movies;

            }
            catch (IOException e)
            {
                Log.e("HTTP", e.toString());
                client.close();
                return null;
            }
        }
        
        @Override
        protected void onPostExecute(List<Movie> result)
        {
            super.onPostExecute(result);
            updateList(result);
            progressDialog.cancel();
        }
    }

    private void showMovieDetail(int position)
    {
        Movie selected = (Movie)this.getListAdapter().getItem(position);
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("selectedMovie", MovieMapper.toJson(selected));
        startActivity(intent);
    }

    private void updateList(List<Movie> movies)
    {
        // initialize the list view
        movieAdapter = new MovieAdapter(this, R.layout.list_item, movies);
        super.setListAdapter(movieAdapter);
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
            View item = inflater.inflate(R.layout.list_item, parent, false);

            Movie movie = getItem(position);
            ((TextView)item.findViewById(R.id.item_title)).setText(movie.getTitle());
            ((TextView)item.findViewById(R.id.item_mpaa)).setText(movie.getMPAARating());
            ((TextView)item.findViewById(R.id.item_runtime)).setText(movie.getRunTime());
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