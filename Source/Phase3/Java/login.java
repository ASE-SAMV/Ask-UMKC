package com.aseproject.askumkc;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class login extends AppCompatActivity {

   public EditText et1,et2;
    Context mContext = login .this;
    AccountManager mAccountManager;
    Boolean first=true;
    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";

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
            if(validUser) {
                Intent intent = new Intent(this, HomeScreen.class);
                startActivity(intent);
            }
        }
        else
        {
            Intent intent = new Intent(this, HomeScreen.class);
            startActivityForResult(intent,1);
            Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_LONG).show();
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
        return new GetNameInForeground(activity, email, scope);
    }

    public void loginWithGoogle(View v) {
       /* TextView tvPlzWait=(TextView)findViewById(R.id.tvPlzWait);
        ProgressBar pbWait=(ProgressBar)findViewById(R.id.progressBar1);
        tvPlzWait.setVisibility(View.VISIBLE);
        pbWait.setVisibility(View.VISIBLE);*/

        if(isNetworkAvailable() == true) {
            String[] accountarrs = getAccountNames();
            if(accountarrs.length > 0) {
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

}
