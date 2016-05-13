package com.aseproject.askumkc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    ListView listview;
    ArrayList<String> Admissions = new ArrayList<String>();
    ArrayList<String> Non_Academics = new ArrayList<String>();
    ArrayList<String> Academics = new ArrayList<String>();
    ArrayList<String> Career = new ArrayList<String>();
    ArrayList<String> follwedQuestions= new ArrayList<String>();
    ArrayList<String> fullData=new ArrayList<>();
    basicAdapter basicAdapterObj;
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    NavigationView navigationView;
    String Userinfo;
    String authorName="";
    String authorID="";
    Toolbar toolbar;
    HomeScreen homeScreenObj;
    Boolean flwdClick=false;
    EditText serachText;
    Button clearText;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
         toolbar.setTitle("Academics");
         Intent intent=getIntent();
         Userinfo=intent.getStringExtra("userData");
         String imageData="";
         Bitmap bitmap;
         View view;
         LayoutInflater inflater = (LayoutInflater)   this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         view = inflater.inflate(R.layout.nav_header_home_screen, null);
         homeScreenObj=this;
         serachText=(EditText)findViewById(R.id.searchField);
        clearText =(Button)findViewById(R.id.clearable_button_clear);
         clearText.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(!toolbar.getTitle().toString().equals("FAQ")) {
                     Log.d("Value",toolbar.getTitle().toString());
                     serachText.setText("");
                     serachText.clearFocus();
                     InputMethodManager inputManager = (InputMethodManager)
                             getSystemService(Context.INPUT_METHOD_SERVICE);

                     inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                             InputMethodManager.HIDE_NOT_ALWAYS);
                     toolbar.setTitle("Questions");

                     Log.d("Cross", "working");
                 }
             }
         });
         try {
             JSONObject jsonObject = new JSONObject(Userinfo);
             authorName=jsonObject.getString("username");
             authorID=jsonObject.getString("user_id");
             imageData=jsonObject.getString("img");
             //Log.d("Image string",imageData);
             //Log.d("Author name: ",authorName);
             TextView authorTextView=(TextView)view.findViewById(R.id.authorNameField);
             //Log.d("TextView",authorTextView.toString());
             authorTextView.setText(authorName);
         }
         catch (JSONException e){}
         if(imageData.length()>0) {
             //Log.d("Working",imageData);
             imageData=imageData.replace("\\n","");
             byte[] encodeByte = Base64.decode(imageData, Base64.DEFAULT);
             bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
             //Author name and image
             ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
             imageView.setImageBitmap(bitmap);
         }

         setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
         navigationView = (NavigationView) findViewById(R.id.nav_view);
         navigationView.setNavigationItemSelectedListener(this);

         drawer.setDrawerTitle(Gravity.LEFT, "Working");
         Thread thread = new Thread() {
             @Override
             public void run() {
                 try {

                     MongoAsyncTaskHome mongoAsyncTaskHome=new MongoAsyncTaskHome();
                     mongoAsyncTaskHome.homeScreen=homeScreenObj;
                     mongoAsyncTaskHome.execute();
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             }
         };
         thread.start();
        //loadFunction();
         ListView  listview1 = (ListView) findViewById(R.id.listview);
         listview1.setAdapter(new Freqquest(this, new String[]{"All graduate students interested in admission into any of our numerous graduate programs will need to meet with an academic advisor in their area of graduate studies interest. If you are interested in graduate studies and would still like to take a campus tour only, please contact the Welcome Center at 816-235-8652.",
                 "Classes are not scheduled during weekday visits. For first time college students entering for the fall, you are required to attend orientation prior to setting up your classes. Transfer students can attend orientation or contact their advisor directly.",
                 "During your visit, financial aid, scholarships, as well as housing information are all covered in the UMKC Admissions overview.\n" +
                         "Transfer students, this information is not covered if you meet directly with your academic area. Please attend a weekday visit or UMKC Preview Day",
                 "The Hospital Hill campus and the Volker campus are approximately 5 miles apart. For more information about the campus, please visit our campus maps page.",
                 "The campus tour is a 60 minute long walking tour of the Volker campus."}));
         toolbar.setTitle("FAQ");
         serachText.setText("Swipe left for more questions");
         serachText.setEnabled(false);
         serachText.setInputType(InputType.TYPE_NULL);
         clearText.setVisibility(View.INVISIBLE);
     }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      //  serachText.setText("");
       // Log.d("req code", String.valueOf(requestCode));
        //Log.d("res code", String.valueOf(resultCode));
        if (requestCode == 1) {
            if(resultCode == 1){
                String result=data.getStringExtra("Posted");
          //      Log.d("String value",result);
                toolbar.setTitle(result);
                switch (result)
                {
                    case "Career":doSomething(Career);
                                  setTitle("Career");
                                  break;
                    case "Admissions":doSomething(Admissions);
                        setTitle("Admissions");
                        break;
                    case "Non-Academics":doSomething(Non_Academics);
                        setTitle("Non-Academics");
                        break;
                    case "Academics":doSomething(Academics);
                        setTitle("Academics");
                        break;
                }
                //loadFunction();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

       @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.setDrawerTitle(Gravity.LEFT,"Working");
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent=getIntent();
            setResult(1, intent);
            finish();
        }
    }

    public void requestFocus(MenuItem menu)
    {
        int id = menu.getItemId();

        if (id == R.id.action_settings) {
            if(!toolbar.getTitle().toString().equals("FAQ")) {
                serachText.setEnabled(true);
                serachText.setText("");
                serachText.setInputType(InputType.TYPE_CLASS_TEXT);
                serachText.requestFocus();
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(serachText, InputMethodManager.SHOW_FORCED);
            }
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
        int id = item.getItemId();

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
        clearText.setEnabled(true);
        clearText.setVisibility(View.VISIBLE);
        serachText.setText("");
        homeScreenObj.flwdClick=false;
        if (id == R.id.profile) {
            Intent intent= new Intent(this,ProfileUpdate.class);
            intent.putExtra("userData",Userinfo);
            startActivityForResult(intent, 1);

            // Handle the camera action
        } else if (id == R.id.logout) {
            Intent intent=getIntent();
            setResult(1, intent);
            finish();

        } else if (id == R.id.nav_slideshow) {
            toolbar.setTitle("Admissions");
            item.setEnabled(true);
            doSomething(Admissions);

        } else if (id == R.id.nav_manage) {
            toolbar.setTitle("Career");
            item.setEnabled(true);
            doSomething(Career);
        }
        else if (id == R.id.nav_gallery) {
            toolbar.setTitle("Non-Academics");
            item.setEnabled(true);
            doSomething(Non_Academics);
        }
        else if (id == R.id.nav_camera) {
            toolbar.setTitle("Academics");
            item.setEnabled(true);
            doSomething(Academics);
        }
        else if (id == R.id.myQuestions) {
            item.setEnabled(true);
            toolbar.setTitle("Follwed Questions");
            homeScreenObj.flwdClick=true;
            //Log.d("Before calling:",String.valueOf(follwedQuestions.size()));
            doSomething(follwedQuestions);
        }
        else if(id==R.id.afaq)
        {
            item.setEnabled(true);
            serachText.setInputType(InputType.TYPE_NULL);
            serachText.setEnabled(false);
            clearText.setEnabled(false);
            clearText.setVisibility(View.INVISIBLE);
            serachText.setText("Swipe left for more questions");
            toolbar.setTitle("FAQ");
            ListView  listview1 = (ListView) findViewById(R.id.listview);
            listview1.setAdapter(new Freqquest(this, new String[]{"All graduate students interested in admission into any of our numerous graduate programs will need to meet with an academic advisor in their area of graduate studies interest. If you are interested in graduate studies and would still like to take a campus tour only, please contact the Welcome Center at 816-235-8652.",
                    "Classes are not scheduled during weekday visits. For first time college students entering for the fall, you are required to attend orientation prior to setting up your classes. Transfer students can attend orientation or contact their advisor directly.",
                    "During your visit, financial aid, scholarships, as well as housing information are all covered in the UMKC Admissions overview.\n" +
                            "Transfer students, this information is not covered if you meet directly with your academic area. Please attend a weekday visit or UMKC Preview Day",
                    "The Hospital Hill campus and the Volker campus are approximately 5 miles apart. For more information about the campus, please visit our campus maps page.",
                    "The campus tour is a 60 minute long walking tour of the Volker campus."}));

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void doSomething(ArrayList<String> s)
    {

        final ProgressDialog dialog = new ProgressDialog(HomeScreen.this);
        dialog.setTitle("Loading...");
        dialog.setMessage("Please wait.");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
        long delayInMillis = 3000;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, delayInMillis);
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {

                        MongoAsyncTaskHome mongoAsyncTaskHome=new MongoAsyncTaskHome();
                        mongoAsyncTaskHome.homeScreen=homeScreenObj;
                        mongoAsyncTaskHome.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        serachText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                //Log.d("Text did changed", s.toString());
                // Log.d("Array SIze",String.valueOf(fullData.size()));
                if (!s.equals("") && !s.equals("Swipe left for more questions")) {
                    ArrayList<String> listClone = new ArrayList<String>();
                    String questionText = "";
                    for (String string : fullData) {

                        try {

                            JSONObject jsonObject = new JSONObject(string);
                            questionText = "";
                            questionText = jsonObject.getString("question");
                            // Log.d("Check",questionText);
                            if (questionText.toLowerCase().contains(s.toString().toLowerCase())) {
                                //Log.d("Matched text",questionText);
                                listClone.add(string);
                            }
                        } catch (Exception e) {
                            InputMethodManager inputManager = (InputMethodManager)
                                    getSystemService(Context.INPUT_METHOD_SERVICE);

                            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                            Toast.makeText(homeScreenObj, "Invalid parameter to search",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                    // Log.d("Searched array", listClone.toString());
                    if (listClone.size() > 0) {
                        toolbar.setTitle("Search Results");
                        listview = (ListView) findViewById(R.id.listview);
                        basicAdapterObj = new basicAdapter(homeScreenObj, listClone, authorID, Userinfo);
                        listview.setAdapter(basicAdapterObj);
                        basicAdapterObj.homeScreen = homeScreenObj;
                        ((basicAdapter) listview.getAdapter()).notifyDataSetChanged();
                    } else {
                        InputMethodManager inputManager = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);

                        AlertDialog.Builder builder = new AlertDialog.Builder(homeScreenObj);
                        builder.setTitle("No Results found..!!");
                        //builder.setMessage("Post your answer");
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                }
            }
        });
        listview = (ListView) findViewById(R.id.listview);
        basicAdapterObj=new basicAdapter(this,s,authorID,Userinfo);
        listview.setAdapter(basicAdapterObj);
        basicAdapterObj.homeScreen=this;
        ((basicAdapter) listview.getAdapter()).notifyDataSetChanged();
    }
    public void onClickPostQuestion(View view)
    {

        Intent intent= new Intent(this,Questionary.class);
        intent.putExtra("userData",Userinfo);
        startActivityForResult(intent, 1);

    }
    public void setAdapterData(String dataArray)
    {

       // Log.d("In Home Screen",dataArray);
        final String temp=dataArray;
        try {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Career.clear();
                    Admissions.clear();
                    Non_Academics.clear();
                    Academics.clear();
                    follwedQuestions.clear();
                    fullData.clear();
                    JSONObject obj = new JSONObject(temp);
                    JSONArray jsonArray=obj.getJSONArray("question_list");
                    int len=jsonArray.length();
                    Log.d("Array length",String.valueOf(len));
                    ArrayList<String> tempF=new ArrayList<>();
                    try{
                        JSONObject jsonObject1 = new JSONObject(Userinfo);
                        JSONArray flwd=jsonObject1.getJSONArray("followed_questions");
                        for(int i=0;i<flwd.length();i++)
                        {
                            tempF.add(flwd.getString(i));
                        }
                        //Log.d("fwqs",tempF.toString());
                    }
                    catch (Exception e)
                    {}

                    for(int i=0;i<len;i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String cat = jsonObject.getString("category");
                        fullData.add(jsonObject.toString());
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
                        String questioID=jsonObject.getString("questionid");
                       int length=tempF.size();
                        for(int j=0;j<length;j++)
                        {
                            if(questioID.equals(tempF.get(j)))
                            {
                                follwedQuestions.add(jsonObject.toString());
                            }

                        }

                   }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + dataArray + "\"");
        }

    }
    public void AnswersListPush(String data)
    {
        serachText.setText("");
//        Log.d("In anser",data);
        Intent intent= new Intent(this,AnswerList.class);
        intent.putExtra("userData",Userinfo);
        intent.putExtra("Data",data);
        startActivityForResult(intent, 1);
    }




}

class Freqquest extends BaseAdapter {

    public Context context;
    public String[] data;
    public   String[] FAQuestions=new String[]{"I am a graduate student what are my visit options","I am an admitted student. Can I enroll in classes while at a weekday visit","How do I get information about financial aid or housing","How far is the Hospital Hill campus from the Volker Campus","How long is campus tour"};
    private static LayoutInflater inflater = null;

    public Freqquest(Context context, String[] data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data[position];
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
        if (vi == null)
            vi = inflater.inflate(R.layout.faqrow, null);
        TextView headText = (TextView) vi.findViewById(R.id.headText);
        headText.setText("Question: "+FAQuestions[position]);
        TextView text = (TextView)vi.findViewById(R.id.text);
        text.setText("Answer: "+data[position]);
        return vi;
    }
}




class basicAdapter extends BaseAdapter {
       public Context context;
     public HomeScreen homeScreen;
   public ArrayList<String> data;
    JSONArray jsonArray;
    JSONArray userArray;
   public String UserID;
    public String Userinfo;
    private static LayoutInflater inflater = null;

    public basicAdapter(Context context, ArrayList<String> data, String Id, String userinfo) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        UserID=Id;
        Userinfo=userinfo;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
                    final int Position=position;
      final  RowContent rowContent;
                    if(vi==null) {
                        vi = inflater.inflate(R.layout.row, null);
                        rowContent = new RowContent();
                        rowContent.ques = (Button) vi.findViewById(R.id.headText);
                        rowContent.upImg = (ImageButton) vi.findViewById(R.id.correct);
                        rowContent.upVote = (TextView) vi.findViewById(R.id.upnum);
                        rowContent.downImg = (ImageButton) vi.findViewById(R.id.wrong);
                        rowContent.downVote = (TextView) vi.findViewById(R.id.dwnnum);
                        rowContent.ans = (Button) vi.findViewById(R.id.text);
                        rowContent.flw = (Button) vi.findViewById(R.id.follow);
                        rowContent.flwText = (Button) vi.findViewById(R.id.flwText);
                        rowContent.upImg.setImageResource(R.drawable.social);
                        rowContent.downImg.setImageResource(R.drawable.unlike);
                        vi.setTag(rowContent);
                    }
                    else
                    {
                        rowContent=(RowContent)vi.getTag();
                        rowContent.upImg.setImageResource(R.drawable.social);
                        rowContent.downImg.setImageResource(R.drawable.unlike);
                    }
                    //Set questions and anserws and other
                    try {
                        JSONObject obj = new JSONObject(data.get(position));
                        rowContent.ques.setText("Question: " + obj.getString("question"));
                        try {
                            userArray = obj.getJSONArray("users");
                            if (userArray.length() > 0) {
                                String s=String.valueOf(userArray.length()-1);
                                rowContent.flwText.setText("Followed by "+userArray.getString(0)+" and "+s +" others..!!");
                            } else {
                                rowContent.flwText.setText("No followers yet..!!");
                            }
                        }
                        catch (JSONException e)
                        {
                            rowContent.flwText.setText("No followers yet..!!");
                        }
                        jsonArray=obj.getJSONArray("answers");
                        if(jsonArray.length()>0) {
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            rowContent.ans.setText("Answer: " + jsonObject.getString("answer"));
                            String val = String.valueOf(jsonObject.getInt("downvote"));
                            rowContent.downVote.setText(val);
                            val = String.valueOf(jsonObject.getInt("upvote"));
                            rowContent.upVote.setText(val);
                            //Upvote and downvote loss
                            JSONArray upVoted=jsonObject.getJSONArray("upvoted_by");
                            ArrayList<String> listdata = new ArrayList<String>();
                            if (upVoted.length()>0) {
                                for (int i=0;i<upVoted.length();i++){
                                   //
                                   // Log.d("Values",upVoted.get(i).toString());
                                    listdata.add(upVoted.get(i).toString());
                                }
                                if (listdata.contains(UserID)) {
                                    //System.out.println("Up found");
                                    rowContent.upFocus=true;
                                    rowContent.upImg.setImageResource(R.drawable.uptouch1);
                                } else {
                                    //not getting called
                                    rowContent.upFocus=false;
                                    rowContent.upImg.setImageResource(R.drawable.social);
                                    System.out.println("Up not found");
                                }
                            }
                            upVoted=jsonObject.getJSONArray("downvoted_by");
                            listdata.clear();
                            if (upVoted.length()>0) {
                                for (int i=0;i<upVoted.length();i++){
                                    listdata.add(upVoted.get(i).toString());
                                }
                                if (listdata.contains(UserID)) {
                                    System.out.println("down found");
                                    rowContent.downImg.setImageResource(R.drawable.unliketouch1);
                                    rowContent.downFocus=true;
                                } else {
                                    //not getting called
                                    rowContent.downImg.setImageResource(R.drawable.unlike);
                                    rowContent.downFocus=false;
                                    System.out.println("down not found");
                                }
                            }
                        }
                        else
                        {
                            rowContent.upImg.setVisibility(View.GONE);
                            rowContent.ans.setVisibility(View.GONE);
                            rowContent.upVote.setVisibility(View.GONE);
                            rowContent.downVote.setVisibility(View.GONE);
                            rowContent.downImg.setVisibility(View.GONE);
                        }
                    }
                    catch (JSONException e)
                    {
                            Log.d("ERROR..!!","You are screwed..!!!!");
                    }

       rowContent.upImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                rowContent.upFocus=true;
               Boolean val= check(rowContent);
                String s1=rowContent.downImg.getTag().toString();
                ////Log.d("Value of the button",rowContent.toString());
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
                try {
                    JSONObject obj = new JSONObject(data.get(position));
                    //Log.d("Json val",obj.toString());
                    jsonArray=obj.getJSONArray("answers");
                    if(jsonArray.length()>0) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        JSONArray upVoted=jsonObject.getJSONArray("upvoted_by");
                        ArrayList<String> listdata = new ArrayList<String>();
                        if (upVoted.length()>0) {
                            for (int i=0;i<upVoted.length();i++){
                               // Log.d("Values", upVoted.get(i).toString());
                                listdata.add(upVoted.get(i).toString());
                            }
                            Log.d("List values",listdata.toString());
                            if (listdata.contains(UserID)) {
                                System.out.println("Up found");
                                Log.d("ALready exists","No increasing");
                                rowContent.upImg.setImageResource(R.drawable.uptouch1);

                            } else {
                                String s = rowContent.upVote.getText().toString();
                                int i = Integer.parseInt(s);
                                i++;
                                s = String.valueOf(i);
                                rowContent.upImg.setImageResource(R.drawable.uptouch1);
                                rowContent.upVote.setText(s);
                                listdata.add(UserID);
                                jsonObject.put("upvote",i);
                                jsonObject.put("upvoted_by", listdata);
                                String ds=rowContent.downVote.getText().toString();
                                i=Integer.parseInt(ds);
                                jsonObject.put("downvote", i);
                                jsonArray.put(0, jsonObject);
                                sample temp=new sample();
                                temp.fun1(obj.toString(), "https://api.mongolab.com/api/1/databases/dbsample/collections/records?apiKey=vAC5pceO7ioI5tQoaSPom8kjbrYVFCQl");
                                Log.d("List values inner else", jsonArray.toString());
                                //Set this list to json object
                                Log.d("Clickng for first time", " ALready ");

                            }
                        }
                        else
                        {
                            String s = rowContent.upVote.getText().toString();
                            int i = Integer.parseInt(s);
                            i++;
                            s = String.valueOf(i);
                            rowContent.upVote.setText(s);
                            listdata.clear();
                            listdata.add(UserID);
                            String ds=rowContent.downVote.getText().toString();
                            i=Integer.parseInt(ds);
                            jsonObject.put("upvoted_by", listdata);
                            jsonObject.put("upvote",i);
                            jsonObject.put("downvote",i);
                            jsonArray.put(0,jsonObject);
                            Log.d("List values outer else", obj.toString());
                            rowContent.upImg.setImageResource(R.drawable.uptouch1);
                            //Set this list to json object
                            sample temp=new sample();
                            temp.fun1(obj.toString(), "https://api.mongolab.com/api/1/databases/dbsample/collections/records?apiKey=vAC5pceO7ioI5tQoaSPom8kjbrYVFCQl");
                            Log.d("up Button Clicked", "**********");
                        }
                        upVoted=jsonObject.getJSONArray("downvoted_by");
                        listdata.clear();
                        if (upVoted.length()>0) {
                            for (int i=0;i<upVoted.length();i++){
                                listdata.add(upVoted.get(i).toString());
                            }
                            if (listdata.contains(UserID)) {
                                listdata.remove(UserID);
                                jsonObject.put("downvoted_by", listdata);
                                jsonArray.put(0,jsonObject);
                                Log.d("List values rem", jsonArray.toString());
                                System.out.println("down found");

                            } else {
                                System.out.println("down not found");
                            }
                        }

                    }
                    sample s=new sample();
                    s.fun1(obj.toString(),"https://api.mongolab.com/api/1/databases/dbsample/collections/records?apiKey=vAC5pceO7ioI5tQoaSPom8kjbrYVFCQl");
                }
                catch (Exception e)
                {}



            }
        });


        rowContent.downImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                rowContent.downFocus=true;
               Boolean val= check1(rowContent);
                if(!val)
                {
                    String s=rowContent.upVote.getText().toString();
                    int i=Integer.parseInt(s);
                    i--;
                    s=String.valueOf(i);
                    rowContent.upVote.setText(s);
                    rowContent.upImg.setImageResource(R.drawable.social);
                }
                try {
                    JSONObject obj = new JSONObject(data.get(position));
                    jsonArray=obj.getJSONArray("answers");
                    if(jsonArray.length()>0) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        JSONArray upVoted=jsonObject.getJSONArray("upvoted_by");
                        ArrayList<String> listdata = new ArrayList<String>();
                        if (upVoted.length()>0) {
                            for (int i=0;i<upVoted.length();i++){
                                Log.d("Values",upVoted.get(i).toString());
                                listdata.add(upVoted.get(i).toString());
                                //jsonObject.put("upvoted_by", listdata);
                            }
                            if (listdata.contains(UserID)) {
                                System.out.println("Up found");
                                listdata.remove(UserID);
                                jsonObject.put("upvoted_by", listdata);
                                Log.d("List values rem", listdata.toString());
                            }
                            else
                            {System.out.println("Down not found");
                            }
                        }
                        upVoted=jsonObject.getJSONArray("downvoted_by");
                        listdata.clear();
                        if (upVoted.length()>0) {
                            for (int i=0;i<upVoted.length();i++){
                                listdata.add(upVoted.get(i).toString());
                                //jsonObject.put("upvoted_by", listdata);
                            }
                            if (listdata.contains(UserID)) {
                                System.out.println("down found");
                                rowContent.downImg.setImageResource(R.drawable.unliketouch1);

                            } else {
                                rowContent.downImg.setImageResource(R.drawable.unliketouch1);
                                String s=rowContent.downVote.getText().toString();
                                int i=Integer.parseInt(s);
                                i++;
                                jsonObject.put("downvote",i);
                                s=String.valueOf(i);
                                rowContent.downVote.setText(s);
                                s=rowContent.upVote.getText().toString();
                                i=Integer.parseInt(s);
                                jsonObject.put("upvote",i);
                                listdata.add(UserID);
                                jsonObject.put("downvoted_by", listdata);
                                Log.d("List values", listdata.toString());
                                System.out.println("down not found");
                            }
                        }
                        else
                        {
                            rowContent.downImg.setImageResource(R.drawable.unliketouch1);
                            String s=rowContent.downVote.getText().toString();
                            int i=Integer.parseInt(s);
                            i++;
                            s=String.valueOf(i);
                            jsonObject.put("downvote",i);
                            rowContent.downVote.setText(s);
                            listdata.add(UserID);
                             s=rowContent.upVote.getText().toString();
                            i=Integer.parseInt(s);
                            jsonObject.put("upvote",i);
                            jsonObject.put("downvoted_by", listdata);
                        }

                    }
                }
                catch (Exception e)
                {}
                sample s=new sample();
                s.fun1(data.get(position).toString(),"https://api.mongolab.com/api/1/databases/dbsample/collections/records?apiKey=vAC5pceO7ioI5tQoaSPom8kjbrYVFCQl");

            }
        });

        rowContent.flw.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Button b=(Button)v;
                ArrayList<String> tempF=new ArrayList<>();
                JSONArray flwd=new JSONArray();
                JSONObject jsonObject1=new JSONObject();
                String Questionid="";
                try{
                    jsonObject1 = new JSONObject(Userinfo);
                   flwd =jsonObject1.getJSONArray("followed_questions");
                    JSONObject obj = new JSONObject(data.get(position));
                    for(int i=0;i<flwd.length();i++)
                    {
                        tempF.add(String.valueOf(flwd.getInt(i)));
                    }
                    Questionid=obj.getString("questionid");
                }
                catch (Exception e)
                {}
                if(b.getText().equals("Unfollow"))
                {
                    b.setText("Follow");
                    tempF.remove(Questionid);
                    JSONArray jsonArray=new JSONArray(tempF);
                    try{
                    jsonObject1.put("followed_questions",tempF);}
                    catch (Exception e){}
                    Userinfo=jsonObject1.toString();
                }
                else {
                    tempF.add(Questionid);
                    b.setText("Unfollow");
                    flwd.put(Questionid);
                    Userinfo=jsonObject1.toString();
                }
                homeScreen.Userinfo=Userinfo;
                Log.d("User data aftr clicking", Userinfo);
                try {
                    JSONObject pushJsonData = new JSONObject(Userinfo);


                    //Final commit uncomment the below lines
                    //sample s=new sample();
                    //s.fun1(pushJsonData.toString(),"https://api.mongolab.com/api/1/databases/askumkc/collections/users?apiKey=7rL6L7EoWpZnPgyjtHDS8Rwh6VDer-ic");
                }
                catch (Exception e){}
                Toast.makeText(context, "Question followed",
                        Toast.LENGTH_LONG).show();



            }
        });
                rowContent.ans.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Log.d("Values are:", data.get(position));
                        String clickedData=data.get(position);
                        homeScreen.AnswersListPush(clickedData);

                    }
                });
                    rowContent.ques.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Log.d("Values are:", data.get(position));
                            String clickedData = data.get(position);
                            homeScreen.AnswersListPush(clickedData);
                        }
                    });
                    rowContent.flwText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Followers..!!");
                            String s="";
                            try {
                                JSONObject obj = new JSONObject(data.get(position));
                                JSONArray jsonArray = obj.getJSONArray("users");
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        try {
                                            s = s + jsonArray.getString(i) + " , ";
                                        } catch (JSONException e) {
                                        }
                                    }
                                    int x = s.length();
                                    s = s.substring(0, x - 2);
                                }
                            }
                            catch (Exception e)
                            {
                                s = "No Follwers..!! :'( ";
                            }
                            builder.setMessage(s);
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        }
                    });
                    if(homeScreen.flwdClick)
                    {
                        rowContent.flw.setText("Unfollow");
                    }
         return vi;
    }
    public Boolean check(RowContent rowContent)
    {
        if(rowContent.downFocus)
        {
            rowContent.downFocus=false;
            return false;
        }
        else
            return true;
    }
    public Boolean check1(RowContent rowContent)
    {
        if(rowContent.upFocus)
        {
            rowContent.upFocus=false;
            return false;
        }
        else
            return true;
    }

    class RowContent
    {
        Button ques;
        Button ans;
        ImageButton upImg;
        TextView upVote;
        ImageButton downImg;
        TextView downVote;
        Button flw;
        Button flwText;
        Boolean upFocus=false;
        Boolean downFocus=false;
    }

}



class MongoAsyncTaskHome extends AsyncTask<Void, Void, Void> {
    public HomeScreen homeScreen;
   @Override
    protected Void doInBackground(Void... args0) {
        try{
            try {
                URL url1 = new URL("https://api.mongolab.com/api/1/databases/askumkc/collections/questionlist?apiKey=7rL6L7EoWpZnPgyjtHDS8Rwh6VDer-ic");
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
                String OriginalObject = "";
                // BasicDBObject user = null;
                while ((temp_output = br.readLine()) != null) {
                  server_output = temp_output;
                }
                String mongoarray = "{ question_list: "+server_output+"}";
                homeScreen.setAdapterData(mongoarray);
            } catch (Exception ex) {
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
