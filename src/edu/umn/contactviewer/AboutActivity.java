package edu.umn.contactviewer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by IntelliJ IDEA.
 * User: lemmej1
 * Date: 3/1/12
 * Time: 10:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class AboutActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.about);

        ToolbarConfig tb = new ToolbarConfig(this, "About");
        
        String aboutText;
        aboutText = "Contact Viewer version 1.0 \n"
                   + "March 3, 2012 \n"
                   + "Spitfire and the TroubleShooters \n"
                   + "Niki Anderson \n"
                   + "Danny Alcantara \n"
                   + "Todd Becicka \n"
                   + "Jeff Lemmerman \n"
                   + "Eric Schmit";

        TextView tv = (TextView)findViewById(R.id.txtAbout);
        tv.setText(aboutText);
    }

}
