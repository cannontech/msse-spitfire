package com.spitfire.moviemon;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class BrowseActivity extends ListActivity
{


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);      
        setContentView(R.layout.list);
        
        HeaderConfig headerConfig = new HeaderConfig(this, "Browse");

        // make some contacts
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("New Releases By Date");
        categories.add("Top 100");
        categories.add("Action & Adventure");
                  
        // initialize the list view
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories));
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        
        // handle the item click events
        lv.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(), 
					"Clicked: " + getListAdapter().getItem(position),
					Toast.LENGTH_SHORT).show();
				}
			}
        );
        
    }
    
}