package edu.umn.contactviewer;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import edu.umn.contactviewer.data.ContactStoreFactory;
import edu.umn.contactviewer.data.IContactStore;
import edu.umn.contactviewer.data.InternalStorageContactStore;

/**
 * presents details for a single contact item
 * */
public class ContactDetailsActivity extends Activity implements OnClickListener {
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.details);
        
        Bundle extras = super.getIntent().getExtras();

        updateView(extras.getInt("offset"));
        
        ToolbarConfig tb = new ToolbarConfig(this, "Contact Details");
        
        Button btn = tb.getToolbarRightButton();
        btn.setVisibility(1);
        btn.setText("Edit");
    }

	private void updateView(int offset) {
		
        //go get a store for us to get data from
        IContactStore store = new InternalStorageContactStore(getApplicationContext());

        Contact selectedContact = store.getContactById(offset);

        TextView name = (TextView)findViewById(R.id.name);
        name.setText(selectedContact.getName());
        
        TextView title = (TextView)findViewById(R.id.title);
        title.setText(selectedContact.getTitle());

        TextView company = (TextView)findViewById(R.id.company);
        company.setText(selectedContact.getCompany());

        TextView phone = (TextView)findViewById(R.id.valuePhone);
        phone.setText(selectedContact.getPhone());

        TextView email = (TextView)findViewById(R.id.valueEmail);
        email.setText(selectedContact.getEmail());

        TextView twitter = (TextView)findViewById(R.id.valueTwitter);
        twitter.setText(selectedContact.getTwitterId());
	}
	
	public void onClick(View v) {
	}
}