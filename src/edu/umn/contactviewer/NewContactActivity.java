package edu.umn.contactviewer;

import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import edu.umn.contactviewer.data.Contact;
import edu.umn.contactviewer.data.ContactMapper;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import edu.umn.contactviewer.data.ServiceMessage;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class NewContactActivity extends Activity {

    private boolean _editing = false;
    private String _contactId;

	@Override
	public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
		super.setContentView(R.layout.newcontact);

        ToolbarConfig toolbar = new ToolbarConfig(this, "New Contact");
        Button addButton = toolbar.getToolbarRightButton();
        addButton.setVisibility(View.VISIBLE);
        addButton.setText("Save");
        bind();

        if (0 != ((TextView) findViewById(R.id.txtName)).getText().length()) {

            toolbar.getToolbarTitleView().setText("Edit Contact");
            _editing = true;
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

        TextView phone = (TextView)findViewById(R.id.txtPhone);
        contact.setPhone(phone.getText().toString());

        TextView email = (TextView)findViewById(R.id.txtEmail);
        contact.setEmail(email.getText().toString());

        TextView twitter = (TextView)findViewById(R.id.txtTwitter);
        contact.setTwitterId(twitter.getText().toString());

        AsycSaveContacts store = new AsycSaveContacts();

        if(_editing) {
            store.execute("edit", _contactId);
        }
        else {
            store.execute("new");
        }
    }
	 
    private void bind() {

        Bundle extras = super.getIntent().getExtras();
        Contact selectedContact = ContactMapper.fromJsonString(extras.getString("selectedContact"));

        TextView name = (TextView)findViewById(R.id.txtName);
        name.setText(selectedContact.getName());

        TextView title = (TextView)findViewById(R.id.txtTitle);
        title.setText(selectedContact.getTitle());

        TextView phone = (TextView)findViewById(R.id.txtPhone);
        phone.setText(selectedContact.getPhone());

        TextView email = (TextView)findViewById(R.id.txtEmail);
        email.setText(selectedContact.getEmail());

        TextView twitter = (TextView)findViewById(R.id.txtTwitter);
        twitter.setText(selectedContact.getTwitterId());

        _contactId = selectedContact.get_id();
    }

    //pull the entered information off the gui and put it in a list to
    //post to the server
    private List<BasicNameValuePair> getDetails() {

        List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();

        TextView name = (TextView)findViewById(R.id.txtName);

        if( 0 < name.getText().toString().length()) {

            list.add(new BasicNameValuePair("name",name.getText().toString()));
        }

        TextView title = (TextView)findViewById(R.id.txtTitle);

        if( 0 < title.getText().toString().length()) {

            list.add(new BasicNameValuePair("title",title.getText().toString()));
        }

        TextView phone = (TextView)findViewById(R.id.txtPhone);

        if( 0 < phone.getText().toString().length()) {

            list.add(new BasicNameValuePair("phone",phone.getText().toString()));
        }

        TextView email = (TextView)findViewById(R.id.txtEmail);

        if( 0 < email.getText().toString().length()) {

            list.add(new BasicNameValuePair("email",email.getText().toString()));
        }

        TextView twitter = (TextView)findViewById(R.id.txtTwitter);

        if( 0 < twitter.getText().toString().length()) {

            list.add(new BasicNameValuePair("twitterId",twitter.getText().toString()));
        }

        return list;
    }

    public void ReturnToList() {

        Intent intent = new Intent(this, ContactListActivity.class);
        startActivity(intent);
    }

    //
    //this is the async call out to the web service where our contact data lives
    //
    class AsycSaveContacts extends AsyncTask<String, Void, ServiceMessage> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            //todo - add a progress bar in case the call is slow or times out
        }

        @Override
        protected void onPostExecute(ServiceMessage results) {

            super.onPostExecute(results);

            if(results.get_message().contains("Successfully")) {

                ReturnToList();
            }
        }

        @Override
        protected ServiceMessage doInBackground(String... params) {

            ServiceMessage results = null;
            AndroidHttpClient client = null;
            HttpUriRequest request = null;

            try{
                client = AndroidHttpClient.newInstance("Android");

                if( "new" == params[0]) {

                    //do a post
                    List<BasicNameValuePair> list = getDetails();
                    String fullUrl = buildPost(list);
                    request = new HttpPost(buildPost(list));
                }
                else {

                    //get the id of the contact to update, if it exists
                    if((2 <= params.length)&&("" != params[1])) {

                        //do an update
                        List<BasicNameValuePair> list = getDetails();
                        request = new HttpPut(buildPut(params[1], list));
                    }
                }

                HttpResponse response = client.execute(request);
                results = ContactMapper.fromJsonString(new InputStreamReader(response.getEntity().getContent()));
            }
            catch (Exception ex){
                Log.e("", ex.getMessage());
            }
            finally{

                if(null != client){
                    client.close();
                }
            }

            return results;
        }

        private String buildPost(List<BasicNameValuePair> parts) {

            StringBuilder sb = new StringBuilder();

            sb.append(getString(R.string.basePostUrl));
            sb.append("?");
            sb.append(getString(R.string.urlKey));
            sb.append(buildQuery(parts));

            return sb.toString();
        }

        private String buildPut(String contactId, List<BasicNameValuePair> parts) {

            StringBuilder sb = new StringBuilder();

            sb.append(getString(R.string.basePutUrl));
            sb.append(contactId);
            sb.append("?");
            sb.append(getString(R.string.urlKey));
            sb.append(buildQuery(parts));

            return sb.toString();
        }

        private String buildQuery(List<BasicNameValuePair> parts) {

            StringBuilder sb = new StringBuilder();

            for(BasicNameValuePair pair : parts) {

                sb.append("&");
                sb.append(pair.getName());
                sb.append("=");
                sb.append(pair.getValue());
            }

            return (sb.toString()).replace(' ','+');
        }
    }
}
