package com.spitfire.moviemon.data;

import com.google.gson.annotations.SerializedName;

public class Availability{

    @SerializedName("AvailableFrom")
   	private String availableFrom;

    @SerializedName("AvailableTo")
   	private String availableTo;

    @SerializedName("DeliveryFormat")
   	private String deliveryFormat;

    @SerializedName("ProviderName")
   	private String providerName;

 	public String getAvailableFrom(){
		return this.availableFrom;
	}
	public void setAvailableFrom(String availableFrom){
		this.availableFrom = availableFrom;
	}
 	public String getAvailableTo(){
		return this.availableTo;
	}
	public void setAvailableTo(String availableTo){
		this.availableTo = availableTo;
	}
 	public String getDeliveryFormat(){
		return this.deliveryFormat;
	}
	public void setDeliveryFormat(String deliveryFormat){
		this.deliveryFormat = deliveryFormat;
	}
 	public String getProviderName(){
		return this.providerName;
	}
	public void setProviderName(String providerName){
		this.providerName = providerName;
	}
}
