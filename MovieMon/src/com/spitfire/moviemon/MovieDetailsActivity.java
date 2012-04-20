package com.spitfire.moviemon;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.spitfire.moviemon.data.Movie;
import com.spitfire.moviemon.data.MovieMapper;
import com.spitfire.moviemon.data.Rating;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Todd
 * Date: 4/18/12
 * Time: 8:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class MovieDetailsActivity extends Activity {

    private Movie movie;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.details);
        updateView();
    }

    private void updateView() {
        Bundle extras = super.getIntent().getExtras();
        movie = MovieMapper.movieFromJson(extras.getString("selectedMovie"));
        HeaderConfig headerConfig = new HeaderConfig(this, movie.getTitle());
        String imgUrl = movie.getRelatedImages().get(1).getUrl();
        try
        {
            ((ImageView)findViewById(R.id.item_cover)).setImageDrawable(Drawable.createFromStream((InputStream) new URL(imgUrl).getContent(), "src"));
        }
        catch (Exception e)
        {
            Log.e("IMAGE", e.toString());
        }

        TextView name = (TextView)findViewById(R.id.critic_rating);
        Rating rating = movie.getRating();
        name.setText(rating != null ? movie.getRating().getCriticsScore() + "%" : null);

        TextView mpaa = (TextView)findViewById(R.id.mpaa);
        mpaa.setText(movie.getMPAARating());

        TextView runtime = (TextView)findViewById(R.id.runtime);
        runtime.setText(movie.getRunTime());
    }
}