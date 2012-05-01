package com.spitfire.moviemon;

import android.app.Activity;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    private final String URL_BASE = "http://movieman.apphb.com/api/Members/";
    private final String DEFAULT_MEMEBER_ID = "f98b9048-1324-440f-802f-ebcfab1c5395";

    private RatingBar _ratingbar;
    private Button _okButton;
    private Movie _movie;
    private Member _member;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_rating);

        _ratingbar = (RatingBar)findViewById(R.id.ratingbar);
        _ratingbar.setOnRatingBarChangeListener(this);

        _okButton = (Button)findViewById(R.id.ok_button);
        _okButton.setOnClickListener(this);
    }

    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

        Bundle extras = super.getIntent().getExtras();
        _movie = MovieMapper.movieFromJson(extras.getString("selectedMovie"));
        _member = MemberMapper.memberFromJson(extras.getString("member"));

        Toast.makeText(UserRatingActivity.this, "New Rating: " + rating, Toast.LENGTH_SHORT).show();

        _movie.getKey().setRating((int)rating);
        _member.getMovieQueue().add(_movie);
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

//        @Override
//        protected Member doInBackground(String... params) {
//
//            AndroidHttpClient client = null;
//            Member member = null;
//
//            try {
//                client = AndroidHttpClient.newInstance("MovieMon", null);
//                String mem = MemberMapper.toJson(_member);
//                //String url = URL_BASE + Uri.encode(DEFAULT_MEMEBER_ID) + "/" + mem;
//                HttpUriRequest request = new HttpPut(URL_BASE + Uri.encode(DEFAULT_MEMEBER_ID) + "/" + mem);
//                request.setHeader("Content-Type", "application/json");
//                //request.
//                //request.setEntity(MemberMapper.toJson(_member));
//                HttpResponse response = client.execute(request);
//                member = MemberMapper.memberFromJson(new InputStreamReader(response.getEntity().getContent()));
//            }
//            catch (IOException e) {
//                Log.e("HTTP", e.toString());
//            }
//            catch (Exception ex) {
//                Log.e("General", ex.toString());
//            }
//            finally {
//                if(null != client) {
//                    client.close();
//                }
//            }

//            return member;
//        }

//        @Override
//        protected Member doInBackground(String... params) {
//
//            AndroidHttpClient client = null;
//            Member member = null;
//
//            try {
//                client = AndroidHttpClient.newInstance("MovieMon", null);
//                HttpClient request = new DefaultHttpClient();
//                //HttpPut put = new HttpPut(URL_BASE + Uri.encode(DEFAULT_MEMEBER_ID));
//                HttpPut put = new HttpPut(URL_BASE);
//                //put.setEntity(MemberMapper.toJson(_member).);
//                String mem = MemberMapper.toJson(_member);
//                put.setEntity(new ByteArrayEntity(mem.getBytes()));
//                HttpResponse response = client.execute(put);
//                member = MemberMapper.memberFromJson(new InputStreamReader(response.getEntity().getContent()));
//            }
//            catch (IOException e) {
//                Log.e("HTTP", e.toString());
//            }
//            catch (Exception ex) {
//                Log.e("General", ex.toString());
//            }
//            finally {
//                if(null != client) {
//                    client.close();
//                }
//            }
//
//            return member;
//        }

        @Override
        protected Member doInBackground(String... params) {

            AndroidHttpClient client = null;
            Member member = null;
            MemberProxy proxy = new MemberProxy();

            try {

                List<Movie> queue = proxy.getDefaultMember().getMovieQueue();

                for(Movie m : queue ) {

                    if(m.getTitle() == m.getTitle()) {

                        proxy.rateMovie(m, "this movie was ok", 4);
                        proxy.markAsWatched(m);
                    }
                }
            }
            catch (Exception ex) {
                Log.e("General", ex.toString());
            }
            finally {
                if(null != client) {
                    client.close();
                }
            }

            return member;
        }

        @Override
        protected void onPostExecute(Member result) {
            super.onPostExecute(result);
//            updateList(result.getMovieQueue());
//            progressDialog.cancel();
        }

        private String buildPut(String contactId, List<BasicNameValuePair> parts) {

            StringBuilder sb = new StringBuilder();

            sb.append(URL_BASE);
            sb.append(contactId);
            sb.append("?");
            sb.append(Uri.encode(DEFAULT_MEMEBER_ID));
            sb.append(buildQuery(parts));

            return sb.toString();
        }
        private String buildQuery(List<BasicNameValuePair> parts) {

            StringBuilder sb = new StringBuilder();

            for(BasicNameValuePair pair : parts) {

                sb.append("&");
                sb.append(pair.getName());
                sb.append("=");
                sb.append(pair.getValue());
            }

            return (sb.toString()).replace(' ','+');
        }
    }
}