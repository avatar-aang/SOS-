package com.example.pulkit.sos;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SendMessage1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener,SensorEventListener {
    boolean fall = false;
    ImageButton button1, button_map, button_stop;
    Random random;
    double x, y, z, gravity, gravmax;
    long count;
    Sensor accelerator;
    SensorManager sm;
    int dTap = 0;
    TextView timer;
    GestureDetectorCompat mDetector;
    private static final String DEBUG_TAG = "Gestures";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = new Intent(this,AlarmService.class);
        startService(i);

        timer=(TextView) findViewById(R.id.timer);
        button1 = (ImageButton) findViewById(R.id.sos_button);
        button_stop = (ImageButton) findViewById(R.id.audio_stop_button);
        button_stop.setVisibility(View.INVISIBLE);
        button_map = (ImageButton) findViewById(R.id.temp_button);
        mDetector = new GestureDetectorCompat(this, this);
        mDetector.setOnDoubleTapListener(this);
        random = new Random();
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerator = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener((SensorEventListener) this, accelerator, SensorManager.SENSOR_DELAY_FASTEST);
//
        x = y = z = 10000;
        gravity = 0;
        gravmax = 10000;
        count = 0;


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Go to your current location", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(SendMessage1.this, MapsActivity.class);
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






    public void sendMessage(View view) throws IOException, InterruptedException {
        button1.setEnabled(false);
        button1.setImageResource(R.drawable.sosbtn2);
        timer.setVisibility(View.VISIBLE);
        timer.setText("00:00:10");
        Toast.makeText(SendMessage1.this,"Double tap within 10 seconds to untrigger SOS",Toast.LENGTH_LONG).show();
        new SendMessageTask().execute(view);
        //GPSTracker1 mygps2 = new GPSTracker1(this);
         //mygps2.getLocation();
        //Location location1 = mygps.getLocation();
     //   boolean second = true;

       // lat = mygps2.getLatitude();
       // longitude =mygps2.getLongitude();
        //Log.d("hehe",""+lat+" "+longitude );
        //Log.d("hehe outside the thread",""+lat+" "+longitude );
        //Thread locate=new Thread(new Runnable() {

          /*  public void run() {

                Log.d("hehe inside the thread",""+lat+" "+longitude );
                Geocoder geocoder;
                geocoder  = new Geocoder(SendMessage1.this,Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(lat,longitude,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(addresses!=null) {
                    address = addresses.get(0).getAddressLine(0);
                    city = addresses.get(0).getLocality();
                    state = addresses.get(0).getAdminArea();
                    country = addresses.get(0).getCountryName();
                    postal_code = addresses.get(0).getPostalCode();
                    known_name = addresses.get(0).getFeatureName();
                    Toast.makeText(SendMessage1.this,address +" "+ city + " "+ state + " "+ country + " "+ postal_code+" "+known_name,Toast.LENGTH_LONG).show();
                    //publishProgress(100);
                }

            }
        });*/ // uncomment this
        //locate.start();
//        mygps.getLocation();
//        lat = mygps.getLatitude();
//        longitude = mygps.getLongitude();
//        Log.d("hehe",""+lat+" "+longitude );
//        Geocoder geocoder;
//        geocoder  = new Geocoder(SendMessage1.this,Locale.getDefault());
//        try {
//            addresses = geocoder.getFromLocation(lat,longitude,1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if(addresses!=null) {
//            address = addresses.get(0).getAddressLine(0);
//            city = addresses.get(0).getLocality();
//            state = addresses.get(0).getAdminArea();
//            country = addresses.get(0).getCountryName();
//            postal_code = addresses.get(0).getPostalCode();
//            known_name = addresses.get(0).getFeatureName();
//            Toast.makeText(SendMessage1.this,address +" "+ city + " "+ state + " "+ country + " "+ postal_code+" "+known_name,Toast.LENGTH_LONG).show();
//            //publishProgress(100);
//        }

    }

//
//        button1.setEnabled(false);
//        button1.setImageResource(R.drawable.sosbtn2);
//
//
//
        //GPSTracker1 mygps = new GPSTracker1(this);
//        if (mygps.canGetLocation) {
//            mygps.getLocation();
//            Toast.makeText(this, mygps.getLatitude() + " " + mygps.getLongitude(), Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Turn on the GPS", Toast.LENGTH_LONG).show();
//            button1.setEnabled(true);
//            button1.setImageResource(R.drawable.sosbtn);
//            return;
//        }


        //startRecording();
        //Toast.makeText(this,"press me was clicked",Toast.LENGTH_SHORT).show();




//        String _Location=null;
//            double lat = mygps.getLatitude();
//            double longitude = mygps.getLongitude();
//        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//        try {
//            List<Address> listAddresses = geocoder.getFromLocation(lat, longitude, 1);
//            if(null!=listAddresses&&listAddresses.size()>0){
//                _Location = listAddresses.get(0).getAddressLine(0);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        SharedPreferences sp =getSharedPreferences("Contacts", Context.MODE_PRIVATE);
//        String s1= sp.getString("1","chooseContact1");
//        String s2= sp.getString("2","chooseContact2");
//        String s3= sp.getString("3","chooseContact3");
//        String s4= sp.getString("4","chooseContact4");
//        String s5= sp.getString("5","chooseContact5");
//        SmsManager sms = SmsManager.getDefault();
//        /*if(!s1.equals("chooseContact1"))
//           sms.sendTextMessage(s1,null,"aur kaise ho",null,null);*/   //must be uncommented
//        if(!s2.equals("chooseContact2"))
//            sms.sendTextMessage(s2,null,"aur kaise ho",null,null);
//        if(!s3.equals("chooseContact3"))
//            sms.sendTextMessage(s3,null,"aur kaise ho",null,null);
//        if(!s4.equals("chooseContact4"))
//            sms.sendTextMessage(s4,null,"aur kaise ho",null,null);
//        if(!s5.equals("chooseContact5"))
//           sms.sendTextMessage(s5,null,"aur kaise ho",null,null);
//
//        //Geocoder geoCoder ;
//
//        //  Toast.makeText(this,ans +"this is the address",Toast.LENGTH_SHORT).show();
//
//    }

    String audio_path = null;
    MediaRecorder mediaRecorder;

    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;

//    public void startRecording() {
//
//        if (checkPermission()) {
//            String folder_name = "SOS_Recordings";
//            File f = new File(Environment.getExternalStorageDirectory(), folder_name);
//            if (!f.exists())
//                f.mkdirs();
//            audio_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SOS_Recordings/" + "SOS" + CreateRandomAudioFileName(3) + "Recording.3gp";
//            Toast.makeText(SendMessage1.this, audio_path, Toast.LENGTH_SHORT).show();
//            MediaRecorderReady();
//
//            try {
//                mediaRecorder.prepare();
//                mediaRecorder.start();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (IllegalStateException e) {
//                e.printStackTrace();
//            }
//            Toast.makeText(SendMessage1.this, "Recording started", Toast.LENGTH_SHORT).show();
//            button_stop.setVisibility(View.VISIBLE);
//        } else {
//            requestPermission();
//        }
//    }

    public void stopRecording(View view) {
        mediaRecorder.stop();
        button_stop.setVisibility(View.INVISIBLE);
        button1.setEnabled(true);
        button1.setImageResource(R.drawable.sosbtn);
        Toast.makeText(SendMessage1.this, "Recording complete", Toast.LENGTH_SHORT).show();
    }

    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(audio_path);
    }

    public String CreateRandomAudioFileName(int string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        int i = 0;
        while (i < string) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(SendMessage1.this, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(SendMessage1.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SendMessage1.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
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
        getMenuInflater().inflate(R.menu.send_message1, menu);
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

            Intent intent = new Intent(this, MainActivity1.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_about_us) {
            return true;
        } else if (id == R.id.menu_info) {
            return true;
        } else if (id == R.id.menu_rate_us) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //     return super.onOptionsItemSelected(item);


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_edit_contact) {
            Intent intent = new Intent(this, MainActivity1.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_open_map) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_view_recordings) {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/SOS_Recordings/");
            i.setDataAndType(uri, "text/mp3");
            startActivity(i);

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "SOS APP");
            String str = "\n this is an awesome app ";
            i.putExtra(Intent.EXTRA_TEXT, str);
            startActivity(Intent.createChooser(i, "choose one"));
        } //else if (id == R.id.nav_send) {

        //}


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        this.mDetector.onTouchEvent(event);
//        // Be sure to call the superclass implementation
////        super.onTouchEvent(event);
//        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if(mDrawerLayout.isDrawerOpen(GravityCompat.START) ||
//                mDrawerLayout.isDrawerVisible(GravityCompat.START)){
//            return true;
//        }
//        return super.onTouchEvent(event);
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        //Log.d(DEBUG_TAG,"onDown: " + event.toString());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        //Log.d(DEBUG_TAG, "onFling: " + event1.toString()+event2.toString());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        //Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
        //Toast.makeText(this,"long pressed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        //Log.d(DEBUG_TAG, "onScroll: " + e1.toString()+e2.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        //Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        //Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());

        Toast.makeText(this, "double tap", Toast.LENGTH_SHORT).show();
        dTap = 1;
        timer.setVisibility(View.INVISIBLE);
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        //Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        //Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
        return true;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        double value = Math.sqrt((event.values[0] * event.values[0]) + (event.values[1] * event.values[1]) + (event.values[2] * event.values[2]));
        if (event.values[0] < x)
            x = event.values[0];
        if (event.values[1] < y)
            y = event.values[1];
        if (event.values[2] < z) {
            z = event.values[2];
        }
        if (value < 1) {
            //grav =  Math.sqrt((event.values[0] * event.values[0]) + (event.values[1] * event.values[1]) + (event.values[2] * event.values[2]));
            count++;
        }
        if (value > gravity)
            gravity = value;
        //acceleration.setText("X: "+x +"\n"+"Y: "+y+"\n"+"Z: "+z+"\n"+"Gravity: "+gravmin+"\n"+"count" + count +"\n");
        //Toast.makeText(this, event.values[0]+" "+event.values[1]+" "+event.values[2]+" "+gravmin, Toast.LENGTH_SHORT).show();

        if (gravity >= 90 && count >= 40) {
            fall = true;
            if (fall) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MediaPlayer player;
                        AssetFileDescriptor afd = null;
                        try {
                            afd = getAssets().openFd("Alarm.mp3");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player = new MediaPlayer();
                        try {
                            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            player.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.start();
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //      player.stop();
                    }
                });
                t.start();

                //fallen.setText("free fall");
                Toast.makeText(this, "free fall", Toast.LENGTH_SHORT).show();
            }
            gravity = count = 0;
        } else {
            //fallen.setText("you are safe");
            //Toast.makeText(this,"you are safe",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    class SendMessageTask extends AsyncTask<View, Integer, Void> {
        View view;
        String address,city,state,country,postal_code,known_name;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(View... view) {


            for (int i = 1; i <= 20; i++) {
                try {
                    Thread.sleep(500);
                    if(i%2 == 0)
                    {

                        publishProgress(500*i/1000+10);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (dTap == 1) {
                    dTap=0;
                    publishProgress(2);
                    return null;
                }
            }

            publishProgress(1);

//            double lat=0.0;
//            double longitude=0.0;
//            List<Address> addresses=null;
//            GPSTracker1 mygps = new GPSTracker1(SendMessage1.this);
//            Log.d("hehe","I am above ggetLocation");
//                mygps.getLocation();
//                publishProgress(200);
//                lat = mygps.getLatitude();
//                longitude = mygps.getLongitude();
//                Log.d("hehe",""+lat+" "+longitude );
//                Geocoder geocoder;
//                geocoder  = new Geocoder(SendMessage1.this,Locale.getDefault());
//                try {
//                    addresses = geocoder.getFromLocation(lat,longitude,1);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                 if(addresses!=null) {
//                     address = addresses.get(0).getAddressLine(0);
//                     city = addresses.get(0).getLocality();
//                     state = addresses.get(0).getAdminArea();
//                     country = addresses.get(0).getCountryName();
//                     postal_code = addresses.get(0).getPostalCode();
//                     known_name = addresses.get(0).getFeatureName();
//                     publishProgress(100);
//                 }





                //Toast.makeText(SendMessage1.this, mygps.getLatitude() + " " + mygps.getLongitude(), Toast.LENGTH_SHORT).show();



            startRecording();
            //Toast.makeText(SendMessage1.this, "press me was clicked", Toast.LENGTH_SHORT).show();


//            String _Location = null;
//            double lat = mygps.getLatitude();
//            double longitude = mygps.getLongitude();
//            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//            try {
//                List<Address> listAddresses = geocoder.getFromLocation(lat, longitude, 1);
//                if (null != listAddresses && listAddresses.size() > 0) {
//                    _Location = listAddresses.get(0).getAddressLine(0);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }//uncomment this
           // String messageSent="Hey , I am in danger.Please help me ASAP!! My location is Latitude:"+lat+" Longitude:"+longitude;
            //if(addresses!=null)
              //  messageSent = messageSent + address +" "+ city + " "+ state + " "+ country + " "+ postal_code+" "+known_name;
            SharedPreferences sp = getSharedPreferences("Contacts", Context.MODE_PRIVATE);
            String s1 = sp.getString("1", "chooseContact1");
            String s2 = sp.getString("2", "chooseContact2");
            String s3 = sp.getString("3", "chooseContact3");
            String s4 = sp.getString("4", "chooseContact4");
            String s5 = sp.getString("5", "chooseContact5");
            SmsManager sms = SmsManager.getDefault();
            Log.d("hehe",s1);
//            if(!s1.equals("chooseContact1"))
//                sms.sendTextMessage(s1,null,messageSent,null,null);   //must be uncommented
//            if (!s2.equals("chooseContact2"))
//                sms.sendTextMessage(s2, null,messageSent, null, null); //must be uncommented
            /*if (!s3.equals("chooseContact3"))
                sms.sendTextMessage(s3, null,messageSent, null, null);
            if (!s4.equals("chooseContact4"))
                sms.sendTextMessage(s4, null,messageSent, null, null);
            if (!s5.equals("chooseContact5"))
                sms.sendTextMessage(s5, null,messageSent, null, null);*/

            //Geocoder geoCoder ;

            //  Toast.makeText(this,ans +"this is the address",Toast.LENGTH_SHORT).show();


            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            if (values[0] == 1) {
                button1.setEnabled(false);
                button1.setImageResource(R.drawable.sosbtn2);
            } else if(values[0]==2){
                button1.setEnabled(true);
                button1.setImageResource(R.drawable.sosbtn);
            }
            else if(values[0]==3)
            {
                button_stop.setVisibility(View.VISIBLE);
            }
            else {

                if(20-values[0] != 10) {
                    timer.setText("00:00:0" + (20 - values[0]));
                    if(20-values[0] == 0)
                        timer.setVisibility(View.INVISIBLE);
                }
                else
                    timer.setText("00:00:"+(20-values[0]));
            }

        }



        public void startRecording() {

            if (checkPermission()) {
                String folder_name = "SOS_Recordings";
                File f = new File(Environment.getExternalStorageDirectory(), folder_name);
                if (!f.exists())
                    f.mkdirs();
                audio_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SOS_Recordings/" + "SOS" + CreateRandomAudioFileName(3) + "Recording.3gp";
              //  Toast.makeText(SendMessage1.this, audio_path, Toast.LENGTH_SHORT).show();
                MediaRecorderReady();

                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
               // Toast.makeText(SendMessage1.this, "Recording started", Toast.LENGTH_SHORT).show();
                // button_stop.setVisibility(View.VISIBLE);
                publishProgress(3);
            } else {
                requestPermission();
            }
        }


    }
}

//final class GPSTracker1 implements LocationListener {
//
//    boolean positiveGPS=false;
//    boolean positiveINTERNET=false;
//
//    private final Context mContext;
//
//    // flag for GPS status
//    public boolean isGPSEnabled = false;
//
//    // flag for network status
//    boolean isNetworkEnabled = false;
//
//    // flag for GPS status
//    boolean canGetLocation = false;
//
//    Location location; // location
//    double latitude; // latitude
//    double longitude; // longitude
//
//    // The minimum distance to change Updates in meters
//    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
//
//    // The minimum time between updates in milliseconds
//    private static final long MIN_TIME_BW_UPDATES = 1; // 1 minute
//
//    // Declaring a Location Manager
//    protected LocationManager locationManager;
//    boolean first;
//    public GPSTracker1(Context context) {
//        this.mContext = context;
//        first=true;
//        getLocation();
//    }
//
//    /**
//     * Function to get the user's current location
//     *
//     * @return
//     */
//    public Location getLocation() {
//               // if(second==true)
//        Log.d("hehe","getLocation called");
//
//        locationManager = (LocationManager) mContext
//                .getSystemService(Context.LOCATION_SERVICE);
//
//        // getting GPS status
//        isGPSEnabled = locationManager
//                .isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//        Log.v("isGPSEnabled", "=" + isGPSEnabled);
//
//        // getting network status
//        isNetworkEnabled = locationManager
//                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//        Log.v("isNetworkEnabled", "=" + isNetworkEnabled);
//
//            if((isGPSEnabled!=false || isNetworkEnabled!=false) ){
//                this.canGetLocation = true;
//                if (isNetworkEnabled) {
//                    location = null;
//                    try {
//                        locationManager.requestLocationUpdates(
//                                LocationManager.NETWORK_PROVIDER,
//                                MIN_TIME_BW_UPDATES,
//                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                    } catch (SecurityException e) {
//                        e.printStackTrace();
//                    }
//                    Log.d("Network", "Network");
//                    if (locationManager != null) {
//
//                        try {
//                            location = locationManager
//                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                        } catch (SecurityException e) {
//                            e.printStackTrace();
//                        }
//                        if (location != null) {
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//                        }
//                    }
//                }
//
//                if (isGPSEnabled) {
//                    location = null;
//                    if (location == null) {
//
//                        try {
//                            locationManager.requestLocationUpdates(
//                                    LocationManager.GPS_PROVIDER,
//                                    MIN_TIME_BW_UPDATES,
//                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                        } catch (SecurityException e1) {
//                            e1.printStackTrace();
//                        }
//
//
//                    }
//                    Log.d("GPS Enabled", "GPS Enabled");
//                    if (locationManager != null) {
//                        try {
//                            location = locationManager
//                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                        } catch (SecurityException e1) {
//                            e1.printStackTrace();
//                        }
//                        if (location != null) {
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//                        }
//                    }
//                }
//            }
//
//
//        if (isGPSEnabled == false) {
//            // no network provider is enabled
//            showSettingsAlert(1);
//            Log.d("hehe","before");
//
//        }
//        if(isNetworkEnabled == false && first){
//            showSettingsAlert(2);
//        }
//
//        isGPSEnabled = locationManager
//                .isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//
//        // getting network status
//        isNetworkEnabled = locationManager
//                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//
//        if(isGPSEnabled!=false || isNetworkEnabled!=false) {
//            this.canGetLocation = true;
//            if (isNetworkEnabled) {
//                location = null;
//                try {
//                    locationManager.requestLocationUpdates(
//                            LocationManager.NETWORK_PROVIDER,
//                            MIN_TIME_BW_UPDATES,
//                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                } catch (SecurityException e) {
//                    e.printStackTrace();
//                }
//                Log.d("Network", "Network");
//                if (locationManager != null) {
//
//                    try {
//                        location = locationManager
//                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                    } catch (SecurityException e) {
//                        e.printStackTrace();
//                    }
//                    if (location != null) {
//                        latitude = location.getLatitude();
//                        longitude = location.getLongitude();
//                    }
//                }
//            }
//
//            if (isGPSEnabled) {
//                location = null;
//                if (location == null) {
//
//                    try {
//                        locationManager.requestLocationUpdates(
//                                LocationManager.GPS_PROVIDER,
//                                MIN_TIME_BW_UPDATES,
//                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                    } catch (SecurityException e1) {
//                        e1.printStackTrace();
//                    }
//
//
//                }
//                Log.d("GPS Enabled", "GPS Enabled");
//                if (locationManager != null) {
//                    try {
//                        location = locationManager
//                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                    } catch (SecurityException e1) {
//                        e1.printStackTrace();
//                    }
//                    if (location != null) {
//                        latitude = location.getLatitude();
//                        longitude = location.getLongitude();
//                    }
//                }
//            }
//        }
//        first=false;
//        return location;
//       // first=false;
//
//    }
//    public void stopUsingGPS() {
//        if (locationManager != null) {
//
//            try {
//                locationManager.removeUpdates(GPSTracker1.this);
//            } catch (SecurityException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    public double getLatitude() {
//        if (location != null) {
//            latitude = location.getLatitude();
//        }
//
//        // return latitude
//        return latitude;
//    }
//    public double getLongitude() {
//        if (location != null) {
//            longitude = location.getLongitude();
//        }
//
//        return longitude;
//    }
//
//    public boolean canGetLocation() {
//        return this.canGetLocation;
//    }
//
//    public void showSettingsAlert(int ind) {
//
//        if(ind==1) {
//            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
//
//            // Setting Dialog Title
//            alertDialog.setTitle("GPS settings");
//
//            // Setting Dialog Message
//            alertDialog
//                    .setMessage("GPS is not enabled. Do you want to go to settings menu?");
//
//
//            // On pressing Settings button
//            alertDialog.setPositiveButton("Settings",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            positiveGPS = true;
//                            Log.d("hehe", "" + positiveGPS);
//                            Intent intent = new Intent(
//                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            mContext.startActivity(intent);
//                        }
//                    });
//
//            // on pressing cancel button
//            alertDialog.setNegativeButton("Cancel",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//
//            // Showing Alert Message
//            alertDialog.show();
//        }
//        Log.d("hehe","here");
//        if(positiveGPS==true) {
//            Log.d("hehe","start");
//            Intent i = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            mContext.startActivity(i);
//        }
//
//        if(ind==2) {
//
//            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(mContext);
//            alertDialog2.setMessage("It looks like your internet connection is off.Do you want to turn it on ?")
//                    .setTitle("Unable to connect")
//                    .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
//                            mContext.startActivity(i);
//                        }
//
//                    })
//                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//            alertDialog2.show();
//        }
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//    }
//}