package edu.umn.contactviewer;

import java.util.ArrayList;
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

/** Displays a list of contacts.
 *
 */
public class ContactListActivity extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

    	super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        
        new ToolbarConfig(this, "Contacts");
        
        ContactStore store = new ContactStore();
        
        // initialize the list view
        setListAdapter(new ContactAdapter(this, R.layout.list_item, store.getContacts()));
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        
        // handle the item click events
        lv.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		ShowContactDetail();
        	}
        });        
    }   

	public void ShowContactDetail(){
		
		Intent intent = new Intent(this, ContactDetailsActivity.class);
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

