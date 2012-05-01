package com.spitfire.moviemon.data;

import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.util.Log;
import com.spitfire.moviemon.data.Member;
import com.spitfire.moviemon.data.MemberMapper;
import com.spitfire.moviemon.data.Movie;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dalcantara
 * Date: 4/29/12
 * Time: 9:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class MemberProxy {
    private final String URL_BASE = "http://movieman.apphb.com/api/Members/";
    private final String DEFAULT_MEMEBER_ID = "f98b9048-1324-440f-802f-ebcfab1c5395";

    AndroidHttpClient client = null;

    public Member getDefaultMember(){
        //cache this guy and only refresh it after we've made changes to it.
        Member member = null;
        try
        {
            client = GetClient();
            HttpUriRequest request = new HttpGet(URL_BASE + Uri.encode(DEFAULT_MEMEBER_ID));
            HttpResponse response = client.execute(request);
            member = MemberMapper.memberFromJson(new InputStreamReader(response.getEntity().getContent()));
        }
        catch (IOException e)
        {
            Log.e("HTTP", e.toString());
        }
        finally {
            if (client!=null){
                client.close();
                client=null;
            }
        }

        return member;
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
        Member m = getDefaultMember();
        List<Movie> movies = m.getMovieQueue();
        movies.add(movie);
        putMember(m);
    }

    public void removeFromQueue(Movie movie){
        Member m = getDefaultMember();
        List<Movie> movies = m.getMovieQueue();
        movies.remove(movie);
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
            putMember(m);
        }
    }

    private void putMember(Member member){

        try
        {
            client = GetClient();
            HttpPut put = new HttpPut(URL_BASE);
            put.setHeader("Accept", "application/json");
            put.setHeader("Content-type", "application/json");
            String mem = MemberMapper.toJson(member);
            put.setEntity(new ByteArrayEntity(mem.getBytes()));
            HttpResponse response = client.execute(put);
        }
        catch (IOException e)
        {
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
