package edu.umn.contactviewer;

import edu.umn.contactviewer.data.IContactStore;
import edu.umn.contactviewer.data.InternalStorageContactStore;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class NewContactActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.newcontact);
		 
		Bundle extras = super.getIntent().getExtras();
		
		bind();
		
		ToolbarConfig tb = new ToolbarConfig(this, "New Contact");
		//tb.getToolbarRightButton().setVisibility(1);
		
		Button btn = tb.getToolbarRightButton();
		btn.setVisibility(1);
		btn.setText("Edit");
	}
	 
	 private void bind(){
		IContactStore store = new InternalStorageContactStore(getApplicationContext());
		
	 }
}
