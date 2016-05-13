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
    @Override
      protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
      final String data=intent.getStringExtra("Data");
        Userinfo=intent.getStringExtra("userData");
        Log.d("Passed userdata",Userinfo);
        try {
            JSONObject jsonObject = new JSONObject(Userinfo);
            authorName=jsonObject.getString("username");
            authorID=jsonObject.getString("user_id");
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
        listview.setAdapter(new basicAdapterans(this,list,authorID));
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
        setResult(1, intent);
        finish();
    }
    public void postData(JSONObject data)
    {
        //pushing data to server
        sample userMod=new sample();
        userMod.fun1(data.toString(),"https://api.mongolab.com/api/1/databases/askumkc/collections/questionlist?apiKey=7rL6L7EoWpZnPgyjtHDS8Rwh6VDer-ic");
    }

}

class basicAdapterans extends BaseAdapter {

    public Context context;
    public ArrayList<String> data;
    String UserID;
    public   String[] FAQuestions=new String[]{"I am a graduate student what are my visit options","I am an admitted student. Can I enroll in classes while at a weekday visit","How do I get information about financial aid or housing","How far is the Hospital Hill campus from the Volker Campus","How long is campus tour"};
    private static LayoutInflater inflater = null;

    public basicAdapterans(Context context, ArrayList<String> data,String ID) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        UserID=ID;
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
                } else {
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
                    rowContent.downImg.setImageResource(R.drawable.unlike);
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
                }
                rowContent.downImg.setImageResource(R.drawable.unlike);
                String s=rowContent.upVote.getText().toString();
                int i=Integer.parseInt(s);
                i++;
                s=String.valueOf(i);
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
                rowContent.downFocus=true;
                Boolean val= check1(rowContent);
                if(!val)
                {
                    String s=rowContent.upVote.getText().toString();
                    int i=Integer.parseInt(s);
                    i--;
                    s=String.valueOf(i);
                    rowContent.upVote.setText(s);

                }
                rowContent.upImg.setImageResource(R.drawable.social);
                rowContent.downImg.setImageResource(R.drawable.unliketouch1);
                String s=rowContent.downVote.getText().toString();
                int i=Integer.parseInt(s);
                i++;
                //                if(i<=0)
//                    i=0;
                s=String.valueOf(i);
                rowContent.downVote.setText(s);
                Log.d("down Button Clicked", "**********");
//                Toast.makeText(context, "DownVote button Clicked",
//                        Toast.LENGTH_LONG).show();
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
        }

}