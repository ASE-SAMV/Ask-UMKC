package com.aseproject.askumkc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SignUp extends AppCompatActivity {

    public EditText firstname, lastname, phonenumber, emailid, username, password;
    Button bn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firstname = (EditText) findViewById(R.id.first_Name);
        lastname = (EditText) findViewById(R.id.last_Name);
        phonenumber = (EditText) findViewById(R.id.phone);
        emailid = (EditText) findViewById(R.id.email_id);
        username = (EditText) findViewById(R.id.user_Name);
        password = (EditText) findViewById(R.id.password);
    }

    public void onClickSignUp() {

        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }
}


