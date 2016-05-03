package com.aseproject.askumkc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AnswerList extends AppCompatActivity {
    ListView listview;
    Button questionText;
    ArrayList<String> list = new ArrayList<String>();
    JSONArray jsonArray;
    JSONObject jsonObject;
   String s;
    String Userinfo;
    String authorName;
    String categorySelected;
    String authorID;
    Boolean postedAnything=false;
    String questionID;
    @Override
      protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
      final String data=intent.getStringExtra("Data");
        Userinfo=intent.getStringExtra("userData");
        //Log.d("Passed userdata",Userinfo);
        try {
            JSONObject jsonObject = new JSONObject(Userinfo);
            authorName=jsonObject.getString("username");
            authorID=jsonObject.getString("user_id");
            JSONObject quesJson=new JSONObject(data);
           questionID =quesJson.getString("questionid");
            //Log.d("QuestionID",questionID);
        }
        catch (JSONException e) {
        }
        setContentView(R.layout.activity_answer_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      final AnswerList answerListObj=this;


        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    jsonObject  = new JSONObject(data);
                    categorySelected=jsonObject.getString("category");
                    jsonArray=jsonObject.getJSONArray("answers");
                    s = jsonObject.getString("question");
                    answerListObj.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            questionText = (Button) findViewById(R.id.questionText);
                            CharSequence st = s;
                            questionText.setText(st);
                        }
                    });

                    if (jsonArray != null) {
                        int len = jsonArray.length();
                        for (int i=0;i<len;i++){
                            list.add(jsonArray.get(i).toString());
                        }
                        if(len==0)
                        {
                           // Toast.makeText(this, "No answers yet..!!",Toast.LENGTH_LONG).show();
                        }
                    }
                }
                catch (JSONException e)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(answerListObj);
                    builder.setTitle("No answers yet..!!");
                    builder.setMessage("Post your answer");
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            }
        };
        thread.start();
        listview = (ListView) findViewById(R.id.listviewans);
