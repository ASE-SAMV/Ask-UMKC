package com.aseproject.askumkc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    ListView listview;
    ArrayList<String> Admissions = new ArrayList<String>();
    ArrayList<String> Non_Academics = new ArrayList<String>();
    ArrayList<String> Academics = new ArrayList<String>();
    ArrayList<String> Career = new ArrayList<String>();
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("Home", "Created");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
         MongoAsyncTaskHome mongoAsyncTaskHome=new MongoAsyncTaskHome();
         mongoAsyncTaskHome.homeScreen=this;
         mongoAsyncTaskHome.execute();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
         listview = (ListView) findViewById(R.id.listview);
         try {
             Thread.sleep(3000);
         }
         catch (Exception e)
         {
            Log.d("Wait","Not wokring");
         }
         basicAdapter BA=new basicAdapter(this,Academics);
         BA.homeScreen=this;

         listview.setAdapter(BA);
         ((BaseAdapter) listview.getAdapter()).notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

       @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent=getIntent();
            setResult(1, intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            Intent intent= new Intent(this,ProfileUpdate.class);
            startActivityForResult(intent, 1);

            // Handle the camera action
        } else if (id == R.id.logout) {
            Intent intent=getIntent();
            setResult(1, intent);
            finish();

        } else if (id == R.id.nav_slideshow) {
            MenuItem Item=item;
            item.setEnabled(true);
            listview = (ListView) findViewById(R.id.listview);
            basicAdapter basicAdapter=new basicAdapter(this,Admissions);
            listview.setAdapter(basicAdapter);
            ((BaseAdapter) listview.getAdapter()).notifyDataSetChanged();

        } else if (id == R.id.nav_manage) {
            item.setEnabled(true);
            listview = (ListView) findViewById(R.id.listview);
            basicAdapter basicAdapter=new basicAdapter(this,Career);
            listview.setAdapter(basicAdapter);
            ((BaseAdapter) listview.getAdapter()).notifyDataSetChanged();
        }
        else if (id == R.id.nav_gallery) {
            item.setEnabled(true);
            listview = (ListView) findViewById(R.id.listview);
            basicAdapter basicAdapter=new basicAdapter(this,Non_Academics);
            listview.setAdapter(basicAdapter);
            ((BaseAdapter) listview.getAdapter()).notifyDataSetChanged();
        }
        else if (id == R.id.nav_camera) {
            item.setEnabled(true);
            listview = (ListView) findViewById(R.id.listview);
            basicAdapter basicAdapter=new basicAdapter(this,Academics);
            listview.setAdapter(basicAdapter);
            ((BaseAdapter) listview.getAdapter()).notifyDataSetChanged();
        }
        else if (id == R.id.myQuestions) {
            Toast.makeText(this, "Updating..!!",
                    Toast.LENGTH_LONG).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void onClickPostQuestion(View view)
    {
        Intent intent= new Intent(this,Questionary.class);
        startActivityForResult(intent, 1);

    }
    public void setAdapterData(String dataArray)
    {
       // Log.d("In Home Screen",dataArray);
        try {

            JSONObject obj = new JSONObject(dataArray);
            JSONArray jsonArray=obj.getJSONArray("question_list");
            int len=jsonArray.length();
            for(int i=0;i<len;i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String cat = jsonObject.getString("category");

                switch (cat)
                {
                    case "Career":Career.add(jsonObject.toString());
                        break;
                    case "Admissions":Admissions.add(jsonObject.toString());
                        break;
                    case "Non Academics":Non_Academics.add(jsonObject.toString());
                        break;
                    case "Academics":Academics.add(jsonObject.toString());
                        break;
                }

            }

            Log.d("career", String.valueOf(Career.size()));
            Log.d("Adm", String.valueOf(Admissions.size()));
            Log.d("N_A", String.valueOf(Non_Academics.size()));
            Log.d("Acad", String.valueOf(Academics.size()));

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + dataArray + "\"");
        }

    }
    public void AnswersList(String data)
    {
        Intent intent= new Intent(this,AnswerList.class);
        intent.putExtra("Data",data);
        startActivityForResult(intent, 1);
    }

}


class basicAdapter extends BaseAdapter {
       public Context context;
     public HomeScreen homeScreen;
   public ArrayList<String> data;
    Boolean upFocus=false,downFocus=false;
    private static LayoutInflater inflater = null;

    public basicAdapter(Context context, ArrayList<String> data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub0
        return position;
    }
                @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
                    int i=10,j=110;

