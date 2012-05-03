package com.spitfire.moviemon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import com.spitfire.moviemon.data.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: eric
 * Date: 4/25/12
 * Time: 9:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserRatingActivity extends Activity implements RatingBar.OnRatingBarChangeListener,View.OnClickListener {

    private Movie _movie;
    private ProgressDialog progressDialog;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_rating);

        RatingBar ratingbar = (RatingBar) findViewById(R.id.ratingbar);
        ratingbar.setOnRatingBarChangeListener(this);

        findViewById(R.id.ok_button).setOnClickListener(this);
    }

    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

        Bundle extras = super.getIntent().getExtras();
        _movie = MovieMapper.movieFromJson(extras.getString("selectedMovie"));

        Toast.makeText(UserRatingActivity.this, "New Rating: " + rating, Toast.LENGTH_SHORT).show();

        _movie.getKey().setRating((int) rating);
    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ok_button: {
                new RateTask().execute();
            }
            break;
        }
    }

    private void ReloadQueue() {
        Intent intent = new Intent(this, QueueActivity.class);
        startActivity(intent);
    }

    private class RateTask extends AsyncTask<String, Void, Member> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(UserRatingActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Rating your movie...");
            progressDialog.show();
        }

        @Override
        protected Member doInBackground(String... params) {

            MemberProxy proxy = new MemberProxy();

            EditText comments = (EditText)findViewById(R.id.rating_comments);

            try {
                proxy.rateMovie(_movie.getTitle(), comments.getText().toString(), _movie.getKey().getRating());
            }
            catch (Exception ex) {
                Log.e("General", ex.toString());
            }
            finally {
                proxy.close();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Member result) {
            super.onPostExecute(result);
            progressDialog.cancel();
            ReloadQueue();
        }
    }
}