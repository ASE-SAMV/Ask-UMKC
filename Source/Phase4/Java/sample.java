package com.aseproject.askumkc;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by sravan on 4/12/2016.
 */
public class sample {
    sample sampleObj;
    sample()
    {
        Log.d("Sample","Working..!!");
    }
    public  void fun1( String data, String url)
    {
        sampleObj=this;
        new DoBackgroundTask().execute(data,url);
    }
}


     class DoBackgroundTask extends AsyncTask<String, String, String> {

         public String output;

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            String dataToSend = params[0];
            Log.d("FROM   DoBackgroundTask", dataToSend);
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(params[1]);
            try {
                httpPost.setEntity(new StringEntity(dataToSend, "UTF-8"));

                // Set up the header types needed to properly transfer JSON
                httpPost.setHeader("Content-Type", "application/json");
                httpPost.setHeader("Accept-Encoding", "application/json");
                httpPost.setHeader("Accept-Language", "en-US");
                httpPost.setHeader("Access-Control-Allow-Origin","*");
                httpPost.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");

                // Execute POST
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity responseEntity = httpResponse.getEntity();
                if (responseEntity != null) {
                    response = EntityUtils.toString(responseEntity);
                } else {
                    response = "{\"NO DATA:\"NO DATA\"}";
                }
            } catch (ClientProtocolException e) {
                response = "{\"ERROR\":" + e.getMessage().toString() + "}";
            } catch (IOException e) {
                response = "{\"ERROR\":" + e.getMessage().toString() + "}";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            String STATUS = result;
            Log.i("FROM : STATUS IS:", STATUS);
            output=result;
            super.onPostExecute(result);
        }

    }

