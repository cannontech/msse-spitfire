package edu.umn.contactviewer.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: eric
 * Date: 4/2/12
 * Time: 10:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsonContactsCollection {

    @SerializedName("status")
    private String _status;
    
    @SerializedName("message")
    private String _message;
    
    @SerializedName("contacts")
    private List<Contact> _contacts;

    public List<Contact> get_contacts() {
        return _contacts;
    }
    
    public int contacts_count() {
        return  _contacts.size();
    }
}
