package edu.umn.contactviewer;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;

/**
 * presents details for a single contact item
 * */
public class ContactDetailsActivity extends Activity implements OnClickListener {
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.details);        
    }

	public void onClick(View v) {
	}
	
	//add any of
	//onStart()
	//onRestart()
	//onResume()
	//onPause()
	//onDestroy()
}