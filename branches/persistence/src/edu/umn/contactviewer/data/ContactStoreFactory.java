package edu.umn.contactviewer.data;

import edu.umn.contactviewer.Contact;

public class ContactStoreFactory {
	
	private static ContactStoreFactory _instance = null;
	
	private ContactStoreFactory(){
		
	}

	public static ContactStoreFactory getInstance(){
	
		if(null == _instance)
		{
			_instance = new ContactStoreFactory();
		}

		return _instance;		 
	}

    public ContactStoreFactory MakeContactStore(){
        return getInstance();
    }

	public IContactStore getContactStore(){
	
		IContactStore store = null;
		
		if(true){	//other logic depending on what/where we get data from
			store = new InternalStorageContactStore(null);
		}
		
		return store;
	}	
}
