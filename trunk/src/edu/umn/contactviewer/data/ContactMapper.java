package edu.umn.contactviewer.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: dalcantara
 * Date: 2/27/12
 * Time: 10:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContactMapper {
    
    public static Contact fromJsonString(String contactJson){
        Gson contactMaker = new Gson();
        return (Contact) contactMaker.fromJson(contactJson, new TypeToken<Contact>(){}.getType());
    }

    public static String toJsonString(Contact contact){
        Gson contactMaker = new Gson();
        return contactMaker.toJson(contact);
    }

    public static List<Contact> ListFromJson(InputStreamReader reader) {

        Gson gson = new Gson(); 
        JsonContactsCollection contacts = gson.fromJson(reader, JsonContactsCollection.class);

        return contacts.get_contacts();
    }
}
