package com.aseproject.askumkc;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by andrei on 22.06.2015.
 */
public abstract class AbstractGetNameTask extends AsyncTask<Void, Void, Void> {

    protected login mActivity;
    public static String GOOGLE_USER_DATA = "No data";
    protected String mScope;
    protected String mEmail;
    protected int mRequest;
    Boolean first=true;
    public AbstractGetNameTask(login mActivity, String mEmail, String mScope) {
        this.mActivity = mActivity;
        this.mEmail = mEmail;
        this.mScope = mScope;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            fetchNameFromProfileServer();
        } catch (IOException ex) {
            onError("Following Error occured, please try again. "
                    + ex.getMessage(), ex);
        } catch (JSONException e) {
            onError("Bad response: "
                    + e.getMessage(), e);
        }
        return null;
    }

    protected void onError(String msg, Exception e) {
        if(e != null) {
            Log.e("", "Exception: ", e);
        }
    }

    protected abstract String fetchToken() throws IOException;

    private void fetchNameFromProfileServer() throws IOException, JSONException {
        String token = fetchToken();

        URL url = new URL("https://www.googleapis.com/oauth2" +
                "/v1/userinfo?access_token=" + token);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        int sc = con.getResponseCode();
        if(sc == 200) {
            InputStream is = con.getInputStream();
            GOOGLE_USER_DATA = readResponse(is);
            is.close();
            String gmailname="",gmailgender="",gmailimgurl="";
            Log.d("Email",mEmail);
            String val="";
            try {




                JSONObject profileData = new JSONObject(AbstractGetNameTask.GOOGLE_USER_DATA);
                val=profileData.toString();
                Log.d("Data",profileData.toString());

                if(profileData.has("picture")) {
                    gmailimgurl = profileData.getString("picture");
                    Log.d("in the picture", "in the picture");

                }
                if(profileData.has("name")) {
                    gmailname = profileData.getString("name");
                    Log.d("IN the Name method", gmailname);

                }
                if(profileData.has("gender")) {
                    gmailgender = profileData.getString("gender");
                    Log.d("IN the gender method", gmailgender);

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            if(first)
            {
                Intent intent= new Intent(mActivity,HomeScreen.class);
                JSONObject x=new JSONObject("{\"_id\":{\"$oid\":\"57054de3e4b027cbe28c6151\"},\"firstname\":\"Manikanta\",\"lastname\":\"Maddula\",\"phone_number\":\"8162996311\",\"emailid\":\"manikanta.maddula@gmail.com\",\"username\":\"manimad\",\"user_id\":\"1\",\"password\":\"6353\",\"img\":\"mani\",\"followed_questions\":[1000011,1000012,1000016,1000014]}");
                intent.putExtra("userData",x.toString());
                mActivity.startActivityForResult(intent, 1);
                first=false;
            }
            return;
        } else if(sc == 401) {
            GoogleAuthUtil.invalidateToken(mActivity, token);
            onError("Server auth error: ", null);
        } else {
            onError("Returned by server: "
                    + sc, null);
            return;
        }
    }

    private static String readResponse(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] data = new byte[2048];
        int len = 0;
        while((len = is.read(data, 0, data.length)) >= 0) {
            bos.write(data, 0, len);
        }

        return new String(bos.toByteArray(), "UTF-8");
    }
}
