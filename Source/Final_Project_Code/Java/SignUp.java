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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUp extends AppCompatActivity {

    public EditText firstname, lastname, phonenumber, emailid, username, password;
    String dataJsonObj;
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

    public void fun1()
    {

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
     if(firstname.getText().toString().isEmpty()||lastname.getText().toString().isEmpty()||
                emailid.getText().toString().isEmpty()||username.getText().toString().isEmpty()||password.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_LONG).show();
        }
        else
        {
            new MongoAsyncTaskQues().execute();
            try{Thread.sleep(500);}
            catch (Exception e)
            {}
            JSONObject jsonObject=new JSONObject(dataJsonObj);
            Log.d("JSON value",jsonObject.toString());
            JSONArray jsonArray=jsonObject.getJSONArray("question_list");
            jsonObject=jsonArray.getJSONObject(0);
            int i=jsonObject.getInt("users");
            i++;
            jsonObject.put("users", i);
            Log.d("Obj", jsonObject.toString());
            JSONObject x=new JSONObject();
            x.put("$oid","570ebd3ee4b0cbcd095d83d4");
            jsonObject.put("_id",x);
            // Modifying the total users value
            sample userMod=new sample();
            userMod.fun1(jsonObject.toString(),"https://api.mongolab.com/api/1/databases/askumkc/collections/stats?apiKey=7rL6L7EoWpZnPgyjtHDS8Rwh6VDer-ic");
            sample s=new sample();
            s.fun1(jsonObject.toString(),"https://api.mongolab.com/api/1/databases/dbsample/collections/records?apiKey=vAC5pceO7ioI5tQoaSPom8kjbrYVFCQl");
            JSONObject json = new JSONObject();
            json.put("userid",i);
            json.put("firstname", firstname.getText().toString());
            json.put("lastname", lastname.getText().toString());
            json.put("phone_number",phonenumber.getText().toString());
            json.put("emailid", emailid.getText().toString());
            json.put("username",username.getText().toString());
            json.put("password", password.getText().toString());
            json.put("img", "");
            Log.d("Values", json.toString());
            s.fun1(json.toString(),"https://api.mongolab.com/api/1/databases/dbsample/collections/records?apiKey=vAC5pceO7ioI5tQoaSPom8kjbrYVFCQl");
            Toast.makeText(getApplicationContext(), "Account Created..!! Login now..!!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
        }
    }

class MongoAsyncTaskQues extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... args0) {
        try {
            try {
                URL url1 = new URL("https://api.mongolab.com/api/1/databases/askumkc/collections/stats?apiKey=7rL6L7EoWpZnPgyjtHDS8Rwh6VDer-ic");
                HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));
                String temp_output = null;
                String server_output = null;
                while ((temp_output = br.readLine()) != null) {
                    server_output = temp_output;
                }
                String mongoarray = "{ question_list: " + server_output + "}";
                dataJsonObj=mongoarray;
            } catch (Exception ex) {
                return null;
            } finally {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
}
