package com.spitfire.moviemon;

import com.spitfire.moviemon.R;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/** Use this class to configure the header for an activity.
 *
 */
public class HeaderConfig {

	private Activity _activity;
	
	public HeaderConfig(Activity activity, String title) {
		_activity = activity;
        if (hasHeader()) {
        	getHeaderTitle().setText(title);
        } else {
        	throw new RuntimeException("Trying to initialize the header in a layout that doesn't have one!");
        }
	}
    
    /** Returns true if this activity's content view has a toolabr.
     */
    public boolean hasHeader() {
    	return _activity.findViewById(R.id.header) != null;
    }
	
    /** Gets a reference to the logo on the header.
     */
    public ImageView getHeaderLogo() {
    	return (ImageView)_activity.findViewById(R.id.logo);
    }
	
    /** Gets a reference to the title on the header.
     */
    public TextView getHeaderTitle() {
    	return (TextView)_activity.findViewById(R.id.header_title);
    }
	
    /** Gets a reference to the button on the header.
     */
    public Button getHeaderButton() {
    	return (Button)_activity.findViewById(R.id.header_button);
    }
	
}
