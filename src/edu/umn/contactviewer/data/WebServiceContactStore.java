package edu.umn.contactviewer.data;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import com.google.gson.Gson;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: eric
 * Date: 3/30/12
 * Time: 10:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class WebServiceContactStore implements IContactStore {
    
    @Override
    public List<Contact> getContacts() {

        AsycGetContacts getContacts = new AsycGetContacts();
        getContacts.doInBackground();

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Contact update(Contact contact) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Contact saveContact(Contact contact) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void delete(Contact contact) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void Flush() throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    class Results {
    }

    class AsycGetContacts extends AsyncTask<String, Void, Results> {

        private final String URL_BASE = "http://contacts.tinyapollo.com/contacts/?key=spitfire";

        public AsycGetContacts() {
            super();    //To change body of overridden methods use File | Settings | File Templates.
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        
        @Override
        protected void onPostExecute(Results results ) {
            super.onPostExecute(results);
        }
        
        @Override
        protected Results doInBackground(String... params) {
            
            //String contactId = params[0];
            Results results = null;
            AndroidHttpClient client = null;

            try{
                client = AndroidHttpClient.newInstance("Android");
                HttpUriRequest request = new HttpGet(URL_BASE);// + contactId + "?key=demo");
                HttpResponse response = client.execute(request);
                Gson gson = new Gson();
                results = gson.fromJson(new InputStreamReader(response.getEntity().getContent()), Results.class);
            }
            catch (Exception ex){
                String error = ex.getMessage();
                //do something
                int x =5;
            }
            finally{

                if(null != client){
                    client.close();
                }
            }
            return results;
        }
    }
}
