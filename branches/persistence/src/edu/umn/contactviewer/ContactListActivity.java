package edu.umn.contactviewer;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.*;

/**
 * Displays a list of contacts.
 * */
public class ContactListActivity extends ListActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {

    	super.onCreate(savedInstanceState);
        super.setContentView(R.layout.list);
        
        new ToolbarConfig(this, "Contacts");
        
        //go get a store for us to get data from
        IContactStore store = ContactStoreFactory.getInstance().getContactStore();

        if(null != store){
        	
	        // initialize the list view
        	super.setListAdapter(new ContactAdapter(this, R.layout.list_item, store.getContacts()));

        	ListView lv = super.getListView();
	        lv.setTextFilterEnabled(true);
	        
	        // handle the item click events
	        lv.setOnItemClickListener(new OnItemClickListener() {
	        	
	        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        		
	        		ShowContactDetail(position);
	        	}
	        });        
        }
    }   

	public void ShowContactDetail(int selectedPosition){
		
		Intent intent = new Intent(this, ContactDetailsActivity.class);
		intent.putExtra("offset", selectedPosition);
		
		startActivity(intent);
	}

	/* We need to provide a custom adapter in order to use a custom list item view.
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


