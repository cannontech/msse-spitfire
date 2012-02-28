package edu.umn.contactviewer.data;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: dalcantara
 * Date: 2/25/12
 * Time: 5:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContactCacheProvider implements ICache<Contact> {

    private static List<Contact> _contacts = new ArrayList<Contact>();

    
    public void Add(Contact contact) {
        _contacts.add(contact);
    }

    
    public Contact Get(String key) {
        Contact theContact = null;
        for (Contact contact : _contacts){
            if (contact.getName()==key){
                theContact=contact;
            }
        }
        return theContact;
    }
    
    public List<Contact> GetAll() {
        return _contacts; 
    }
}
