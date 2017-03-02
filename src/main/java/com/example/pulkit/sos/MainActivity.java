package com.example.pulkit.sos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button button1,button2,button3,button4,button5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = (Button) findViewById(R.id.contact1);
        button2 = (Button) findViewById(R.id.contact2);
        button3 = (Button) findViewById(R.id.contact3);
        button4 = (Button) findViewById(R.id.contact4);
        button5 = (Button) findViewById(R.id.contact5);
        load();
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
        String s1= sp.getString("1_1","chooseContact1");
        String s2= sp.getString("2_1","chooseContact2");
        String s3= sp.getString("3_1","chooseContact3");
        String s4= sp.getString("4_1","chooseContact4");
        String s5= sp.getString("5_1","chooseContact5");
        button1.setText(s1);
        button2.setText(s2);
        button3.setText(s3);
        button4.setText(s4);
        button5.setText(s5);
        return;
    }



}
