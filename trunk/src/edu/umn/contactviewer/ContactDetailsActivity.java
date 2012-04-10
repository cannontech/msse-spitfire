package edu.umn.contactviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.umn.contactviewer.data.Contact;
import edu.umn.contactviewer.data.ContactMapper;

/**
 * presents details for a single contact item
 * */
public class ContactDetailsActivity extends Activity implements OnClickListener {

    Contact currentContact;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.details);        
        updateView();              
        
        ToolbarConfig tb = new ToolbarConfig(this, "Contact Details");
        
        Button btn = tb.getToolbarRightButton();
        btn.setVisibility(1);
        btn.setText("Edit");

        btn.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(ContactDetailsActivity.this, NewContactActivity.class);
                intent.putExtra("selectedContact", ContactMapper.toJsonString(currentContact));
                startActivity(intent);
            }
        });
    }

	private void updateView() {
		
        //go get a store for us to get data from

        Bundle extras = super.getIntent().getExtras();
        currentContact = ContactMapper.fromJsonString(extras.getString("selectedContact"));

        TextView name = (TextView)findViewById(R.id.name);
        name.setText(currentContact.getName());
        
        TextView title = (TextView)findViewById(R.id.title);
        title.setText(currentContact.getTitle());

        TextView phone = (TextView)findViewById(R.id.valuePhone);
        phone.setText(currentContact.getPhone());

        TextView email = (TextView)findViewById(R.id.valueEmail);
        email.setText(currentContact.getEmail());

        TextView twitter = (TextView)findViewById(R.id.valueTwitter);
        twitter.setText(currentContact.getTwitterId());
	}
	
	public void onClick(View v) {
	}
}