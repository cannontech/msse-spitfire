package com.moviemon.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Todd
 * Date: 4/1/12
 * Time: 11:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class MovieMapper {
    public static Movie movieFromJson(String json)
    {
        Gson movieMaker = new Gson();
        return (Movie) movieMaker.fromJson(json, new TypeToken<Movie>(){}.getType());
    }

    public static List<Movie> listFromJson(InputStreamReader reader)
    {
        Gson movieMaker = new Gson();
        Type collectionType = new TypeToken<List<Movie>>(){}.getType();
        return movieMaker.fromJson(reader, collectionType);
    }

    public static String toJson(Movie movie)
    {
        Gson movieMaker = new Gson();
        return movieMaker.toJson(movie);
    }
}
