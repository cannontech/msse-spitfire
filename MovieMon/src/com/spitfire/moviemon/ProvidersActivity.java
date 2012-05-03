package com.spitfire.moviemon;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.view.View.OnClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spitfire.moviemon.data.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lemmej1
 * Date: 4/23/12
 * Time: 8:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProvidersActivity extends Activity implements OnClickListener {
    private Movie movie;
    private ProgressDialog progressDialog;
    private final String URL_BASE = "http://movieman.apphb.com/api/Reservations";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.providers);

        findViewById(R.id.play_trailer).setOnClickListener(this);
        findViewById(R.id.add_to_queue).setOnClickListener(this);

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
                case R.id.add_to_queue: {
                    new ReserveTask().execute();
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
                    Button addButton = (Button)findViewById(R.id.add_to_queue);
                    addButton.setText("Add to Netflix Queue");
                    addButton.setVisibility(View.VISIBLE);
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
                    Button addButton = (Button)findViewById(R.id.add_to_queue);
                    addButton.setText("Reserve at RedBox");
                    addButton.setVisibility(View.VISIBLE);
                    RadioButton button1 = new RadioButton(this);
                    button1.setText(avail.getDeliveryFormat());
                    formatGroup.addView(button1);
                }
            }
        }

    }

    private class ReserveTask extends AsyncTask<String, Void, MovieReservationResult>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ProvidersActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Reserving...");
            progressDialog.show();
        }

        @Override
        protected MovieReservationResult doInBackground(String... params)
        {
            AndroidHttpClient client = null;
            MovieReservationResult result = null;
            try
            {
                client = AndroidHttpClient.newInstance("MovieMon", null);
                HttpPost put = new HttpPost(URL_BASE);
                put.setHeader("Accept", "application/json");
                put.setHeader("Content-type", "application/json");

                MovieReservation reserv = new MovieReservation();
                reserv.setTitle(movie.getTitle());
                RadioGroup group = (RadioGroup) findViewById(R.id.group_provider);
                if (group.getCheckedRadioButtonId() == R.id.radio_netflix)
                {
                  reserv.setProviderName("Netflix");
                }
                else
                {
                    reserv.setProviderName("RedBox");
                }
                RadioGroup formatGroup = (RadioGroup) findViewById(R.id.group_format);
                RadioButton format = (RadioButton) findViewById(formatGroup.getCheckedRadioButtonId());
                reserv.setFormat(format.getText().toString());

                Gson gson = new Gson();
                String str =  gson.toJson(reserv);
                put.setEntity(new ByteArrayEntity(gson.toJson(reserv).getBytes()));
                HttpResponse response = client.execute(put);

                result = (MovieReservationResult) gson.fromJson(new InputStreamReader(response.getEntity().getContent()), new TypeToken<MovieReservationResult>(){}.getType());


            }
            catch (IOException e)
            {
                Log.e("HTTP", e.toString());
            }
            finally {
                client.close();
            }
             return result;
           
        }

        @Override
        protected void onPostExecute(MovieReservationResult result)
        {
            super.onPostExecute(result);
            progressDialog.cancel();
            new AlertDialog.Builder(ProvidersActivity.this)
                    .setTitle("Success")
                    .setMessage(result.getConfirmationMessage())
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .show();
        }
    }
   

}
