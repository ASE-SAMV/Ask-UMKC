package com.aseproject.askumkc;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpRequest;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

public class login extends AppCompatActivity {

   public EditText et1,et2;
    Context mContext = login .this;
    AccountManager mAccountManager;
    Boolean first=true;
    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
    JSONObject reqJSON;
    Boolean loginAction=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d("Login", "Created");
        et1=(EditText)findViewById(R.id.editText);
        et2=(EditText)findViewById(R.id.editText2);
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
    public void login(View view)
    {
        Boolean validUser=false;

        if(et1.getText().toString().length()>0 && et2.getText().toString().length()>0) {
                try {
                    reqJSON = new JSONObject();
                    reqJSON.put("username", "manimad");
                    reqJSON.put("password", "6353");
                }
                catch (JSONException e)
                {

                }
                MongoAsyncTaskLogin x=new MongoAsyncTaskLogin();
                x.loginObj=this;
                x.execute();

        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_LONG).show();
        }
    }

    public void loginActionCheck(Boolean val, String sam)
    {
        if(val && sam.length()>0)
        {
            Intent intent= new Intent(this,HomeScreen.class);
            intent.putExtra("userData",sam);
            Log.d("User info ",sam);
            startActivityForResult(intent, 1);

        }
    }

    public void Register(View view)
    {
        Intent intent= new Intent(this,SignUp.class);
        startActivityForResult(intent,1);
        }
    private String[] getAccountNames() {
        mAccountManager = AccountManager.get(this);
        Account[] accounts = mAccountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        String[] names = new String[accounts.length];
        for(int i = 0; i < names.length; i++) {
            names[i] = accounts[i].name;
        }
        return names;
    }

    private AbstractGetNameTask getTask(login activity, String email, String scope) {


//        try {
//
//            String gmailname,gmailgender,gmailimgurl;
//
//
//            JSONObject profileData = new JSONObject(AbstractGetNameTask.GOOGLE_USER_DATA);
//
//            if(profileData.has("picture")) {
//                gmailimgurl = profileData.getString("picture");
//                Log.d("in the picture", "in the picture");
//
//            }
//            if(profileData.has("name")) {
//                gmailname = profileData.getString("name");
//                Log.d("IN the Name method", gmailname);
//
//            }
//            if(profileData.has("gender")) {
//                gmailgender = profileData.getString("gender");
//                Log.d("IN the gender method", gmailgender);
//
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
        return new GetNameInForeground(activity, email, scope);
    }

    public void loginWithGoogle(View v) {



        //String names="\"vpn7d@mail.umkc.edu\"";
//        String names="vpn7d@mail.umkc.edu"+"," +"vikeshpadarthi@gmail.com";
//        String s3="100001";
//        String s2="http://f1f4edfa.ngrok.io/Mail/tosend.jsp?firstname="+names+"&lastname="+s3;
//        Log.d("Url req",s2);
//sample s=new sample();
//        s.fun1("",s2);

        if(isNetworkAvailable() == true) {
            String[] accountarrs = getAccountNames();
            if(accountarrs.length > 0) {
                Log.d("Acc",accountarrs.toString());
                getTask(login.this, accountarrs[0], SCOPE).execute();
                               } else {
                Toast.makeText(login.this, "No Google Account Sync!",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(login.this, "No Network Service!",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public  void delay(long delay)
    {
        final ProgressDialog dialog = new ProgressDialog(login.this);
        dialog.setTitle("Authenticating...");
        dialog.setMessage("Please wait..");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();

        long delayInMillis = delay;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, delayInMillis);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            Log.e("Network Testing", "Available");
            return true;
        }
        Log.e("Network Testing", "Not Available");
        return false;
    }
    class MongoAsyncTaskLogin extends AsyncTask<Void, Void, Void> {

    public login loginObj;


        @Override
        protected Void doInBackground(Void... args0) {
            loginObj.runOnUiThread(new Runnable() {
                public void run() {
                    delay(2000);
                }
            });

            try {
                try {
                    URL url1 = new URL("https://api.mongolab.com/api/1/databases/askumkc/collections/users?q="+reqJSON.toString()+"&apiKey=7rL6L7EoWpZnPgyjtHDS8Rwh6VDer-ic");
                    HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
                    Log.d("URL",url1.toString());
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
                    String mongoarray = "{ Result: "+server_output+"}";
                    JSONObject jsonobj=new JSONObject(mongoarray);
                    JSONArray jsonArray=jsonobj.getJSONArray("Result");
                    if(jsonArray.length()>0)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        Log.d("Result Array",jsonObject.toString());
                                    loginAction=true;
                                    String x=jsonObject.toString();
                                    loginObj.loginActionCheck(loginAction,x);
                    }
                    else
                    {
                        loginObj.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(loginObj, "Wrong Credentials..!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception ex) {
                    Log.e("App", "yourDataTask", ex);
                    return null;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();

            }

            return null;
        }
    }

}
