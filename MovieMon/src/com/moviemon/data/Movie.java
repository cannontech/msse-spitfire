package com.moviemon.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movie{

    @SerializedName("Availability")
   	private List<Availability> availability;

    @SerializedName("Cast")
   	private List<String> cast;

    @SerializedName("MPAARating")
   	private String mPAARating;

    @SerializedName("ProviderMovieId")
   	private String providerMovieId;

    @SerializedName("Providers")
   	private String providers;

    @SerializedName("RelatedClips")
   	private List<String> relatedClips;

    @SerializedName("RelatedImages")
   	private List<RelatedImages> relatedImages;

    @SerializedName("Rating")
    private Rating rating;

    @SerializedName("Reviews")
   	private List<Review> reviews;

    @SerializedName("RunTime")
   	private String runTime;

    @SerializedName("Summary")
   	private String summary;

    @SerializedName("Title")
   	private String title;

    @SerializedName("WatchedDate")
   	private String watchedDate;

   	private String id;

 	public List<Availability> getAvailability(){
		return this.availability;
	}
	public void setAvailability(List<Availability> availability){
		this.availability = availability;
	}
 	public List<String> getCast(){
		return this.cast;
	}
	public void setCast(List<String> cast){
		this.cast = cast;
	}
 	public String getMPAARating(){
		return this.mPAARating;
	}
	public void setMPAARating(String mPAARating){
		this.mPAARating = mPAARating;
	}
 	public String getProviderMovieId(){
		return this.providerMovieId;
	}
	public void setProviderMovieId(String providerMovieId){
		this.providerMovieId = providerMovieId;
	}
 	public String getProviders(){
		return this.providers;
	}
	public void setProviders(String providers){
		this.providers = providers;
	}
    public Rating getRating(){
        return this.rating;
    }
    public void setRating(Rating rating){
        this.rating = rating;
    }
 	public List<String> getRelatedClips(){
		return this.relatedClips;
	}
	public void setRelatedClips(List<String> relatedClips){
		this.relatedClips = relatedClips;
	}
 	public List<RelatedImages> getRelatedImages(){
		return this.relatedImages;
	}
	public void setRelatedImages(List<RelatedImages> relatedImages){
		this.relatedImages = relatedImages;
	}
 	public List<Review> getReviews(){
		return this.reviews;
	}
	public void setReviews(List<Review> reviews){
		this.reviews = reviews;
	}
 	public String getRunTime(){
		return this.runTime;
	}
	public void setRunTime(String runTime){
		this.runTime = runTime;
	}
 	public String getSummary(){
		return this.summary;
	}
	public void setSummary(String summary){
		this.summary = summary;
	}
 	public String getTitle(){
		return this.title;
	}
	public void setTitle(String title){
		this.title = title;
	}
 	public String getWatchedDate(){
		return this.watchedDate;
	}
	public void setWatchedDate(String watchedDate){
		this.watchedDate = watchedDate;
	}
 	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
}
