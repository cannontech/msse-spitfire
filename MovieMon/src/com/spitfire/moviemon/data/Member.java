package com.spitfire.moviemon.data;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Todd
 * Date: 4/18/12
 * Time: 9:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class Member {

    @SerializedName("Email")
    private String email;

    @SerializedName("Id")
    private String id;

    @SerializedName("LastName")
    private String lastName;

    @SerializedName("Name")
    private String firstName;

    @SerializedName("Phone")
    private String phone;

    @SerializedName("Movies")
    private List<Movie> movieQueue;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Movie> getMovieQueue() {
        return movieQueue;
    }

    public void setMovieQueue(List<Movie> movieQueue) {
        this.movieQueue = movieQueue;
    }
}
