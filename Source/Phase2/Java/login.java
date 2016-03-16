package com.aseproject.askumkc;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class login extends AppCompatActivity {

   public EditText et1,et2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d("Login", "Created");
        et1=(EditText)findViewById(R.id.editText);
        et2=(EditText)findViewById(R.id.editText2);
    }
    public void login(View view)
    {
        DBCollection users;
        DB db;
        Boolean validUser=false;

        if(et1.getText().toString().length()>0 && et2.getText().toString().length()>0) {
            MongoClientURI uri  = new MongoClientURI("https://api.mongolab.com/api/1/databases/assignment6/collections/register?apiKey=n3_LdBV6H3Z1uOXstMLQHCgrV9l1aG8P");
            //MongoClientURI uri  = new MongoClientURI("mongodb://vikesh:Kiran86@ds045598.mlab.com:45598/assignment6");
            MongoClient client = new MongoClient(uri);
            db = client.getDB(uri.getDatabase());
            System.out.println("Sysy" + db.getName());
            Log.d("details", db.getName().toString());
            users = db.getCollection("register");
            Log.d("data", String.valueOf(users));

            BasicDBObject query = new BasicDBObject();
            query.put("username","");
            query.put("password","");
            DBCursor docs = users.find(query);
            JSONArray ja=new JSONArray(docs.toArray());
            JSONObject jO=new JSONObject();
            try {
                jO = (JSONObject) ja.getJSONObject(0);
                if (jO!=null)
                    validUser=true;
            }
            catch (JSONException e)
            {
                Log.d("message",e.getMessage().toString());
            }
            if(validUser) {
                Intent intent = new Intent(this, HomeScreen.class);
                startActivity(intent);
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_LONG).show();
        }
    }

    public void Register(View view)
    {
        Intent intent= new Intent(this,SignUp.class);
        startActivity(intent);
    }
    public void loginWithGoogle(View view)
    {
        //Need to update
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }
}
