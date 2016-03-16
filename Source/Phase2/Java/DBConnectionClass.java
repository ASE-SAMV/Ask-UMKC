package com.aseproject.askumkc;

import android.util.Log;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.util.JSON;

import org.json.JSONObject;

/**
 * Created by sravan on 3/11/2016.
 */
public class DBConnectionClass {

    public DBCollection users;
    DB db;

    DBConnectionClass()
    {
        MongoClientURI uri  = new MongoClientURI("mongodb://vikesh:Kiran86@ds045598.mlab.com:45598/assignment6");
        MongoClient client = new MongoClient(uri);
        db = client.getDB(uri.getDatabase());
        System.out.println("Sysy" + db.getName());
    }

    public void insertuser(JSONObject j,String s)
    {
        System.out.println(j.toString());
        users = db.getCollection(s);
        Log.d("data", String.valueOf(users));
        String userdata=j.toString();
        DBObject dbObject = (DBObject) JSON.parse(userdata);
        users.insert(dbObject);
    }
}
