package com.spitfire.moviemon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
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
        {
            switch (v.getId()) {

                case R.id.play_trailer: {
                    if (movie.getRelatedClips() != null && !movie.getRelatedClips().isEmpty())
                    {
                        String trailerUrl = movie.getRelatedClips().get(0);
                        if (trailerUrl != null)
                        {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse(trailerUrl),"video/*");
                            startActivity(intent);
                            break;
                        }
                    }
                }
            }
        }
    }


    private void updateView() {
        Bundle extras = super.getIntent().getExtras();
        movie = MovieMapper.movieFromJson(extras.getString("selectedMovie"));
        HeaderConfig headerConfig = new HeaderConfig(this, movie.getTitle());

        TextView formats = (TextView)findViewById(R.id.formats);
        String format = "";
        boolean first = true;
        for (Availability avail : movie.getAvailability() ) {
            if (!format.contains(avail.getDeliveryFormat()))
            {
                if (!first)
                {
                    format = format + ", ";
                }
                format = format + avail.getDeliveryFormat();
                first = false;
            }
        }
        formats.setText(format);

        TextView mpaa = (TextView)findViewById(R.id.mpaa);
        mpaa.setText(movie.getMPAARating());

        TextView runtime = (TextView)findViewById(R.id.runtime);
        runtime.setText(movie.getRunTime());

        TextView dateReleased = (TextView)findViewById(R.id.released_date);
        if (movie.getAvailability() != null && movie.getAvailability().get(0) != null &&movie.getAvailability().get(0).getAvailableFrom() != null)
        {
            dateReleased.setText(movie.getAvailability().get(0).getAvailableFrom());
        }

        String netflixAvailability = "";
        String redboxAvailability = "";
        Boolean netflixHas = false;
        Boolean redboxHas = false;

        OnClickListener radioListener = new OnClickListener() {
            public void onClick(View v) {
                onRadioButtonClick(v);
            }
        };
        
        RadioButton netflix = (RadioButton)findViewById(R.id.radio_netflix);
        netflix.setOnClickListener(radioListener);
        RadioButton redbox = (RadioButton)findViewById(R.id.radio_redbox);
        redbox.setOnClickListener(radioListener);


        String addr = "";
        for (Availability avail : movie.getAvailability() ) {
              if (avail.getProviderName().equals("Netflix")) {
                  netflixHas = true;
              }
              if (avail.getProviderName().equals("RedBox")) {
                  redboxHas = true;
                  addr =  avail.getAddresses().get(0);
              }
        }
        if (netflixHas)
        {
            netflixAvailability = "Available Now";
        }
        else
        {
            netflixAvailability = "Not Available";
        }
        if (redboxHas)
        {
            redboxAvailability = "Available Now " + addr;
        }
        else
        {
            redboxAvailability = "Not Available";
        }
        
        redbox.setText(redboxAvailability);
        netflix.setText(netflixAvailability);
        

        Button trailerBtn = (Button)findViewById(R.id.play_trailer);
        if (movie.getRelatedClips() == null || movie.getRelatedClips().isEmpty())
        {
            trailerBtn.setVisibility(View.INVISIBLE);
        }
    }

    public void onRadioButtonClick(View v) {
        RadioButton button = (RadioButton) v;
        RadioGroup formatGroup = (RadioGroup)findViewById(R.id.group_format);
        formatGroup.removeAllViews();
        TextView formatLabel = (TextView)findViewById(R.id.format_label);
        formatLabel.setVisibility(View.VISIBLE);
        formatGroup.setVisibility(View.VISIBLE);
        if (button.getId() == R.id.radio_netflix)
        {
            for (Availability avail : movie.getAvailability() ) {
                if (avail.getProviderName().equals("Netflix")) {
                    RadioButton button1 = new RadioButton(this);
                    button1.setText(avail.getDeliveryFormat());
                    formatGroup.addView(button1);
                }
            }
        }
        else if (button.getId() == R.id.radio_redbox)
        {
            for (Availability avail : movie.getAvailability() ) {

                if (avail.getProviderName().equals("RedBox")) {
                    RadioButton button1 = new RadioButton(this);
                    button1.setText(avail.getDeliveryFormat());
                    formatGroup.addView(button1);
                }
            }
        }

    }
   

}
