package com.aseproject.askumkc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void login(View view)
    {
        Intent intent= new Intent(this,HomeScreen.class);
        startActivity(intent);
    }

    public void Register(View view)
    {
        Intent intent= new Intent(this,SignUp.class);
        startActivity(intent);

    }
}
