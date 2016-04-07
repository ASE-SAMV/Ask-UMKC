package com.aseproject.askumkc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import javax.microedition.khronos.egl.EGLDisplay;

public class Questionary extends AppCompatActivity {

    EditText ed1;
    int QuestionID=9999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionary);
        ed1=(EditText)findViewById(R.id.question);

    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent=getIntent();
        setResult(1,intent);
        finish();
    }
    public void postQuestionToDb(View view) {
        if (ed1.getText().toString().length() > 0) {
//            MongoClientURI uri = new MongoClientURI("https://api.mongolab.com/api/1/databases/assignment6/collections/register?apiKey=n3_LdBV6H3Z1uOXstMLQHCgrV9l1aG8P");
//            MongoClient client = new MongoClient(uri);
//
//            DB db = client.getDB(uri.getDatabase());
//            DBCollection users = db.getCollection("questions");
//            System.out.println(users.getName());
//            JSONObject sample=new JSONObject();
//            try {
//                sample.put("Question", ed1.getText().toString());
//                sample.put("QuestionID",QuestionID++);
//                sample.put("Upvotes",0);
//                sample.put("Downvotes",0);
//            }
//            catch (JSONException e) {
//
//            }
//            DBObject dbObject= (DBObject) JSON.parse(sample.toString());
//
//            users.insert(dbObject);

            Toast.makeText(getApplicationContext(), "Data posted..!!", Toast.LENGTH_LONG).show();
            onBackPressed();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_LONG).show();
        }
    }
}
