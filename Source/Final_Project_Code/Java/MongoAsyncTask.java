/**
 * Created by sravan on 4/6/2016.
 */
package com.aseproject.askumkc;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MongoAsyncTask extends AsyncTask<Void, Void, Void> {
    public String fname;

    MongoAsyncTask()
    {

    }

   /* MongoAsyncTask(JSONObject j) throws JSONException {
        fname=j.getString("fname");
Log.d("in MongoAsynch methos.....",fname);
    }*/


    @Override
    protected Void doInBackground(Void... args0) {
        try{
            Log.d("IN backgroind method","ajkfajdfhadjkf");
            QueryBuilder qb = new QueryBuilder();

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost request = new HttpPost(qb.buildContactsSaveURL());
            JSONObject j = new JSONObject();

            j.put("number","9959948383");
            j.put("lastname","Appana");
            j.put("fname","Sravan Kumar");



            StringEntity params =new StringEntity(qb.createContact(j));
            Log.d("In mongoasynch taks ",j.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            Log.d("in the background end","bashkldjsaf");
            // To Retrieve
            // String url = "https://api.mlab.com/api/1/databases/user/collections/register?apiKey=3xzlRS5DzNYSRv9h5bfjnZzCyD8q98Z9";

            try {
                Log.d("IN try block","ajsdfkjafhjkdahfjd");
                QueryBuilder qb1 = new QueryBuilder();
                URL url1 = new URL(qb.buildContactsGetURL());
                HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                Log.d("IN try block", "Before IF COndition.........................................");
                if (conn.getResponseCode() != 200) {
                    Log.d("IN try block", "In IF COndition.........................................");
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());

                }
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));
                String temp_output = null;
                String server_output = null;
                String OriginalObject = "";
                // BasicDBObject user = null;
                Log.d("IN try block", "Before WHILE COndition.........................................");
                while ((temp_output = br.readLine()) != null) {
                    Log.d("IN try block", "IN while COndition.........................................");
                    server_output = temp_output;
                }
                Log.d("IN try block", "after while COndition.........................................");
                String mongoarray = "{ artificial_basicdb_list: "+server_output+"}";
                System.out.println(mongoarray);
                Log.d("mongo array data.....",mongoarray.toString());
                Log.d("",mongoarray);
//                JSONObject mgarray=new JSONObject(mongoarray);
//                Object o = mongoarray;
//                DBObject dbObj = (DBObject) o;
//                BasicDBList contacts = (BasicDBList) dbObj.get("artificial_basicdb_list");
//                for (Object obj : contacts) {
//                    DBObject userObj = (DBObject) obj;
//                    Log.d("IN try block", "In FOR COndition.........................................");
//
//                    String firstname=(userObj.get("fname").toString());
//                    String lastnameva=(userObj.get("lastname").toString());
//                    Log.d("First name from DB",firstname);
//
//                }


// create a basic db list


               /* HttpGet httpGet = new HttpGet(url);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse togetdata = httpclient.execute(httpGet);
                InputStream pleasedata = togetdata.getEntity().getContent();

                String jsondata = pleasedata.toString();
                Log.d("wanted details", jsondata.toString());*/

            } catch (Exception ex) {
                Log.e("App", "yourDataTask", ex);
                return null;
            } finally {

            }





        }
        catch (Exception e)
        {
            e.printStackTrace();

        }

        return null;
    }
}
