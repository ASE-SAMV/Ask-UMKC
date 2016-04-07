package com.aseproject.askumkc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class ProfileUpdate extends AppCompatActivity {

    Button s1;
    public int code = 0;
    ImageButton profilePic;
    Boolean status = false;
    Bitmap bm;
    ByteArrayOutputStream bs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        profilePic = (ImageButton) findViewById(R.id.imgView);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent=getIntent();
        setResult(1, intent);
        finish();
    }

    public void updateProfile(View view) {
        Intent intent=getIntent();
        setResult(1,intent);
        finish();
    }

    public void getPic(View view) {
        CharSequence[] textArray = new CharSequence[1];
        textArray[0] = "Camera";

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Object o = requestCode;
        Log.d("request", o.toString());
        o = resultCode;
        Log.d("result", o.toString());
        if (requestCode == 0 && resultCode == RESULT_OK) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            bm = photo;
            profilePic.setImageBitmap(photo);
            Log.d("CameraDemo", "Pic saved");

        }
    }
}
