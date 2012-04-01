package com.moviemon;

import android.app.ListActivity;
import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.moviemon.data.Movie;
import com.moviemon.data.MovieMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class SearchActivity extends ListActivity
{
    private String URL_BASE = "http://movieman.apphb.com/api/Movies/";
    
    private EditText searchField;
    private Button searchButton;

    private SearchActivity.MovieAdapter movieAdapter;

    /** Called when the activity is first created. */
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
    }
    
    private class SearchTask extends AsyncTask<String, Void, List<Movie>>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }
        
        @Override
        protected List<Movie> doInBackground(String... params)
        {
            AndroidHttpClient client = null;
            try
            {
                client = AndroidHttpClient.newInstance("MovieMon", null);
                HttpUriRequest request = new HttpGet(URL_BASE + params[0].replace(" ", "%20"));
                HttpResponse response = client.execute(request);
                List<Movie> movies = MovieMapper.listFromJson(new InputStreamReader(response.getEntity().getContent()));
                client.close();
                return movies;

            }
            catch (IOException e)
            {
                client.close();
                return null;
            }
        }
        
        @Override
        protected void onPostExecute(List<Movie> result)
        {
            super.onPostExecute(result);
            updateList(result);
        }
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

            return item;
        }
    }
}
