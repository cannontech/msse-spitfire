package com.spitfire.moviemon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;
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
public class MovieDetailsActivity extends Activity implements OnClickListener {

    private Movie movie;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.details);

        findViewById(R.id.play_trailer).setOnClickListener(this);

        updateView();
    }

    public void onClick(View v) {
        Bundle extras = super.getIntent().getExtras();
        movie = MovieMapper.movieFromJson(extras.getString("selectedMovie"));
        String trailerUrl = movie.getRelatedClips().get(1);
        if (trailerUrl != null)
        {
            switch (v.getId()) {

                case R.id.play_trailer: {

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(trailerUrl),"video/*");
                    startActivity(intent);
                }
            }
        }
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
        name.setText(rating != null && rating.getCriticsScore() != null && !rating.getCriticsScore().startsWith("-") ? rating.getCriticsScore() + "%" : "N/A");

        TextView mpaa = (TextView)findViewById(R.id.mpaa);
        mpaa.setText(movie.getMPAARating());

        TextView runtime = (TextView)findViewById(R.id.runtime);
        runtime.setText(movie.getRunTime());

        TextView summary = (TextView)findViewById(R.id.summary);
        summary.setText(movie.getSummary());

        TextView cast = (TextView)findViewById(R.id.cast);
        cast.setText(movie.getCast().toString().replace("[","").replace("]",""));
    }
}