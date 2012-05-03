package com.spitfire.moviemon.data;

import android.net.http.AndroidHttpClient;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dalcantara
 * Date: 4/29/12
 * Time: 9:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class MemberProxy {

     //private final String URL_BASE = "http://10.0.2.2/MovieMon/api/Members";
    private final String URL_BASE = "http://movieman.apphb.com/api/Members/";

    private final String DEFAULT_MEMEBER_ID = "f98b9048-1324-440f-802f-ebcfab1c5395";

    AndroidHttpClient client = null;
    private static Member theDefaultMember = null;
    public Member getDefaultMember(){
        //cache this guy and only refresh it after we've made changes to it.
        if (theDefaultMember ==null){
            try {
                client = GetClient();
                HttpUriRequest request = new HttpGet(URL_BASE);
                HttpResponse response = client.execute(request);
                theDefaultMember = MemberMapper.fromCollection(new InputStreamReader(response.getEntity().getContent()));
            }
            catch (Exception e) {
                Log.e("HTTP", e.toString());
            }
            finally {
                close();
            }
        }
        return theDefaultMember;
    }

    private AndroidHttpClient GetClient() {
        if (client == null){
            client = AndroidHttpClient.newInstance("MovieMon", null);
        }
        return client;
    }

    public void close() {
        if (client!=null){
            client.close();
            client=null;
        }
    }

    public void addToQueue(Movie movie){
        Member member = getDefaultMember();
        List<Movie> movies = member.getMovieQueue();
        boolean isInQ=false;

        for (Movie m:movies){
            if (m.getTitle().equals(movie.getTitle())){
                isInQ=true;
                break;
            }
        }
        if(!isInQ){
            movies.add(movie);
            putMember(member);
        }
    }

    public void removeFromQueue(Movie movie){
        Member m = getDefaultMember();
        List<Movie> movies = m.getMovieQueue();
        for(int i=0;i<movies.size();i++){
            Movie movieToRemove=movies.get(i);
            if (movie.getTitle().equals(movieToRemove.getTitle())){
                m.getMovieQueue().remove(i);
                break;
            }
        }
        putMember(m);
    }

    public void markAsWatched(Movie movie){}

    public void rateMovie(String title, String comment, int rating){
        Member m = getDefaultMember();
        List<Movie> movies = m.getMovieQueue();
        Movie movieToRate = null;

        for (Movie movie:movies){
            if (movie.getTitle().equals(title)){
                movieToRate=movie;
                break;
            }
        }

        if (movieToRate!=null){
            movieToRate.getKey().setRating(rating);
            movieToRate.getKey().setComment(comment);
            movieToRate.getKey().setWasWatched(true);
            movieToRate.getKey().setWatchedDateTime(new Date());
            putMember(m);
        }
    }

    private void putMember(Member member){

        try {
            client = GetClient();
            HttpPut put = new HttpPut(URL_BASE);
            put.setHeader("Accept", "application/json");
            put.setHeader("Content-type", "application/json");
            String mem = MemberMapper.toJson(member);
            put.setEntity(new ByteArrayEntity(mem.getBytes()));
            HttpResponse response = client.execute(put);
            theDefaultMember = MemberMapper.memberFromJson(new InputStreamReader(response.getEntity().getContent()));

        }
        catch (IOException e) {
            Log.e("HTTP", e.toString());
        }
        finally {
            if (client!=null){
                client.close();
                client=null;
            }
        }
    }
}
