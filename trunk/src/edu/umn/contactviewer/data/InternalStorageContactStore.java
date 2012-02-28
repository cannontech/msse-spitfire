package edu.umn.contactviewer.data;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.umn.contactviewer.R;

/**
 * storage methods for the contacts (to json??)
 * */
public class InternalStorageContactStore implements IContactStore {
	
	public static final String CONTACTS_FILE = "ContactsFile.json";
	private List<Contact> _contacts;
    private Context _applicationContext;

	//public InternalStorageContactStore(Context context){
	public InternalStorageContactStore(Context context){


        _contacts = new ArrayList<Contact>();
        _applicationContext = context;
        loadContacts();

        Bitmap face = null;
        try{
        	//get an image to go with each contact (the same, right now)
        	//face = BitmapFactory.decodeResource(context.getResources(), R.drawable.face);
        }
        catch(Exception ex)
        {
        	String xx = ex.getMessage();
        }
    }

    private void loadContacts() {
        String contents = null;
        StringBuilder builder = null;
        InputStream stream = null;
        boolean isFirstRun = hasBeenSaved();        
        try{    
            if (isFirstRun){
                stream = _applicationContext.openFileInput(CONTACTS_FILE);
            }else{                
                stream = _applicationContext.getResources().openRawResource(R.raw.contacts);
            }
            
            builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            if (stream!=null) {
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                stream.close();
            }            
            contents = builder.toString();
            Gson contactMaker = new Gson();
            Type collectionType = new TypeToken<List<Contact>>(){}.getType();
            _contacts = contactMaker.fromJson(contents, collectionType);
            if (isFirstRun){
                Flush(); //let's copy the contents to local storage.
            }

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private boolean hasBeenSaved() {
        String[] files = _applicationContext.fileList();
        boolean fileExists = false;
        for (String file: files){
            if (file.toLowerCase()==CONTACTS_FILE.toLowerCase()){
                fileExists = true;
                break;
            }
        }
        return fileExists;
    }

    
	public List<Contact> getContacts(){
		return _contacts;
	}

    
    public Contact getContactByName(String name){

        Contact theContact = null;
        for (Contact contact : _contacts){
            if (contact.getName()==name){
                theContact=contact;
            }
        }

		return theContact;
	}
	
	public Contact saveContact(Contact contact){
		_contacts.add(contact);
        try {
            Flush();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return contact;
	}

    
    public Contact getContactById(int offset) {
        return _contacts.get(offset);
    }

    
    public void Flush() throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(_contacts);
        FileOutputStream jsonFile = null;
        try {
            jsonFile = _applicationContext.getApplicationContext().openFileOutput(CONTACTS_FILE, Context.MODE_PRIVATE);
            jsonFile.write(json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }finally {
            if (jsonFile!=null){
                jsonFile.flush();
                jsonFile.close();
            }

        }

    }

}
