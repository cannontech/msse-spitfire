package com.spitfire.moviemon;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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