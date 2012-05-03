package com.spitfire.moviemon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import com.spitfire.moviemon.data.Member;
import com.spitfire.moviemon.data.MemberProxy;
import com.spitfire.moviemon.data.Movie;
import com.spitfire.moviemon.data.MovieMapper;

/**
 * Created with IntelliJ IDEA.
 * User: eric
 * Date: 5/1/12
 * Time: 9:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class RemoveMovieFromQueue extends Activity {//implements View.OnClickListener{

    private Movie _movie;
    private ProgressDialog progressDialog;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle extras = super.getIntent().getExtras();
        _movie = MovieMapper.movieFromJson(extras.getString("selectedMovie"));

        new RemoveTask().execute();
    }

    private void ReloadQueue() {

        Intent intent = new Intent(this, QueueActivity.class);
        intent.putExtra("watched", "false");
        startActivity(intent);
    }

    private class RemoveTask extends AsyncTask<String, Void, Member> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RemoveMovieFromQueue.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Removing your movie...");
            progressDialog.show();
        }

        @Override
        protected Member doInBackground(String... params) {

            MemberProxy proxy = new MemberProxy();

            try {
                proxy.removeFromQueue(_movie);
            }
            catch (Exception ex) {
                Log.e("General", ex.toString());
            }
            finally {

                if(null != proxy) {
                    proxy.close();
                    proxy = null;
                }
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