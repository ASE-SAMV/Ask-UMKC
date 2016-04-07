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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    String s;
    @Override
      protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        String data=intent.getStringExtra("Data");

        Log.d("Passed Data",data);
        setContentView(R.layout.activity_answer_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray=jsonObject.getJSONArray("answers");
            s=jsonObject.getString("question");
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i=0;i<len;i++){
                    list.add(jsonArray.get(i).toString());
                }
            }
        }
        catch (JSONException e)
        {

        }
        questionText=(Button)findViewById(R.id.questionText);
        CharSequence st=s;
        questionText.setText(st);
        listview = (ListView) findViewById(R.id.listviewans);
        listview.setAdapter(new basicAdapterans(this,list));
    }

    public void onPost(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your answer..!!");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHeight(150);
        input.setMaxLines(1);
        input.canScrollHorizontally(1);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("POST", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("Value entered",input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent=getIntent();
        setResult(1, intent);
        finish();
    }

}

class basicAdapterans extends BaseAdapter {

    public Context context;
    public ArrayList<String> data;
    Boolean upFocus=false,downFocus=false;
    public   String[] FAQuestions=new String[]{"I am a graduate student what are my visit options","I am an admitted student. Can I enroll in classes while at a weekday visit","How do I get information about financial aid or housing","How far is the Hospital Hill campus from the Volker Campus","How long is campus tour"};
    private static LayoutInflater inflater = null;

    public basicAdapterans(Context context, ArrayList<String> data) {
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
        final int Position=position;
        final  RowContent rowContent;
        if (vi == null)
            vi = inflater.inflate(R.layout.ansrow, null);
        rowContent=new RowContent();
        rowContent.upImg=(ImageButton)vi.findViewById(R.id.correct);
        rowContent.upVote=(TextView)vi.findViewById(R.id.upnum);
        rowContent.downImg=(ImageButton)vi.findViewById(R.id.wrong);
        rowContent.downVote=(TextView)vi.findViewById(R.id.dwnnum);
        rowContent.ans = (Button) vi.findViewById(R.id.text);
        try{
            JSONObject jsonObject=new JSONObject(data.get(position));
            rowContent.ans.setText("Answer: " +jsonObject.getString("answer"));
            String val=String.valueOf(jsonObject.getInt("downvote"));
            rowContent.downVote.setText(val);
            val=String.valueOf(jsonObject.getInt("upvote"));
            rowContent.upVote.setText(val);

        }
        catch (JSONException e)
        {

        }

        rowContent.upImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                upFocus=true;
                rowContent.upImg.setImageResource(R.drawable.uptouch1);
                Boolean val= check();
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
                downFocus=true;
                Boolean val= check1();
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
        Button ans;
        ImageButton upImg;
        TextView upVote;
        ImageButton downImg;
        TextView downVote;
        }

}