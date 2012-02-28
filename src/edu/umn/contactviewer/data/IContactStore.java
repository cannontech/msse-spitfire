package edu.umn.contactviewer.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface IContactStore {

	List<Contact> getContacts();
	
	Contact getContactByName(String name);
	
	Contact saveContact(Contact contact);

    Contact getContactById(int offset);
    void Flush() throws IOException;
}
