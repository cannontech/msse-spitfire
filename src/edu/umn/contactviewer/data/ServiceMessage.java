package edu.umn.contactviewer.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created with IntelliJ IDEA.
 * User: eric
 * Date: 4/9/12
 * Time: 10:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServiceMessage {

    @SerializedName("status")
    private String _status;

    @SerializedName("message")
    private String _message;

    public String get_status() {
        return _status;
    }

    public String get_message() {
        return _message;
    }

    public void set_message(String _message) {
        this._message = _message;
    }

    public void set_status(String _status) {
        this._status = _status;
    }
}
