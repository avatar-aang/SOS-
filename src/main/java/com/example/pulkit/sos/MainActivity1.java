package com.example.pulkit.sos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button button1,button2,button3,button4,button5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        button1 = (Button) findViewById(R.id.contact1);
        button2 = (Button) findViewById(R.id.contact2);
        button3 = (Button) findViewById(R.id.contact3);
        button4 = (Button) findViewById(R.id.contact4);
        button5 = (Button) findViewById(R.id.contact5);
        load();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "go to your current location", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity1.this,MapsActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void openContacts(View view)
    {
        // Intent intent = null;
        int choose_button;
        if(view.getId() == R.id.contact1)
            choose_button = 1;
        else if(view.getId() ==R.id.contact2)
            choose_button = 2;
        else if(view.getId() == R.id.contact3)
            choose_button = 3;
        else if(view.getId() == R.id.contact4)
            choose_button = 4;
        else
            choose_button = 5;

        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        // Toast.makeText(this,"this is me",Toast.LENGTH_SHORT).show();
        startActivityForResult(intent, choose_button);
        Toast.makeText(this,"this is me",Toast.LENGTH_SHORT).show();

    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if(resultCode == 0)
            return ;
        String cNumber=null;
        String name = null;

        if (resultCode == Activity.RESULT_OK) {

            Uri contactData = data.getData();
            Cursor c =  managedQuery(contactData, null, null, null, null);
            if (c.moveToFirst()) {


                String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                if (hasPhone.equalsIgnoreCase("1")) {
                    Cursor phones = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,
                            null, null);
                    phones.moveToFirst();
                    cNumber = phones.getString(phones.getColumnIndex("data1"));
                    System.out.println("number is:"+cNumber);
                }
                name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


            }
        }
        storeData(name,cNumber,reqCode);
        showSelectedNumber(cNumber);
        switch (reqCode)
        {
            case 1:
                button1.setText(name);
                break;
            case 2:
                button2.setText(name);
                break;
            case 3:
                button3.setText(name);
                break;
            case 4:
                button4.setText(name);
                break;
            case 5:
                button5.setText(name);
                break;
        }
    }
    public void showSelectedNumber(String number) {
        Toast.makeText(this,": " + number, Toast.LENGTH_LONG).show();
    }
    public void storeData(String name,String cNumber,int choose_button)
    {
        SharedPreferences sp = getSharedPreferences("Contacts", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed= sp.edit();
        ed.putString(Integer.toString(choose_button),cNumber);
        ed.putString(Integer.toString(choose_button)+"_1",name);
        ed.commit();
        Toast.makeText(this,"data saved successfully"+cNumber,Toast.LENGTH_SHORT).show();
    }
    public void load()
    {
        SharedPreferences sp = getSharedPreferences("Contacts", Context.MODE_PRIVATE);
        String s1= sp.getString("1_1","choose Contact 1");
        String s2= sp.getString("2_1","choose Contact 2");
        String s3= sp.getString("3_1","choose Contact 3");
        String s4= sp.getString("4_1","choose Contact 4");
        String s5= sp.getString("5_1","choose Contact 5");
        button1.setText(s1);
        button2.setText(s2);
        button3.setText(s3);
        button4.setText(s4);
        button5.setText(s5);
        return;
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
        getMenuInflater().inflate(R.menu.main_activity1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_update_contacts) {
            Intent intent = new Intent(this,MainActivity1.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.menu_about_us)
        {
            return true;
        }
        else if(id == R.id.menu_info)
        {
            return true;
        }
        else if(id == R.id.menu_rate_us)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_edit_contact) {
            Intent intent = new Intent(this,MainActivity1.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_open_map) {
            Intent intent = new Intent(this,MapsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_view_recordings) {
            Intent i=new Intent(Intent.ACTION_GET_CONTENT);
            Uri uri=Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/SOS_Recordings/");
            i.setDataAndType(uri,"text/mp3");
            startActivity(i);

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT,"SOS APP");
            String str = "\n this is an awesome app ";
            i.putExtra(Intent.EXTRA_TEXT,str);
            startActivity(Intent.createChooser(i,"choose one"));

        } //else if (id == R.id.nav_send) {

        //}

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
