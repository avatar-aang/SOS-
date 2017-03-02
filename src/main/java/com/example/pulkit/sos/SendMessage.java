package com.example.pulkit.sos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SendMessage extends ActionBarActivity implements GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener,SensorEventListener{
    Thread t;


    Button button_stop;
    ImageButton button1,button_map;
    Random random;
    double x ,y,z,gravmin,gravmax;
    long count ;
    Sensor accelerator;
    SensorManager sm;
    int dTap=0;
    GestureDetectorCompat mDetector;
    private static final String DEBUG_TAG = "Gestures";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        Toast.makeText(this,"New activity is created",Toast.LENGTH_SHORT).show();
        button1 = (ImageButton) findViewById(R.id.sos_button);
        button_stop=(Button)findViewById(R.id.audio_stop_button);
        button_stop.setVisibility(View.INVISIBLE);
        button_map=(ImageButton)findViewById(R.id.temp_button);
        mDetector=new GestureDetectorCompat(this,this);
        mDetector.setOnDoubleTapListener(this);
        random=new Random();
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerator = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener((SensorEventListener) this,accelerator,SensorManager.SENSOR_DELAY_FASTEST);
        x = y = z  =10000;
        gravmin = 0;
        gravmax  = 10000;
        count  = 0;

    }

    public boolean onCreateOptionMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }



    public void showMap(View view)
    {
        Intent intent=new Intent(this,MapsActivity.class);
        startActivity(intent);
    }

    public void sendMessage(View view) throws IOException, InterruptedException {

        button1.setEnabled(false);
        button1.setImageResource(R.drawable.sosbtn2);

        startRecording();//To start recording
        Toast.makeText(this,"press me was clicked",Toast.LENGTH_SHORT).show();


        GPSTracker mygps = new GPSTracker(this);
        if (mygps.canGetLocation) {
            mygps.getLocation();
            Toast.makeText(this, mygps.getLatitude() + " " + mygps.getLongitude(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "not able to find location", Toast.LENGTH_LONG).show();
        }
        double lat = mygps.getLatitude();
        double longitude = mygps.getLongitude();





       /* String _Location=null;
            double lat = mygps.getLatitude();
            double longitude = mygps.getLongitude();
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation(lat, longitude, 1);
            if(null!=listAddresses&&listAddresses.size()>0){
                _Location = listAddresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
           // Toast.makeText(this,_Location+ "this is what we need",Toast.LENGTH_SHORT).show();

           // Toast.makeText(this, _Location + "this is the current location", Toast.LENGTH_SHORT).show();
          /*  SharedPreferences sp = getSharedPreferences("Contacts", Context.MODE_PRIVATE);
        String s1= sp.getString("1","chooseContact1");
        String s2= sp.getString("2","chooseContact2");
        String s3= sp.getString("3","chooseContact3");
        String s4= sp.getString("4","chooseContact4");
        String s5= sp.getString("5","chooseContact5");
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(s1,null,"aur kaise ho",null,null);
        */
        //Geocoder geoCoder ;

      //  Toast.makeText(this,ans +"this is the address",Toast.LENGTH_SHORT).show();

    }
    String audio_path=null;
    MediaRecorder mediaRecorder;

    String RandomAudioFileName="ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode=1;
    public void startRecording()
    {

        if(checkPermission())
        {
            audio_path= Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "SOS" +CreateRandomAudioFileName(3) + "Recording.3gp";
            MediaRecorderReady();

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalStateException e){
                e.printStackTrace();
                             }
            Toast.makeText(this,"Recording started", Toast.LENGTH_SHORT).show();
            button_stop.setVisibility(View.VISIBLE);
        }
        else
        {
            requestPermission();
        }
    }

    public void stopRecording(View view)
    {
        mediaRecorder.stop();
        button_stop.setVisibility(View.INVISIBLE);
        button1.setEnabled(true);
        button1.setImageResource(R.drawable.sosbtn);
        Toast.makeText(this, "Recording complete", Toast.LENGTH_SHORT).show();
    }

    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(audio_path);
    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(SendMessage.this,new String[]{WRITE_EXTERNAL_STORAGE,RECORD_AUDIO},RequestPermissionCode);
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
                        Toast.makeText(this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission()
    {
        int result= ContextCompat.checkSelfPermission(getApplicationContext(),WRITE_EXTERNAL_STORAGE);
        int result1=ContextCompat.checkSelfPermission(getApplicationContext(),RECORD_AUDIO);
        return result==PackageManager.PERMISSION_GRANTED && result1==PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
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
        //Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());

        Toast.makeText(this,"double tap",Toast.LENGTH_SHORT).show();
        dTap=1;
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
    public void onSensorChanged(SensorEvent event)
    {
        double value  = Math.sqrt((event.values[0] * event.values[0]) + (event.values[1] * event.values[1]) + (event.values[2] * event.values[2]));
        if(event.values[0]< x)
            x = event.values[0];
        if(event.values[1]< y)
            y = event.values[1];
        if(event.values[2]< z) {
            z = event.values[2];
        }
        if(value < 1) {
            //grav =  Math.sqrt((event.values[0] * event.values[0]) + (event.values[1] * event.values[1]) + (event.values[2] * event.values[2]));
            count++;
        }
        if(value > gravmin)
            gravmin = value;
        //acceleration.setText("X: "+x +"\n"+"Y: "+y+"\n"+"Z: "+z+"\n"+"Gravity: "+gravmin+"\n"+"count" + count +"\n");
        //Toast.makeText(this, event.values[0]+" "+event.values[1]+" "+event.values[2]+" "+gravmin, Toast.LENGTH_SHORT).show();
        if(gravmin >=  90  && count >= 40)
        {
            //fallen.setText("free fall");
            Toast.makeText(this,"free fall",Toast.LENGTH_SHORT).show();

        }
        else
        {
            //fallen.setText("you are safe");
            //Toast.makeText(this,"you are safe",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
    final class GPSTracker implements LocationListener {

        private final Context mContext;

        // flag for GPS status
        public boolean isGPSEnabled = false;

        // flag for network status
        boolean isNetworkEnabled = false;

        // flag for GPS status
        boolean canGetLocation = false;

        Location location; // location
        double latitude; // latitude
        double longitude; // longitude

        // The minimum distance to change Updates in meters
        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

        // The minimum time between updates in milliseconds
        private static final long MIN_TIME_BW_UPDATES = 1; // 1 minute

        // Declaring a Location Manager
        protected LocationManager locationManager;

        public GPSTracker(Context context) {
            this.mContext = context;
            getLocation();
        }

        /**
         * Function to get the user's current location
         *
         * @return
         */
        public Location getLocation() {

            locationManager = (LocationManager) mContext
                    .getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            Log.v("isGPSEnabled", "=" + isGPSEnabled);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            Log.v("isNetworkEnabled", "=" + isNetworkEnabled);

            if (isGPSEnabled == false && isNetworkEnabled == false) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    location = null;
                    try {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                    Log.d("Network", "Network");
                    if (locationManager != null) {

                        try {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    location = null;
                    if (location == null) {

                        try {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        } catch (SecurityException e1) {
                            e1.printStackTrace();
                        }


                    }
                    Log.d("GPS Enabled", "GPS Enabled");
                    if (locationManager != null) {
                        try {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        } catch (SecurityException e1) {
                            e1.printStackTrace();
                        }
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            }

            return location;
        }

        /**
         * Stop using GPS listener Calling this function will stop using GPS in your
         * app
         */
        public void stopUsingGPS() {
            if (locationManager != null) {

                try {
                    locationManager.removeUpdates(GPSTracker.this);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Function to get latitude
         */
        public double getLatitude() {
            if (location != null) {
                latitude = location.getLatitude();
            }

            // return latitude
            return latitude;
        }

        /**
         * Function to get longitude
         */
        public double getLongitude() {
            if (location != null) {
                longitude = location.getLongitude();
            }

            // return longitude
            return longitude;
        }

        /**
         * Function to check GPS/wifi enabled
         *
         * @return boolean
         */
        public boolean canGetLocation() {
            return this.canGetLocation;
        }

        /**
         * Function to show settings alert dialog On pressing Settings button will
         * lauch Settings Options
         */
        public void showSettingsAlert() {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

            // Setting Dialog Title
            alertDialog.setTitle("GPS is settings");

            // Setting Dialog Message
            alertDialog
                    .setMessage("GPS is not enabled. Do you want to go to settings menu?");

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            mContext.startActivity(intent);
                        }
                    });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            // Showing Alert Message
            alertDialog.show();
        }

        @Override
        public void onLocationChanged(Location location) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }




