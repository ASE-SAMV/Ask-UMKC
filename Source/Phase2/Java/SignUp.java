package com.aseproject.askumkc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.util.JSON;

import org.json.JSONException;
import org.json.JSONObject;

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


    public void onClickSignUp(View view) throws JSONException {
        if(firstname.getText().toString().isEmpty()||lastname.getText().toString().equals("abc")||
                emailid.getText().toString().isEmpty()||username.getText().toString().isEmpty()||password.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_LONG).show();
        }
        else
        {
            JSONObject json = new JSONObject();
            json.put("firstName", firstname.getText().toString());
            json.put("lastName", lastname.getText().toString());
            json.put("phone",phonenumber.getText().toString());
            json.put("emailid", emailid.getText().toString());
            json.put("pwd", password.getText().toString());
            Log.d("Values",json.toString());
            // json.put("pwd2", pwd2);
          DBCollection users;
            DB db;

            MongoClientURI uri  = new MongoClientURI("https://api.mongolab.com/api/1/databases/assignment6/collections/register?apiKey=n3_LdBV6H3Z1uOXstMLQHCgrV9l1aG8P");
            //MongoClientURI uri  = new MongoClientURI("mongodb://vikesh:Kiran86@ds045598.mlab.com:45598/assignment6");
            MongoClient client = new MongoClient(uri);
            db = client.getDB(uri.getDatabase());
            System.out.println("Sysy" + db.getName());
            Log.d("details",db.getName().toString());
            System.out.println(json.toString());
            users = db.getCollection("register");
            Log.d("data", String.valueOf(users));
            String userdata=json.toString();
            DBObject dbObject = (DBObject) JSON.parse(userdata);
            users.insert(dbObject);
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
        }

    }
}