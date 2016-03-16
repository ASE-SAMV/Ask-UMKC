package com.aseproject.askumkc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ProfileUpdate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
    }
    public void updateProfile(View view)
    {
        Intent intent= new Intent(this,HomeScreen.class);
        startActivity(intent);
    }
}
