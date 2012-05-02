package com.spitfire.moviemon.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by IntelliJ IDEA.
 * User: Todd
 * Date: 5/1/12
 * Time: 9:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class MovieReservationResult {

    @SerializedName("ConfirmationMessage")
    private String confirmationMessage;

    public String getConfirmationMessage() {
        return confirmationMessage;
    }

    public void setConfirmationMessage(String confirmationMessage) {
        this.confirmationMessage = confirmationMessage;
    }
}