//        listview.setAdapter(new basicAdapterans());
        basicAdapterans ansAdapter =new basicAdapterans(this,list,authorID);
        ansAdapter.quesObj=data;
        listview.setAdapter(ansAdapter);

        String dq="\"";
        String c=",";

        ArrayList<String> flwUsers=new ArrayList<>();
        try {
            JSONObject x=new JSONObject(data);
            JSONArray jsonArray = x.getJSONArray("users");

            for(int i=0; i<jsonArray.length();i++)
            {
                flwUsers.add(jsonArray.getString(i));
            }
        }catch (Exception e){}

    }

    public void onPost(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your answer..!!");

// Set up the input
        Context context = view.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
       input.setHeight(150);
       input.setMaxLines(10);
       input.setSingleLine(false);
        input.setHint("Your answer goes here");
        input.setBackgroundColor(0);
        input.getBackground().clearColorFilter();
        input.setHorizontalScrollBarEnabled(false);
//        input.setEnabled(true);
//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInputFromWindow(input.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
       input.canScrollHorizontally(1);
       // input.setText("sfkjbkjsbf");
        layout.addView(input);
        builder.setView(layout);


// Set up the buttons
        builder.setPositiveButton("POST", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("Value entered", input.getText().toString());
                JSONObject ans=new JSONObject();
                try {
                    //Json formation
                    ans.put("answer", input.getText());
                    ans.put("upvote",0);
                    ans.put("downvote",0);
                    ArrayList sam=new ArrayList();
                    ans.put("upvoted_by",new JSONArray(sam));
                    ans.put("downvoted_by",new JSONArray(sam));
                    jsonArray.put(ans);
                    int len = jsonArray.length();
                    list.clear();
                    for (int i=0;i<len;i++){
                        list.add(jsonArray.get(i).toString());
                    }
                    listview = (ListView) findViewById(R.id.listviewans);
                    ((basicAdapterans) listview.getAdapter()).notifyDataSetChanged();
                    jsonObject.put("answers", jsonArray);
                    postData(jsonObject);
                }
                catch (JSONException e)
                {

                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNeutralButton("Mike", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("Mike","Working..!!");
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed()
    {
       // super.onBackPressed();
        Intent intent=new Intent();
        intent.putExtra("Posted",categorySelected);
       // intent.putExtra("Refresh",postedAnything);
       // Log.d("Values",String.valueOf(postedAnything));
        setResult(1, intent);
        finish();
    }
    public void postData(JSONObject data)
    {
        //pushing data to server
        postedAnything=true;
        sample userMod=new sample();
        userMod.fun1(data.toString(),"https://api.mongolab.com/api/1/databases/askumkc/collections/questionlist?apiKey=7rL6L7EoWpZnPgyjtHDS8Rwh6VDer-ic");

        String dq="\"";
        String c=",";


        ArrayList<String> flwUsers=new ArrayList<>();
        try {
            JSONArray jsonArray = data.getJSONArray("users");

            for(int i=0; i<jsonArray.length();i++)
            {
                flwUsers.add(jsonArray.getString(i));
            }
        }catch (Exception e){}
        String output="";
        Log.d("Arraylist values",flwUsers.toString());
        for(int i=0;i<flwUsers.size();i++)
        {
            output+=flwUsers.get(i)+c;
        }
        output=output.substring(0,output.length()-1);
        Log.d("Values of the out", output);
        Log.d("Question",questionID);
        sample s=new sample();
        String url="http://8d8aba3e.ngrok.io/Mail/tosend.jsp?firstname="+questionID+"&lastname="+output;
        s.fun1("", url);

    }

}

class basicAdapterans extends BaseAdapter {

    public Context context;
    public ArrayList<String> data;
    String UserID;
    private static LayoutInflater inflater = null;
    public String quesObj;

    public basicAdapterans(Context context, ArrayList<String> data,String ID) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        UserID=ID;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d("ID",UserID);
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
        if (vi == null) {
            vi = inflater.inflate(R.layout.ansrow, null);
            rowContent = new RowContent();
            rowContent.upImg = (ImageButton) vi.findViewById(R.id.correct);
            rowContent.upVote = (TextView) vi.findViewById(R.id.upnum);
            rowContent.downImg = (ImageButton) vi.findViewById(R.id.wrong);
            rowContent.downVote = (TextView) vi.findViewById(R.id.dwnnum);
            rowContent.ans = (Button) vi.findViewById(R.id.text);
            rowContent.upImg.setImageResource(R.drawable.social);
            rowContent.downImg.setImageResource(R.drawable.unlike);
            rowContent.upFocus=false;
            rowContent.downFocus=false;
            vi.setTag(rowContent);
        }
        else
        {
            rowContent=(RowContent)vi.getTag();
            rowContent.upImg.setImageResource(R.drawable.social);
            rowContent.downImg.setImageResource(R.drawable.unlike);
        }
        try{
            JSONObject jsonObject=new JSONObject(data.get(position));
            Log.d("Ans",jsonObject.toString());
            rowContent.ans.setText("Answer: " +jsonObject.getString("answer"));
            String val=String.valueOf(jsonObject.getInt("downvote"));
            rowContent.downVote.setText(val);
            val=String.valueOf(jsonObject.getInt("upvote"));
            rowContent.upVote.setText(val);
            JSONArray upVoted=jsonObject.getJSONArray("upvoted_by");
            ArrayList<String> listdata = new ArrayList<String>();
            if (upVoted.length()>0) {
                for (int i=0;i<upVoted.length();i++){
                    Log.d("Values",upVoted.get(i).toString());
                    listdata.add(upVoted.get(i).toString());
                }
                if (listdata.contains(UserID)) {
                    System.out.println("Up found");
                    rowContent.upFocus=true;
                    rowContent.upImg.setImageResource(R.drawable.uptouch1);
                    rowContent.upBtnAlreadyClicked=true;
                } else {
                    rowContent.upFocus=false;
                    rowContent.upBtnAlreadyClicked=false;
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
                    rowContent.dwnBtnAlreadyClicked=true;
                    rowContent.downFocus=true;
                } else {
                    rowContent.downImg.setImageResource(R.drawable.unlike);
                    rowContent.dwnBtnAlreadyClicked=false;
                    rowContent.downFocus=false;
                    System.out.println("down not found");
                }
            }


        }
        catch (JSONException e)
        {

        }

        rowContent.upImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                rowContent.upFocus=true;
                rowContent.upImg.setImageResource(R.drawable.uptouch1);
                Boolean val= check(rowContent);
                if(!val)
                {
                    String s=rowContent.downVote.getText().toString();
                    int i=Integer.parseInt(s);
                    i--;
                    s=String.valueOf(i);
                    rowContent.downVote.setText(s);
                    rowContent.downImg.setImageResource(R.drawable.unlike);
                    rowContent.dwnBtnAlreadyClicked=false;
                }
                if(!rowContent.upBtnAlreadyClicked) {
                    rowContent.upBtnAlreadyClicked=true;
                    String s = rowContent.upVote.getText().toString();
                    int i = Integer.parseInt(s);
                    i++;
                    s = String.valueOf(i);
                    rowContent.upVote.setText(s);
                    Log.d("up Button Clicked", "**********");
//                Toast.makeText(context, "UpVote button Clicked",
//                        Toast.LENGTH_LONG).show();


                    try {
                        JSONObject answerObj=new JSONObject(data.get(position));
                        //Log.d("answer Obj",answerObj.toString());
                        JSONArray upVoted=answerObj.getJSONArray("upvoted_by");
                        Log.d("upvoted Obj",upVoted.toString());
                        ArrayList<String> listdata = new ArrayList<String>();
                        if (upVoted.length()>0) {
                            for (int k=0;k<upVoted.length();k++){
                                listdata.add(upVoted.get(k).toString());
                            }if(listdata.contains(UserID))
                            {}
                            else
                                listdata.add(UserID);
                        }
                        else
                        {
                            listdata.add(UserID);
                        }

                        JSONArray dwnVoted=answerObj.getJSONArray("downvoted_by");
                        Log.d("upvoted Obj",dwnVoted.toString());
                        ArrayList<String> listdata1 = new ArrayList<String>();
                        if (dwnVoted.length()>0) {
                            for (int k=0;k<dwnVoted.length();k++){
                                listdata1.add(dwnVoted.get(k).toString());
                            }
                            if(listdata1.contains(UserID))
                            {
                                listdata1.remove(UserID);
                            }
                        }
                        JSONArray x=new JSONArray();
                        for (int g=0;g<listdata.size();g++)
                        {
                            x.put(g,Integer.parseInt(listdata.get(g)));
                        }
                        Log.d("Value from x", x.toString());
                        answerObj.put("upvoted_by", x);
                        JSONArray y=new JSONArray();
                        //Log.d("up values upvoted", upVoted.toString());
                        for (int g=0;g<listdata1.size();g++)
                        {
                            y.put(g, Integer.parseInt(listdata1.get(g)));
                        }
                        Log.d("Value from x", x.toString());
                        answerObj.put("downvoted_by", y);
                        // Log.d("dwn values dwnvoted", answerObj.toString());
                        answerObj.put("upvote", i);
                        String dwnval=rowContent.downVote.getText().toString();
                        answerObj.put("downvote", Integer.parseInt(dwnval));
                        JSONObject pushObj=new JSONObject(quesObj);
                        JSONArray answerJsonArray=pushObj.getJSONArray("answers");

                        //Log.d("answer",answerJsonArray.toString());
                        answerJsonArray.put(position, answerObj);
                       // Log.d("Answer", answerObj.toString());
                      //  Log.d("Total data",pushObj.toString());
                        sample push=new sample();
                        push.fun1(pushObj.toString(), "https://api.mongolab.com/api/1/databases/askumkc/collections/questionlist?apiKey=7rL6L7EoWpZnPgyjtHDS8Rwh6VDer-ic");
                    }
                    catch (Exception e)
                    {

                    }

                }
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
                    rowContent.upBtnAlreadyClicked=false;
                }
                if(!rowContent.dwnBtnAlreadyClicked) {
                    rowContent.dwnBtnAlreadyClicked=true;
                    rowContent.downImg.setImageResource(R.drawable.unliketouch1);
                    String s = rowContent.downVote.getText().toString();
                    int i = Integer.parseInt(s);
                    i++;
                    s = String.valueOf(i);
                    rowContent.downVote.setText(s);
                    Log.d("down Button Clicked", "**********");
//                Toast.makeText(context, "DownVote button Clicked",
//                        Toast.LENGTH_LONG).show();

                    try {
                        JSONObject answerObj=new JSONObject(data.get(position));
                        //Log.d("answer Obj",answerObj.toString());
                        JSONArray upVoted=answerObj.getJSONArray("downvoted_by");
                        Log.d("upvoted Obj",upVoted.toString());
                        ArrayList<String> listdata = new ArrayList<String>();
                        if (upVoted.length()>0) {
                            for (int k=0;k<upVoted.length();k++){
                                listdata.add(upVoted.get(k).toString());
                            }if(listdata.contains(UserID))
                            {}
                            else
                                listdata.add(UserID);
                        }
                        else
                        {
                            listdata.add(UserID);
                        }

                        JSONArray dwnVoted=answerObj.getJSONArray("upvoted_by");
                        Log.d("upvoted Obj",dwnVoted.toString());
                        ArrayList<String> listdata1 = new ArrayList<String>();
                        if (dwnVoted.length()>0) {
                            for (int k=0;k<dwnVoted.length();k++){
                                listdata1.add(dwnVoted.get(k).toString());
                            }
                            if(listdata1.contains(UserID))
                            {
                                listdata1.remove(UserID);
                            }
                        }
                        JSONArray x=new JSONArray();
                        for (int g=0;g<listdata.size();g++)
                        {
                            x.put(g,Integer.parseInt(listdata.get(g)));
                        }
                        Log.d("Value from x", x.toString());
                        answerObj.put("downvoted_by", x);
                        JSONArray y=new JSONArray();
                        //Log.d("up values upvoted", upVoted.toString());
                        for (int g=0;g<listdata1.size();g++)
                        {
                            y.put(g, Integer.parseInt(listdata1.get(g)));
                        }
                        Log.d("Value from x", x.toString());
                        answerObj.put("upvoted_by", y);
                        // Log.d("dwn values dwnvoted", answerObj.toString());
                        String upVotedS=rowContent.upVote.getText().toString();
                        answerObj.put("upvote", Integer.parseInt(upVotedS));
                        String dwnval=rowContent.downVote.getText().toString();
                        answerObj.put("downvote", Integer.parseInt(dwnval));
                       // Log.d("Answer", answerObj.toString());

                        JSONObject pushObj=new JSONObject(quesObj);
                        JSONArray answerJsonArray=pushObj.getJSONArray("answers");

                        //Log.d("answer",answerJsonArray.toString());
                        answerJsonArray.put(position, answerObj);
                        // Log.d("Answer", answerObj.toString());
                        sample push=new sample();
                        push.fun1(pushObj.toString(),"https://api.mongolab.com/api/1/databases/askumkc/collections/questionlist?apiKey=7rL6L7EoWpZnPgyjtHDS8Rwh6VDer-ic");
                        Log.d("Total data", pushObj.toString());


                    }
                    catch (Exception e)
                    {

                    }


                }
            }
        });

              rowContent.ans.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.d("Move to next", "**********");

                Toast.makeText(context, "Next button",
                        Toast.LENGTH_LONG).show();
            }
        });
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
        Button ans;
        ImageButton upImg;
        TextView upVote;
        ImageButton downImg;
        TextView downVote;
        Boolean upFocus=false;
        Boolean downFocus=false;
        Boolean upBtnAlreadyClicked=false;
        Boolean dwnBtnAlreadyClicked=false;
        }

}