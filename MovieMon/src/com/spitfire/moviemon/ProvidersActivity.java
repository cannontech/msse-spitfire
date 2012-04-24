package com.spitfire.moviemon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.view.View.OnClickListener;
import com.spitfire.moviemon.data.Movie;
import com.spitfire.moviemon.data.MovieMapper;
import com.spitfire.moviemon.data.Availability;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: lemmej1
 * Date: 4/23/12
 * Time: 8:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProvidersActivity extends Activity implements OnClickListener {
    private Movie movie;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.providers);

        findViewById(R.id.play_trailer).setOnClickListener(this);

        Bundle extras = super.getIntent().getExtras();

        movie = MovieMapper.movieFromJson(extras.getString("selectedMovie"));
        HeaderConfig headerConfig = new HeaderConfig(this, movie.getTitle() + " Availability");
        String imgUrl = movie.getRelatedImages().get(1).getUrl();
        try
        {
            ((ImageView)findViewById(R.id.item_cover)).setImageDrawable(Drawable.createFromStream((InputStream) new URL(imgUrl).getContent(), "src"));
        }
        catch (Exception e)
        {
            Log.e("IMAGE", e.toString());
        }

        updateView();
    }

    public void onClick(View v) {
        Bundle extras = super.getIntent().getExtras();
        movie = MovieMapper.movieFromJson(extras.getString("selectedMovie"));
        String trailerUrl = movie.getRelatedClips().get(0);
        if (trailerUrl != null)
        {
            switch (v.getId()) {

                case R.id.play_trailer: {

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(trailerUrl),"video/*");
                    startActivity(intent);
                    break;
                }
            }
        }
    }

    private void updateView() {
        Bundle extras = super.getIntent().getExtras();
        movie = MovieMapper.movieFromJson(extras.getString("selectedMovie"));
        HeaderConfig headerConfig = new HeaderConfig(this, movie.getTitle());
        String netflixAvailability = "";
        String redboxAvailability = "";
        Boolean netflixHas = false;
        Boolean redboxHas = false;

        
        for (Availability avail : movie.getAvailability() ) {
                  if (avail.getProviderName().equals("Netflix")) {
                      netflixHas = true;
                      netflixAvailability = netflixAvailability + " " + avail.getDeliveryFormat();
                  }
                  if (avail.getProviderName().equals("Redbox")) {
                      redboxHas = true;
                      redboxAvailability = redboxAvailability + " " + avail.getDeliveryFormat();
                  }
               }
             if (netflixHas) {
                 netflixAvailability = "Available Now" + netflixAvailability;
             }
             if (!netflixHas) {
                 netflixAvailability = "Not Available";
             }
             if (redboxHas) {
                 redboxAvailability = "Available Now" + redboxAvailability;
             }
             if (!redboxHas) {
                 redboxAvailability = "Not Available";
             }
        
        RadioButton netflix = (RadioButton)findViewById(R.id.radio_netflix);
        netflix.setText(netflixAvailability);

        RadioButton redbox = (RadioButton)findViewById(R.id.radio_redbox);
        redbox.setText(redboxAvailability);
    }
   

}
