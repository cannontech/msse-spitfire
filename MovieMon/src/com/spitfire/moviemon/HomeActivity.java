package com.spitfire.moviemon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;

/** The home screen of the application.
 *
 */
public class HomeActivity extends Activity implements OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        HeaderConfig headerConfig = new HeaderConfig(this, "MovieMon");

        findViewById(R.id.title_search_button).setOnClickListener(this);
        findViewById(R.id.browse_button).setOnClickListener(this);
        findViewById(R.id.queue_button).setOnClickListener(this);
        findViewById(R.id.watched_button).setOnClickListener(this);
    }

	public void onClick(View v) {

        switch (v.getId()) {

            case R.id.title_search_button: {

                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.browse_button:{

                Intent intent = new Intent(this, BrowseActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.queue_button:{
                //add long tap and if activated, go to a 'ratings' activity
                //http://developer.android.com/resources/tutorials/views/hello-formstuff.html#RatingBar
                //else
                Intent intent = new Intent(this, QueueActivity.class);
                intent.putExtra("watched", "true");
                startActivity(intent);
            }
            break;

            case R.id.watched_button:{

                Intent intent = new Intent(this, QueueActivity.class);
                intent.putExtra("watched", "true");
                startActivity(intent);
            }
            break;
        }
	}
}