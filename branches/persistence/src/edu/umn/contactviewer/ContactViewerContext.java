package edu.umn.contactviewer;

import android.app.Application;
import android.content.Context;

/**
 * Created by IntelliJ IDEA.
 * User: dalcantara
 * Date: 2/26/12
 * Time: 11:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class ContactViewerContext extends Application {
    private static Context context;

    public void onCreate(){
        super.onCreate();
        ContactViewerContext.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return ContactViewerContext.context;
    }

}
