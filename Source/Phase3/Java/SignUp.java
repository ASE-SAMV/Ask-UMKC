package com.aseproject.askumkc;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUp extends AppCompatActivity {

    public EditText firstname, lastname, phonenumber, emailid, username, password;
    Button bn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("SignUp","Created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firstname = (EditText) findViewById(R.id.first_Name);
        lastname = (EditText) findViewById(R.id.last_Name);
        phonenumber = (EditText) findViewById(R.id.phone);
        emailid = (EditText) findViewById(R.id.email_id);
        username = (EditText) findViewById(R.id.user_Name);
        password = (EditText) findViewById(R.id.password);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(SignUp.this, login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void onClickSignUp(View view) throws JSONException {

            //MongoAsyncTask mongoAsyncTask=new MongoAsyncTask();
       new MongoAsyncTask().execute();

//        if(firstname.getText().toString().isEmpty()||lastname.getText().toString().equals("abc")||
//                emailid.getText().toString().isEmpty()||username.getText().toString().isEmpty()||password.getText().toString().isEmpty())
//        {
//            Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_LONG).show();
//        }
//        else
//        {
//            JSONObject json = new JSONObject();
//            json.put("firstName", firstname.getText().toString());
//            json.put("lastName", lastname.getText().toString());
//            json.put("phone",phonenumber.getText().toString());
//            json.put("emailid", emailid.getText().toString());
//            json.put("pwd", password.getText().toString());
//            Log.d("Values",json.toString());
//            // json.put("pwd2", pwd2);
////          DBCollection users;
////            DB db;
////
////            MongoClientURI uri  = new MongoClientURI("https://api.mongolab.com/api/1/databases/assignment6/collections/register?apiKey=n3_LdBV6H3Z1uOXstMLQHCgrV9l1aG8P");
////            //MongoClientURI uri  = new MongoClientURI("mongodb://vikesh:Kiran86@ds045598.mlab.com:45598/assignment6");
////            MongoClient client = new MongoClient(uri);
////            db = client.getDB(uri.getDatabase());
////            System.out.println("Sysy" + db.getName());
////            Log.d("details",db.getName().toString());
////            System.out.println(json.toString());
////            users = db.getCollection("register");
////            Log.d("data", String.valueOf(users));
////            String userdata=json.toString();
////            DBObject dbObject = (DBObject) JSON.parse(userdata);
////            users.insert(dbObject);
//            Intent intent = new Intent(this, login.class);
//            startActivity(intent);
//        }

    }
}

/*class MongoAsyncTask extends AsyncTask<Void, Void, Void> {
    public String fname;

    MongoAsyncTask()
    {

    }

   *//* MongoAsyncTask(JSONObject j) throws JSONException {
        fname=j.getString("fname");
Log.d("in MongoAsynch methos.....",fname);
    }*//*


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
           request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            Log.d("in the background end","bashkldjsaf");
            // To Retrieve
            // String url = "https://api.mla-b.com/api/1/databases/user/collections/register?apiKey=3xzlRS5DzNYSRv9h5bfjnZzCyD8q98Z9";

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


               *//* HttpGet httpGet = new HttpGet(url);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse togetdata = httpclient.execute(httpGet);
                InputStream pleasedata = togetdata.getEntity().getContent();

                String jsondata = pleasedata.toString();
                Log.d("wanted details", jsondata.toString());*//*

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

class QueryBuilder {
    public String getDatabaseName() {

        //return "user";
        return "firstdetails";
    }

    public String getApiKey()
    {
        //return "3xzlRS5DzNYSRv9h5bfjnZzCyD8q98Z9";

        return "7rL6L7EoWpZnPgyjtHDS8Rwh6VDer-ic";
    }

    public String getBaseUrl()
    {
        return "https://api.mongolab.com/api/1/databases/"+getDatabaseName()+"/collections/";
    }

    public String docApiKeyUrl()
    {
        return "?apiKey="+getApiKey();
    }
    public String docApiKeyUrl(String docid)
    {
        return "/"+docid+"?apiKey="+getApiKey();
    }

    public String documentRequest()
    {
        return "register";
    }
    public String buildContactsSaveURL()
    {
        return getBaseUrl()+documentRequest()+docApiKeyUrl();


    public String buildContactsGetURL()
    {
        return getBaseUrl()+documentRequest()+docApiKeyUrl();
    }
    public String buildContactsSaveURL(String doc_id)
    {
        return getBaseUrl()+documentRequest()+docApiKeyUrl(doc_id);
    }


    public String createContact(JSONObject j) throws JSONException {
        return String
                .format("{\"fname\": \"%s\", "
                                + "\"lastname\": \"%s\", \"number\": \"%s\"}",
                        j.getString("fname"),j.getString("lastname"), j.getString("number"));

    }*/
//}