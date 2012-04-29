package com.spitfire.moviemon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;
import com.spitfire.moviemon.data.Member;
import com.spitfire.moviemon.data.MemberMapper;
import com.spitfire.moviemon.data.Movie;
import com.spitfire.moviemon.data.MovieMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: eric
 * Date: 4/25/12
 * Time: 9:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserRatingActivity extends Activity implements RatingBar.OnRatingBarChangeListener,View.OnClickListener {

    private final String URL_BASE = "http://movieman.apphb.com/api/Members/";
    private final String DEFAULT_MEMEBER_ID = "f98b9048-1324-440f-802f-ebcfab1c5395";

    RatingBar ratingbar;
    Button okButton;
    Movie movie;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_rating);

        ratingbar = (RatingBar)findViewById(R.id.ratingbar);
        ratingbar.setOnRatingBarChangeListener(this);

        okButton = (Button)findViewById(R.id.ok_button);
        okButton.setOnClickListener(this);
    }

    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

        Bundle extras = super.getIntent().getExtras();
        movie = MovieMapper.movieFromJson(extras.getString("selectedMovie"));

        Toast.makeText(UserRatingActivity.this, "New Rating: " + rating, Toast.LENGTH_SHORT).show();

        movie.getKey().setRating((int)rating);
    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ok_button: {
                new RateTask().execute();
            }
            break;
        }
    }

    private class RateTask extends AsyncTask<String, Void, Member> {

        @Override
        protected void onPreExecute()  {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(QueueActivity.this);
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setMessage("Loading Queue...");
//            progressDialog.show();
        }

        @Override
        protected Member doInBackground(String... params) {

            AndroidHttpClient client = null;
            Member member = null;

            try
            {
                client = AndroidHttpClient.newInstance("MovieMon", null);
                HttpUriRequest request = new HttpGet(URL_BASE + Uri.encode(DEFAULT_MEMEBER_ID));
                HttpResponse response = client.execute(request);
                member = MemberMapper.memberFromJson(new InputStreamReader(response.getEntity().getContent()));
            }
            catch (IOException e)
            {
                Log.e("HTTP", e.toString());
            }
            finally {

                client.close();
            }

            return member;
        }

        @Override
        protected void onPostExecute(Member result) {
            super.onPostExecute(result);
//            updateList(result.getMovieQueue());
//            progressDialog.cancel();
        }
    }

}