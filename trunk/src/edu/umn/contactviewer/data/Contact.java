package edu.umn.contactviewer.data;

import android.graphics.Bitmap;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/** Model class for storing a single contact.
 *
 */
public class Contact {

//    @SerializedName("status")
//    private String _status;
//
//    @SerializedName("message")
//    private String _message;

    //@SerializedName("group")

    @SerializedName("name")
	private String _name;

    @SerializedName("phone")
    private String _phone;

    @SerializedName("title")
    private String _title;

    @SerializedName("email")
    private String _email;

    @SerializedName("twitterId")
    private String _twitterId;

    @SerializedName("_id")
    private String _id;

	public Contact(String name) {
		_name = name;
	}
	
	public Contact setName(String name) {
		_name = name;
		return this;
	}

	public String getName() {
		return _name;
	}
	
	public String getPhone() {
		return _phone;
	}

	public Contact setPhone(String phone) {
		_phone = phone;
		return this;
	}
	
	public String getTitle() {
		return _title;
	}

	public Contact setTitle(String title) {
		_title = title;
		return this;
	}
	
	public String getEmail() {
		return _email;
	}

	public Contact setEmail(String email) {
		_email = email;
		return this;
	}
	
	public String getTwitterId() {
		return _twitterId;
	}

	public Contact setTwitterId(String twitterId) {
		_twitterId = twitterId;
		return this;
	}

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

	public String toString() {
		return _name + " (" + _title + ")";
	}
}

