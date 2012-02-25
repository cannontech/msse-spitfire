package edu.umn.contactviewer;

import java.util.ArrayList;

public interface IContactStore {

	ArrayList<Contact> getContacts();
	
	Contact getContactById(int id);
	
	void saveContact(Contact contact);
}
