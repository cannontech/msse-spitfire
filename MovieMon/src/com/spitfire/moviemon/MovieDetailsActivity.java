package com.spitfire.moviemon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.spitfire.moviemon.data.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.PrivateKey;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Todd
 * Date: 4/18/12
 * Time: 8:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class MovieDetailsActivity extends Activity implements OnClickListener {

    private Movie movie;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.details);

        findViewById(R.id.play_trailer).setOnClickListener(this);
        findViewById(R.id.read_reviews).setOnClickListener(this);
        findViewById(R.id.check_providers).setOnClickListener(this);
        findViewById(R.id.add_to_queue).setOnClickListener(this);

        updateView();
    }

    public void onClick(View v) {
        Bundle extras = super.getIntent().getExtras();
        movie = MovieMapper.movieFromJson(extras.getString("selectedMovie"));

        switch (v.getId()) {

            case R.id.play_trailer: {

                if (movie.getRelatedClips() != null && !movie.getRelatedClips().isEmpty())
                {
                    //String trailerUrl = movie.getRelatedClips().get(0);

//                    if (trailerUrl != null)
//                    {

                        //Intent intent = new Intent(this, TrailerActivity.class);
                        //intent.putExtra("url", trailerUrl);
                        //startActivity(intent);
                        String urlToPlay = null;
                        if (movie.getTitle().equalsIgnoreCase("Rocky")){
                            urlToPlay="http://m.youtube.com/watch?v=aJmr5CKY73M";
                        }else{
                            urlToPlay="http://m.youtube.com/watch?v=g8evyE9TuYk";
                        }
                        WebView wView = new WebView(this);
                        wView.getSettings().setPluginState(WebSettings.PluginState.ON);
                        wView.getSettings().setJavaScriptEnabled(true);
                        wView.getSettings().setAllowFileAccess(true);
                        wView.getSettings().setPluginsEnabled(true);
                        wView.loadUrl(urlToPlay);
                        setContentView(wView);
                    //wView.setWebViewClient(new MyWebViewClient());
                        break;
                   // }
                }
            }

            case R.id.read_reviews: {
                Intent intent = new Intent(this, ReviewListActivity.class);
                intent.putExtra("selectedMovie", MovieMapper.toJson(movie));
                startActivity(intent);
                break;
            }

            case R.id.check_providers: {
                Intent intent = new Intent(this, ProvidersActivity.class);
                intent.putExtra("selectedMovie", MovieMapper.toJson(movie));
                startActivity(intent);
                break;
            }

            case R.id.add_to_queue: {
                AddToQueueTask task = new AddToQueueTask();
                task.execute(new String[] { extras.getString("selectedMovie")} );
                Toast.makeText(getApplicationContext(),
                        "Added to Queue", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void updateView() {

        Bundle extras = super.getIntent().getExtras();
        movie = MovieMapper.movieFromJson(extras.getString("selectedMovie"));
        HeaderConfig headerConfig = new HeaderConfig(this, movie.getTitle());
        if (movie.getRelatedImages() != null && movie.getRelatedImages().get(1) != null)
        {
            String imgUrl = movie.getRelatedImages().get(1).getUrl();


            try
            {
                ((ImageView)findViewById(R.id.item_cover)).setImageDrawable(Drawable.createFromStream((InputStream) new URL(imgUrl).getContent(), "src"));
            }
            catch (Exception e)
            {
                Log.e("IMAGE", e.toString());
            }
        }

        TextView name = (TextView)findViewById(R.id.critic_rating);
        Rating rating = movie.getRating();
        name.setText(rating != null && rating.getCriticsScore() != null && !rating.getCriticsScore().startsWith("-") ? rating.getCriticsScore() + "%" : "N/A");

        TextView mpaa = (TextView)findViewById(R.id.mpaa);
        mpaa.setText(movie.getMPAARating());

        TextView dateReleased = (TextView)findViewById(R.id.released_date);

        if (movie.getAvailability() != null &&
            movie.getAvailability().size() > 0 &&
            movie.getAvailability().get(0) != null &&
            movie.getAvailability().get(0).getAvailableFrom() != null)
        {
            dateReleased.setText(movie.getAvailability().get(0).getReleaseDate());
        }

        TextView runtime = (TextView)findViewById(R.id.runtime);
        runtime.setText(movie.getRunTime());

        TextView summary = (TextView)findViewById(R.id.summary);
        summary.setText(movie.getSummary());
        
        TextView formats = (TextView)findViewById(R.id.formats);
        String format = "";
        boolean first = true;

        if (movie.getAvailability() != null)
        {
            for (Availability avail : movie.getAvailability() ) {

                if (!format.contains(avail.getDeliveryFormat())) {

                    if (!first) {
                        format = format + ", ";
                    }
                    format = format + avail.getDeliveryFormat();
                    first = false;
                }
            }
            formats.setText(format);
        }

        TextView cast = (TextView)findViewById(R.id.cast);
        cast.setText(movie.getCast().toString().replace("[","").replace("]",""));

        Button trailerBtn = (Button)findViewById(R.id.play_trailer);

        if (movie.getRelatedClips() == null || movie.getRelatedClips().isEmpty())
        {
            trailerBtn.setVisibility(View.INVISIBLE);
        }

        Button reviewBtn = (Button)findViewById(R.id.read_reviews);

        if (movie.getReviews() == null || movie.getReviews().isEmpty())
        {
            reviewBtn.setVisibility(View.INVISIBLE);
        }
    }

    private class AddToQueueTask extends AsyncTask<String, Void, String> {

        private MemberProxy memberProxy = new MemberProxy();

        @Override
        protected void onPreExecute()  {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... newMovies) {

            movie = MovieMapper.movieFromJson(newMovies[0]);

            try {
                memberProxy.addToQueue(movie);
            }
            catch (Exception e) {
                Log.e("HTTP", e.toString());
            }
            finally {
                memberProxy.close();
            }
            finish();
            return "AddedToQueue" ;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }
}