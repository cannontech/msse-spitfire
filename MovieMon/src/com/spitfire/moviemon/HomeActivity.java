package com.spitfire.moviemon;

import com.spitfire.moviemon.HeaderConfig;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/** The home screen of the application.
 *
 */
public class HomeActivity extends Activity implements OnClickListener{
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
		if (v.getId() == R.id.title_search_button) {
			Intent intent = new Intent(this, SearchActivity.class);
			startActivity(intent);
		}
        if (v.getId() == R.id.browse_button ) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.queue_button ) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.watched_button ) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }
	}
}