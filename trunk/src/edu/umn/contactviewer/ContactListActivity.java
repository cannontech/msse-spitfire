package edu.umn.contactviewer;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import edu.umn.contactviewer.data.Contact;
import edu.umn.contactviewer.data.ContactMapper;
import edu.umn.contactviewer.data.IContactStore;
import edu.umn.contactviewer.data.InternalStorageContactStore;

/**
 * Displays a list of contacts.
 * */
public class ContactListActivity extends ListActivity {

    private ActionMode.Callback mActionModeCallback;
	private int selectedItemIndex;
    private IContactStore store;
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
        //go get a store for us to get data from
        store = new InternalStorageContactStore(getApplicationContext());

        
        // initialize the list view
        contactAdapter = new ContactAdapter(this, R.layout.list_item, store.getContacts());
        super.setListAdapter(contactAdapter);

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
                switch (item.getItemId()) {
                    case R.id.delete:
                        onDeleteContact();
                        contactAdapter.notifyDataSetChanged();
                        return true;
                    case R.id.edit:
                        onEditContact();
                        contactAdapter.notifyDataSetChanged();
                        return true;
                    default:
                        return false;
                }
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

    private void onDeleteContact() {
        Contact selected = (Contact)this.getListAdapter().getItem(selectedItemIndex);
        store.delete(selected);
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
        intent.putExtra("selectedContact", ContactMapper.toJsonString(new Contact("my contact")));
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


