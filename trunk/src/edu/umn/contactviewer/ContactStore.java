package edu.umn.contactviewer;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * storage methods for the contacts (to json??)
 * */
public class ContactStore extends Activity {
	
	public static final String CONTACTS = "ContactsFile";
	
	private ArrayList<Contact> _contacts;
//	SharedPreferences _contacts;
//	
//	@Override
//	protected void onCreate(Bundle state){
//		super.onCreate(state);
//		
//		_contacts = getSharedPreferences(CONTACTS, 0);
//	}
//
//    @Override
//    protected void onStop(){
//       super.onStop();
//
//      // We need an Editor object to make preference changes.
//      // All objects are from android.context.Context
////      SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
////      SharedPreferences.Editor editor = settings.edit();
////      editor.putBoolean("silentMode", mSilentMode);
////
////      // Commit the edits!
////      editor.commit();
//    }
    
	public ContactStore(){
        
		// make some contacts
        _contacts = new ArrayList<Contact>();
        
        _contacts.add(new Contact("Malcom Reynolds")
    		.setEmail("mal@serenity.com")
    		.setTitle("Captain")
    		.setPhone("612-555-1234")
    		.setTwitterId("malcomreynolds"));
        
        _contacts.add(new Contact("Zoe Washburne")
			.setEmail("zoe@serenity.com")
			.setTitle("First Mate")
			.setPhone("612-555-5678")
			.setTwitterId("zoewashburne"));
        
        _contacts.add(new Contact("Hoban Washburne")
			.setEmail("wash@serenity.com")
			.setTitle("Pilot")
			.setPhone("612-555-9012")
			.setTwitterId("wash"));
        
        _contacts.add(new Contact("Jayne Cobb")
			.setEmail("jayne@serenity.com")
			.setTitle("Muscle")
			.setPhone("612-555-3456")
			.setTwitterId("heroofcanton"));
        
        _contacts.add(new Contact("Kaylee Frye")
			.setEmail("kaylee@serenity.com")
			.setTitle("Engineer")
			.setPhone("612-555-7890")
			.setTwitterId("kaylee"));
        
        _contacts.add(new Contact("Simon Tam")
			.setEmail("simon@serenity.com")
			.setTitle("Doctor")
			.setPhone("612-555-4321")
			.setTwitterId("simontam"));
        
        _contacts.add(new Contact("River Tam")
			.setEmail("river@serenity.com")
			.setTitle("Doctor's Sister")
			.setPhone("612-555-8765")
			.setTwitterId("miranda"));
        
        _contacts.add(new Contact("Shepherd Book")
			.setEmail("shepherd@serenity.com")
			.setTitle("Shepherd")
			.setPhone("612-555-2109")
			.setTwitterId("shepherdbook"));
	}
	
	public ArrayList<Contact> getContacts(){

		return _contacts;
	}
	
	public Contact getContactById(int id){

		return _contacts.get(id);
	}
	
	public void saveContact(Contact contact){
		
		throw new UnsupportedOperationException("Feature not implemented yet...");
	}
}
