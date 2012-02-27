package edu.umn.contactviewer.data;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.umn.contactviewer.Contact;
import edu.umn.contactviewer.R;
import edu.umn.contactviewer.data.IContactStore;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        readFromDisk();

        Bitmap face = null;
        try{
        	//get an image to go with each contact (the same, right now)
        	//face = BitmapFactory.decodeResource(context.getResources(), R.drawable.face);
        }
        catch(Exception ex)
        {
        	String xx = ex.getMessage();
        }
//
//        _contacts.add(new Contact("Malcom Reynolds")
//    		.setEmail("mal@serenity.com")
//    		.setTitle("Captain")
//    		.setPhone("612-555-1234")
//    		.setTwitterId("malcomreynolds")
//    		.setCompany("Company One"));
//    		//.setImage(face));
//
//        _contacts.add(new Contact("Zoe Washburne")
//			.setEmail("zoe@serenity.com")
//			.setTitle("First Mate")
//			.setPhone("612-555-5678")
//			.setTwitterId("zoewashburne")
//			.setCompany("Company Two"));
//			//.setImage(face));
//
//        _contacts.add(new Contact("Hoban Washburne")
//			.setEmail("wash@serenity.com")
//			.setTitle("Pilot")
//			.setPhone("612-555-9012")
//			.setTwitterId("wash")
//			.setCompany("Company Three"));
//			//.setImage(face));
//
//        _contacts.add(new Contact("Jayne Cobb")
//			.setEmail("jayne@serenity.com")
//			.setTitle("Muscle")
//			.setPhone("612-555-3456")
//			.setTwitterId("heroofcanton")
//			.setCompany("Company Four"));
//			//.setImage(face));
//
//        _contacts.add(new Contact("Kaylee Frye")
//			.setEmail("kaylee@serenity.com")
//			.setTitle("Engineer")
//			.setPhone("612-555-7890")
//			.setTwitterId("kaylee")
//			.setCompany("Company Five"));
//			//.setImage(face));
//
//        _contacts.add(new Contact("Simon Tam")
//			.setEmail("simon@serenity.com")
//			.setTitle("Doctor")
//			.setPhone("612-555-4321")
//			.setTwitterId("simontam")
//			.setCompany("Company Six"));
//			//.setImage(face));
//
//        _contacts.add(new Contact("River Tam")
//			.setEmail("river@serenity.com")
//			.setTitle("Doctor's Sister")
//			.setPhone("612-555-8765")
//			.setTwitterId("miranda")
//			.setCompany("Company Seven"));
//			//.setImage(face));
//
//        _contacts.add(new Contact("Shepherd Book")
//			.setEmail("shepherd@serenity.com")
//			.setTitle("Shepherd")
//			.setPhone("612-555-2109")
//			.setTwitterId("shepherdbook")
//			.setCompany("Company Eight"));
//			//.setImage(face));
//        try {
//            Flush();
//        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }

    }

    private void readFromDisk() {
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
