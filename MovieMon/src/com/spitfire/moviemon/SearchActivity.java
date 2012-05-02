package com.spitfire.moviemon;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import android.content.Intent;
import android.widget.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.spitfire.moviemon.data.Movie;
import com.spitfire.moviemon.data.MovieMapper;

public class SearchActivity extends ListActivity
{
    private String URL_BASE = "http://movieman.apphb.com/api/Movies/";
    
    private EditText searchField;
    private Button searchButton;
    private ProgressDialog progressDialog;

    private SearchActivity.MovieAdapter movieAdapter;

    /** Called when the activity is first created . */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);      
        setContentView(R.layout.search);
            
        searchField = (EditText) findViewById(R.id.search_field);
        searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SearchTask().execute(searchField.getText().toString());
            }
        });

        Button logoBtn = (Button) findViewById(R.id.logo);
        logoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHome();
            }
        });

        ListView lv = super.getListView();
        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showMovieDetail(position);
            }
        });
    }
    
    private class SearchTask extends AsyncTask<String, Void, List<Movie>>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SearchActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Searching for '" + searchField.getText().toString() + "'...");
            progressDialog.show();
        }
        
        @Override
        protected List<Movie> doInBackground(String... params)
        {
            AndroidHttpClient client = null;
            List<Movie> movies = null;

            try
            {
                client = AndroidHttpClient.newInstance("MovieMon", null);
                HttpUriRequest request = new HttpGet(URL_BASE + Uri.encode(params[0]));
                HttpResponse response = client.execute(request);
                movies = MovieMapper.listFromJson(new InputStreamReader(response.getEntity().getContent()));
            }
            catch (IOException e)
            {
                Log.e("HTTP", e.toString());
            }
            finally {
                client.close();
            }

            return movies;
        }
        
        @Override
        protected void onPostExecute(List<Movie> result)
        {
            super.onPostExecute(result);
            updateList(result);
            progressDialog.cancel();
        }
    }

    private void goToHome()
    {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        this.finish();
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

            if (movie.getRelatedImages() != null && movie.getRelatedImages().size() > 0) {
                String imgUrl = movie.getRelatedImages().get(0).getUrl();

                try {
                    ((ImageView)item.findViewById(R.id.item_cover)).setImageDrawable(Drawable.createFromStream((InputStream) new URL(imgUrl).getContent(), "src"));
                }
                catch (Exception e) {
                     Log.e("IMAGE", e.toString());
                }
            }

            return item;
        }
    }
}