         final int Position=position;
      final  RowContent rowContent;
        if (vi == null)
            vi = inflater.inflate(R.layout.row, null);
            rowContent=new RowContent();
            rowContent.ques = (TextView) vi.findViewById(R.id.headText);
            rowContent.upImg=(ImageButton)vi.findViewById(R.id.correct);
            rowContent.upVote=(TextView)vi.findViewById(R.id.upnum);
            rowContent.downImg=(ImageButton)vi.findViewById(R.id.wrong);
            rowContent.downVote=(TextView)vi.findViewById(R.id.dwnnum);
            rowContent.ans = (Button) vi.findViewById(R.id.text);
        rowContent.flw=(Button)vi.findViewById(R.id.follow);
                    //Set questions and anserws and other
                    try {
                        JSONObject obj = new JSONObject(data.get(position));
                        rowContent.ques.setText("Question: " + obj.getString("question"));
                        JSONArray jsonArray=obj.getJSONArray("answers");
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        rowContent.ans.setText("Answer: " + jsonObject.getString("answer"));
                        String val=String.valueOf(jsonObject.getInt("downvote"));
                        rowContent.downVote.setText(val);
                        val=String.valueOf(jsonObject.getInt("upvote"));
                        rowContent.upVote.setText(val);
                    }
                    catch (JSONException e)
                    {
                            Log.d("ERROR..!!","You are screwed..!!!!");
                    }

       rowContent.upImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                rowContent.upImg.setImageResource(R.drawable.uptouch1);
                upFocus=true;
               Boolean val= check();
                String s1=rowContent.downImg.getTag().toString();
                Log.d("Value of the button",s1);
//                String s=rowContent.downImg.get

                if(!val)
                {
                    rowContent.downImg.setImageResource(R.drawable.unlike);
                    String s=rowContent.downVote.getText().toString();
                    int i=Integer.parseInt(s);
                    i--;
                    s=String.valueOf(i);
                    rowContent.downVote.setText(s);
                }

                String s = rowContent.upVote.getText().toString();
                int i = Integer.parseInt(s);
                i++;
                s = String.valueOf(i);
                rowContent.upVote.setText(s);
                Log.d("up Button Clicked", "**********");
//                Toast.makeText(context, "UpVote button Clicked",
//                        Toast.LENGTH_LONG).show();
            }
        });


        rowContent.downImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                downFocus=true;
               Boolean val= check1();
                String s1=rowContent.upImg.getTag().toString();
                Log.d("Value of the button", s1);
                if(!val)
                {
                    String s=rowContent.upVote.getText().toString();
                    int i=Integer.parseInt(s);
                    i--;
                    s=String.valueOf(i);
                    rowContent.upVote.setText(s);
                    rowContent.upImg.setImageResource(R.drawable.social);
                }
                rowContent.downImg.setImageResource(R.drawable.unliketouch1);
                String s=rowContent.downVote.getText().toString();
                int i=Integer.parseInt(s);
                i++;
                s=String.valueOf(i);
                rowContent.downVote.setText(s);
                Log.d("down Button Clicked", "**********");
//                Toast.makeText(context, "DownVote button Clicked",
//                        Toast.LENGTH_LONG).show();
            }
        });

        rowContent.flw.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.d("Follow Button Clicked", "**********");
                Toast.makeText(context, "Question followed",
                        Toast.LENGTH_LONG).show();
            }
        });
                rowContent.ans.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        homeScreen.AnswersList(data.get(Position));

                    }
                });

         return vi;
    }
    public Boolean check()
    {
        if(downFocus)
        {
            downFocus=false;
            return false;
        }
        else
            return true;

    }
    public Boolean check1()
    {
        if(upFocus)
        {
            upFocus=false;
            return false;
        }
        else
            return true;

    }

    class RowContent
    {
        TextView ques;
        Button ans;
        ImageButton upImg;
        TextView upVote;
        ImageButton downImg;
        TextView downVote;
        Button flw;
    }

}



class MongoAsyncTaskHome extends AsyncTask<Void, Void, Void> {
    public HomeScreen homeScreen;



   @Override
    protected Void doInBackground(Void... args0) {
        try{
            try {
                Log.d("IN try block","ajsdfkjafhjkdahfjd");
                QueryBuilderHome qb1 = new QueryBuilderHome();
                URL url1 = new URL(qb1.buildContactsGetURL());
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
                while ((temp_output = br.readLine()) != null) {
                  server_output = temp_output;
                }
                String mongoarray = "{ question_list: "+server_output+"}";
                homeScreen.setAdapterData(mongoarray);
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

class QueryBuilderHome {
    public String getDatabaseName() {

        //return "user";
        return "askumkc";
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
        return "questionlist";
    }
    public String buildContactsSaveURL() {
        return getBaseUrl() + documentRequest() + docApiKeyUrl();
    }

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
                .format("{\"questionid\": \"%s\", "
                                + "\"academics\": \"%s\", \"question\": \"%s\"}",
                        j.get("questionid"),j.getString("academics"), j.getString("question"));

    }
}