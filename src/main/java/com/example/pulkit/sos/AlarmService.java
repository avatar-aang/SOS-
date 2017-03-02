package com.example.pulkit.sos;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class AlarmService extends Service implements SensorEventListener {

    double x, y, z, gravity, gravmax;
    long count;
    Sensor accelerator;
    SensorManager sm;
    boolean fall;
    public AlarmService() {
        x = y = z = 10000;
        gravity = 0;
        fall = false;
        gravmax = 10000;
        count = 0;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                sm = (SensorManager) getSystemService(SENSOR_SERVICE);
                accelerator = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            }
        };
          return Service.START_STICKY;
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

        if (gravity >= 90 && count >= 40) {
            fall = true;
            if (fall) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                      //  PendingIntent i = new PendingIntent(get)
                        Intent i = new Intent(getBaseContext(),SendMessage1.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplication().startActivity(i);
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
                //Toast.makeText(this, "free fall", Toast.LENGTH_SHORT).show();
            }
            gravity = count = 0;
        } else {
            //fallen.setText("you are safe");//Toast.makeText(this,"you are safe",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
