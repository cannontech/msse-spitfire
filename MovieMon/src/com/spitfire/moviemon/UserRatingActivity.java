package com.spitfire.moviemon;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: eric
 * Date: 4/25/12
 * Time: 9:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserRatingActivity extends Activity implements RatingBar.OnRatingBarChangeListener {

    RatingBar ratingbar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_rating);

        ratingbar = (RatingBar)findViewById(R.id.ratingbar);
        ratingbar.setOnRatingBarChangeListener(this);
    }

    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

        //obviously, we need to actually post our rating to the server
        Toast.makeText(UserRatingActivity.this, "New Rating: " + rating, Toast.LENGTH_SHORT).show();
    }
}