package edu.umn.contactviewer;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import edu.umn.contactviewer.data.Contact;
import edu.umn.contactviewer.data.ContactMapper;
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
		ToolbarConfig toolbar = new ToolbarConfig(this, "New Contact");
        Button addButton = toolbar.getToolbarRightButton();
        addButton.setVisibility(View.VISIBLE);
        addButton.setText("Save");
        bind();
        if (((TextView) findViewById(R.id.txtName)).getText().length() != 0)  {
            toolbar.getToolbarTitleView().setText("Edit Contact");
        }
        
        addButton.setOnClickListener(new View.OnClickListener(){                        
            public void onClick(View view) {
                onSaveClicked();
            }
        });
	}

    private void onSaveClicked() {


        TextView name = (TextView)findViewById(R.id.txtName);
        Contact contact = new Contact(name.getText().toString());

        TextView title = (TextView)findViewById(R.id.txtTitle);
        contact.setTitle(title.getText().toString());

//        TextView company = (TextView)findViewById(R.id.txtCompany);
//        contact.setCompany(company.getText().toString());
         
        TextView phone = (TextView)findViewById(R.id.txtPhone);
        contact.setPhone(phone.getText().toString());

        TextView email = (TextView)findViewById(R.id.txtEmail);
        contact.setEmail(email.getText().toString());

        TextView twitter = (TextView)findViewById(R.id.txtTwitter);
        contact.setTwitterId(twitter.getText().toString());
        
        IContactStore contacts =  new InternalStorageContactStore(getApplicationContext());
        contacts.saveContact(contact);

        Intent intent = new Intent(this, ContactListActivity.class);
        startActivity(intent);

    }
	 
	 private void bind(){
         
         Bundle extras = super.getIntent().getExtras();
         Contact selectedContact = ContactMapper.fromJsonString(extras.getString("selectedContact"));

         TextView name = (TextView)findViewById(R.id.txtName);
         name.setText(selectedContact.getName());
                 
         TextView title = (TextView)findViewById(R.id.txtTitle);
         title.setText(selectedContact.getTitle());

//         TextView company = (TextView)findViewById(R.id.txtCompany);
//         company.setText(selectedContact.getCompany());

         TextView phone = (TextView)findViewById(R.id.txtPhone);
         phone.setText(selectedContact.getPhone());         

         TextView email = (TextView)findViewById(R.id.txtEmail);
         email.setText(selectedContact.getEmail());         

         TextView twitter = (TextView)findViewById(R.id.txtTwitter);
         twitter.setText(selectedContact.getTwitterId());

	 }
}
