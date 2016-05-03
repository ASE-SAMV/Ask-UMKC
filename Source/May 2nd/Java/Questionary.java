package com.aseproject.askumkc;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.microedition.khronos.egl.EGLDisplay;

public class Questionary extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    int QuestionID;
    EditText ed1;
    String dataJsonObj;
    private Spinner spinner;
    String categorySelected;
    String Userinfo;
    String authorName;
    List<String> categories = new ArrayList<String>();

    private EditText question;
    private ImageButton btnSpeak;
    Toolbar toolbar;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_questionary);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.your_title);


        Intent intent=getIntent();
        Userinfo=intent.getStringExtra("userData");

        question = (EditText) findViewById(R.id.question);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        //getActionBar().setTitle("Post a question");



        try {
            JSONObject jsonObject = new JSONObject(Userinfo);
            authorName=jsonObject.getString("username");
            Log.d("Author name: ",authorName);
        }
        catch (JSONException e){}
        ed1 = (EditText) findViewById(R.id.question);
        spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        categories.add("Non-Academics");
        categories.add("Academics");
        categories.add("Admissions");
        categories.add("Career");
// Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        categorySelected=item;
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected Category: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("Posted", categorySelected);
        setResult(1, intent);
        finish();
    }

    public void postQuestionToDb(View view) {
        if (ed1.getText().toString().length() > 0) {
            new MongoAsyncTaskQues().execute();
           try {
               JSONObject jsonObject=new JSONObject(dataJsonObj);
               Log.d("JSON value",jsonObject.toString());
                JSONArray jsonArray=jsonObject.getJSONArray("question_list");
               jsonObject=jsonArray.getJSONObject(0);
               int i=jsonObject.getInt("questions");
               i++;
               QuestionID=i;
               jsonObject.put("questions", String.valueOf(i));
               Log.d("Obj", jsonObject.toString());
                              JSONObject x=new JSONObject();
               x.put("$oid","570ebd3ee4b0cbcd095d83d4");
               jsonObject.put("_id",x);
               // Modifying the total questions value
               sample s=new sample();
               s.fun1(jsonObject.toString(),"https://api.mongolab.com/api/1/databases/askumkc/collections/stats?apiKey=7rL6L7EoWpZnPgyjtHDS8Rwh6VDer-ic");
               // Post the question
               String question=ed1.getText().toString();
               JSONObject quesJson=new JSONObject();
//For Updation, Id is imp
//               JSONObject x=new JSONObject();
//               x.put("$oid","56d87b15e4b00cf3135e96b7");
//
//               quesJson.put("_id",x);
               quesJson.put("questionid",QuestionID);
               quesJson.put("category",categorySelected);
               quesJson.put("question",question);
               quesJson.put("author", authorName);
               quesJson.put("upvote",0);
               quesJson.put("downvote",0);
               //Following users are here

               ArrayList sam=new ArrayList();
               quesJson.put("users",new JSONArray(sam));
//               quesJson.put("users",sam);
               ArrayList sam1=new ArrayList();

               quesJson.put("answers", new JSONArray(sam1));
               quesJson.put("upvoted_by",new JSONArray(sam1));
               quesJson.put("downvoted_by",new JSONArray(sam));
               //pushing data to questions database
               s.fun1(quesJson.toString(),"https://api.mongolab.com/api/1/databases/askumkc/collections/questionlist?apiKey=7rL6L7EoWpZnPgyjtHDS8Rwh6VDer-ic");
               Toast.makeText(getApplicationContext(), "Question posted..!!", Toast.LENGTH_LONG).show();
               onBackPressed();

           }
           catch (Exception e)
           { }

        } else {
            Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_LONG).show();
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

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    question.setText(result.get(0));
                }
                break;
            }

        }
    }


}