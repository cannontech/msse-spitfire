package com.spitfire.moviemon;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.spitfire.moviemon.data.Movie;
import com.spitfire.moviemon.data.MovieMapper;

public class BrowseActivity extends ListActivity
{
	// need to change for browse?
    private final static String URL_BASE = "http://movieman.apphb.com/api/Movies/";
    private ProgressDialog progressDialog;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);      
        setContentView(R.layout.list);
        
        HeaderConfig headerConfig = new HeaderConfig(this, "Browse");

        // dummy data
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("New Releases By Date");
        categories.add("Top 100");
        categories.add("Action & Adventure");
                  
        // initialize the category list view
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories));
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        
        context = this;
        // handle the category item click events
        lv.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, MovieListActivity.class);
                intent.putExtra("screenTitle", ((TextView)view).getText().toString());
             // url needs to change - test only !!!
                intent.putExtra("url", URL_BASE + "Godfather");
                startActivity(intent);
				}
			});
        
    }
    
}