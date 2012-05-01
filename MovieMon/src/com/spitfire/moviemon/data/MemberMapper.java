package com.spitfire.moviemon.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class MemberMapper {

    public static Member memberFromJson(InputStreamReader reader) {
        Gson memberMaker = new Gson();

        return (Member) memberMaker.fromJson(reader, new TypeToken<Member>(){}.getType());
    }

    public static Member memberFromJson(String json) {
        Gson memberMaker = new Gson();

        return (Member) memberMaker.fromJson(json, new TypeToken<Member>(){}.getType());
    }

    public static String toJson(Member member) {
        Gson memberMaker = new Gson();

        return memberMaker.toJson(member);
    }
}
