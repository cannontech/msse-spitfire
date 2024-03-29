package edu.umn.contactviewer;

import java.io.InputStreamReader;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import edu.umn.contactviewer.data.Contact;
import edu.umn.contactviewer.data.ContactMapper;
import edu.umn.contactviewer.data.IContactStore;
import edu.umn.contactviewer.data.ServiceMessage;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.message.BasicNameValuePair;

/**
 * Displays a list of contacts.
 * */
public class ContactListActivity extends ListActivity {

    private ActionMode.Callback mActionModeCallback;
	private int selectedItemIndex;
    private ContactListActivity.ContactAdapter contactAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

    	super.onCreate(savedInstanceState);
        super.setContentView(R.layout.list);

        ToolbarConfig toolbar = new ToolbarConfig(this, "Contacts");
        Button addButton = toolbar.getToolbarRightButton();
        addButton.setVisibility(View.VISIBLE);
        addButton.setText("Add");

        addButton.setOnClickListener(new View.OnClickListener() {           
            public void onClick(View view) {
                handleNew();
            }
        });

        AsycGetContacts store = new AsycGetContacts();
        store.execute();

        ListView lv = super.getListView();
        lv.setTextFilterEnabled(true);

        // handle the item click events
        lv.setOnItemClickListener(new OnItemClickListener() {	        	
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showContactDetail(position);
            }
        });

        mActionModeCallback = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate a menu resource providing context menu items
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.list_context_menu, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                boolean retVal = false;
                
                switch (item.getItemId()) {
                    
                    case R.id.delete:
                    {
                        onDeleteContact();
                        contactAdapter.notifyDataSetChanged();
                        mode.finish();
                        retVal = true;
                    }
                    break;
                    
                    case R.id.edit:
                    {
                        onEditContact();
                        contactAdapter.notifyDataSetChanged();
                        mode.finish();
                        retVal = true;
                    }
                    break;
                    
                    default:
                        return false;
                }
                
                return retVal;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Here you can perform updates to the CAB due to
                // an invalidate() request
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Here you can make any necessary updates to the activity when
                // the CAB is removed. By default, selected items are deselected/unchecked.
            }
        };

        lv.setOnItemLongClickListener(new OnItemLongClickListener() {
            // Called when the user long-clicks on someView
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                startActionMode(mActionModeCallback);
                view.setSelected(true);
                selectedItemIndex = position;
                return true;
            }
        });
    }

    private void UpdateContactList(List<Contact> contacts) {

        contactAdapter = new ContactAdapter(this, R.layout.list_item, contacts);
        super.setListAdapter(contactAdapter);
    }

    private void onDeleteContact() {
        Contact selected = (Contact)this.getListAdapter().getItem(selectedItemIndex);
        AsycDeleteContact store = new AsycDeleteContact();
        store.execute(selected.get_id());
    }

	public void showContactDetail(int selectedPosition){
		
		Intent intent = new Intent(this, ContactDetailsActivity.class);
        Contact selected = (Contact)this.getListAdapter().getItem(selectedPosition);
        intent.putExtra("selectedContact", ContactMapper.toJsonString(selected));
        startActivity(intent);
	}

    private void onEditContact() {
        Intent intent = new Intent(this, NewContactActivity.class);
        Contact selected = (Contact)this.getListAdapter().getItem(selectedItemIndex);
        intent.putExtra("selectedContact", ContactMapper.toJsonString(selected));
        startActivity(intent);
    }
    
    private void handleNew(){
        Intent intent = new Intent(this, NewContactActivity.class);        
        intent.putExtra("selectedContact", ContactMapper.toJsonString(new Contact("")));
        startActivity(intent);        
    }

    public void ReturnToList() {

        Intent intent = new Intent(this, ContactListActivity.class);
        startActivity(intent);
    }

    //
    //this is the async call out to the web service where our contact data lives
    //
    class AsycGetContacts extends AsyncTask<String, Void, List<Contact>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //todo - add a progress bar in case the call is slow or times out
        }

        @Override
        protected void onPostExecute(List<Contact> results) {
            super.onPostExecute(results);
            UpdateContactList(results);
        }
    
        @Override
        protected List<Contact> doInBackground(String... params) {

            List<Contact> results = null;
            AndroidHttpClient client = null;

            try{
                client = AndroidHttpClient.newInstance("Android");
                HttpUriRequest request = new HttpGet(getString(R.string.baseUrl));
                HttpResponse response = client.execute(request);

                results = ContactMapper.ListFromJson(new InputStreamReader(response.getEntity().getContent()));
            }
            catch (Exception ex){
                Log.e("",ex.getMessage());
            }
            finally{

                if(null != client){
                    client.close();
                }
            }
            
            return results;
        }
    }

    class AsycDeleteContact extends AsyncTask<String, Void, ServiceMessage> {

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

                //get the id of the contact to delete, if it exists
                if("" != params[0]) {

                    //do a delete
                    request = new HttpDelete(buildDelete(params[0]));
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

        private String buildDelete(String contactId) {

            StringBuilder sb = new StringBuilder();

            sb.append(getString(R.string.basePutUrl));
            sb.append(contactId);
            sb.append("?");
            sb.append(getString(R.string.urlKey));

            return sb.toString();
        }
    }

    /*
        We need to provide a custom adapter in order to use a custom list item view.
	*/
	public class ContactAdapter extends ArrayAdapter<Contact> {
	
		public ContactAdapter(Context context, int textViewResourceId, List<Contact> objects) {
			super(context, textViewResourceId, objects);
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LayoutInflater inflater = getLayoutInflater();
			View item = inflater.inflate(R.layout.list_item, parent, false);
			
			Contact contact = getItem(position);
			((TextView)item.findViewById(R.id.item_name)).setText(contact.getName());
			((TextView)item.findViewById(R.id.item_title)).setText(contact.getTitle());
			((TextView)item.findViewById(R.id.item_phone)).setText(contact.getPhone());
			
			return item;
		}
	}
}


