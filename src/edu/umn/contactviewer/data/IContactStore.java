package edu.umn.contactviewer.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface IContactStore {

	List<Contact> getContacts();
	Contact update(Contact contact);
	Contact saveContact(Contact contact);
    void delete(Contact contact);

    void Flush() throws IOException;
}
