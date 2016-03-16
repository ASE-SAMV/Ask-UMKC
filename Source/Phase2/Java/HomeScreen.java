package com.aseproject.askumkc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(new basicAdapter(this, new String[]{"All graduate students interested in admission into any of our numerous graduate programs will need to meet with an academic advisor in their area of graduate studies interest. If you are interested in graduate studies and would still like to take a campus tour only, please contact the Welcome Center at 816-235-8652.",
                "Classes are not scheduled during weekday visits. For first time college students entering for the fall, you are required to attend orientation prior to setting up your classes. Transfer students can attend orientation or contact their advisor directly.",
        "During your visit, financial aid, scholarships, as well as housing information are all covered in the UMKC Admissions overview.\n" +
                "Transfer students, this information is not covered if you meet directly with your academic area. Please attend a weekday visit or UMKC Preview Day",
        "The Hospital Hill campus and the Volker campus are approximately 5 miles apart. For more information about the campus, please visit our campus maps page.",
        "The campus tour is a 60 minute long walking tour of the Volker campus."}));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            Intent intent= new Intent(this,ProfileUpdate.class);
            startActivity(intent);

            // Handle the camera action
        } else if (id == R.id.logout) {
            Intent intent= new Intent(this,login.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void onClickPostQuestion(View view)
    {
        Intent intent= new Intent(this,Questionary.class);
        startActivity(intent);
    }
}


class basicAdapter extends BaseAdapter {

   public Context context;
   public String[] data;
  public   String[] FAQuestions=new String[]{"I am a graduate student what are my visit options","I am an admitted student. Can I enroll in classes while at a weekday visit","How do I get information about financial aid or housing","How far is the Hospital Hill campus from the Volker Campus","How long is campus tour"};
    private static LayoutInflater inflater = null;

    public basicAdapter(Context context, String[] data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data[position];
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
        if (vi == null)
            vi = inflater.inflate(R.layout.row, null);
        TextView headText = (TextView) vi.findViewById(R.id.headText);
        headText.setText("Question: "+FAQuestions[position]);
        TextView text = (TextView)vi.findViewById(R.id.text);
        text.setText("Answer: "+data[position]);
        return vi;
    }
}