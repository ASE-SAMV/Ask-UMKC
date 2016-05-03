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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ProfileUpdate extends AppCompatActivity {

    Button s1;
    public int code = 0;
    ImageButton profilePic;
    Boolean status = false;
    Bitmap bm;
    ByteArrayOutputStream bs;
    //Image as String..!! Need to work on it.
    String imgString="";
    public EditText fname,lname,phone,passwrd,repasswrs2;
    TextView email;
    TextView username;
    String Userinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        profilePic = (ImageButton) findViewById(R.id.imgView);
        Intent intent=getIntent();
        Userinfo=intent.getStringExtra("userData");
        fname=(EditText)findViewById(R.id.first_Name);
        lname=(EditText)findViewById(R.id.last_Name);
        phone=(EditText)findViewById(R.id.phone);
        email=(TextView)findViewById(R.id.pemail_id);
        username=(TextView)findViewById(R.id.puser_Name);
        passwrd=(EditText)findViewById(R.id.password);
        repasswrs2=(EditText)findViewById(R.id.password2);
        try {
            JSONObject jsonObject = new JSONObject(Userinfo);
            username.setText(jsonObject.getString("username"));
            username.setEnabled(false);
            fname.setText(jsonObject.getString("firstname"));
            lname.setText(jsonObject.getString("lastname"));
            phone.setText(jsonObject.getString("phone_number"));
            email.setText(jsonObject.getString("emailid"));
            email.setEnabled(false);
            passwrd.setText("");
            repasswrs2.setText("");
        }
        catch (JSONException e){}

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent=getIntent();
        intent.putExtra("userData",Userinfo);
        setResult(1, intent);
        finish();
    }

    public void updateProfile(View view) {
        //Addded by vikesh
        Boolean validUser=false;

        if(fname.getText().toString().length()>0 && lname.getText().toString().length()>0 && phone.getText().toString().length()>0
                &&email.getText().toString().length()>0 && username.getText().toString().length()>0 &&
                passwrd.getText().toString().length()>0 && repasswrs2.getText().toString().length()>0 )
        {

            if( passwrd.getText().toString().equals(repasswrs2.getText().toString())) {
                JSONObject prof = new JSONObject();
                try {

                        JSONObject jsonObject = new JSONObject(Userinfo);

                        //authorTextView.setText("zdkjbhdf");

                    String s=jsonObject.getString("user_id");

                   // imgString=jsonObject.getString("img");
                    jsonObject.put("firstname", fname.getText().toString());
                    jsonObject.put("lastname", lname.getText().toString());
                    jsonObject.put("phone_number", phone.getText().toString());
                    jsonObject.put("password", passwrd.getText().toString());
                   //imgString= imgString.replace("\n","");
                    jsonObject.put("img",imgString);
                    jsonObject.put("user_id",s);
//                    ArrayList sam=new ArrayList();
//                    jsonObject.put("followed_questions", new JSONArray(sam));
                    sample s1 = new sample();
                    s1.fun1(jsonObject.toString(), "https://api.mongolab.com/api/1/databases/askumkc/collections/users?apiKey=7rL6L7EoWpZnPgyjtHDS8Rwh6VDer-ic");
                    onBackPressed();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "paswords are not equal", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_LONG).show();
        }
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
//            bm = photo;
//            profilePic.setImageBitmap(photo);
//            imgString=BitMapToString(photo);
//            Log.d("Image",imgString);
//            Log.d("CameraDemo", "Pic saved");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            String strBase64=Base64.encodeToString(byteArray, 0);
            imgString=strBase64;
            Log.d("Val",imgString);



        }
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

}
