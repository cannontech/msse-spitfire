package edu.umn.contactviewer;

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

	public IContactStore getContactStore(){
	
		IContactStore store = null;
		
		if(true){	//other logic depending on what/where we get data from
			store = new ContactStore();
		}
		
		return store;
	}	
}
