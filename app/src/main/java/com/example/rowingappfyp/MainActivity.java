package com.example.rowingappfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LocationListener {

    //Declaring Variables that are used

    //this is an array that stores the co-ordinates
    float[] Dist = new float[1];

    boolean Pause;
    TextView timerText;
    Button stopStartButton;
    Timer timer;
    TimerTask timerTask;
    Double time = 0.0;
    boolean timerStarted = false;
    int totalAmountOfSeconds = 0;
    int seconds;
    int minutes;
    int hours;
    int mins;
    int secs;
    int split;

    String Speed;
    TextView tvDistance;
    TextView tvSpeed;
    Button btnStartLocation;
    Button btnStop;
    Button btnReset;
    TextView tvShowStartLoc;
    TextView tvTime;
    LocationManager locationManager;
    ArrayList<Double> GPSLatitude = new ArrayList<Double>();
    ArrayList<Double> GPSLongitude = new ArrayList<Double>();
    float addDistanceUp = 0;
    Float Distance;
    int NoDecDistance;
    int sessionID = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            DatabaseObject db = new DatabaseObject(MainActivity.this);
            sessionID = db.sessionID() + 1;
        } catch (Exception e){
            sessionID = 1;
        }



        // this is setting variables to be equal to something in the GUI like a button.

        timerText = (TextView) findViewById(R.id.Timer);
     //   stopStartButton = (Button) findViewById(R.id.btnStop);

        timer = new Timer();

      //  tvShowStartLoc = findViewById(R.id.tvshowstartloc);
        btnStartLocation = findViewById(R.id.btnstart);
      //  btnStop = findViewById(R.id.btnStop);
        btnReset = findViewById(R.id.btnReset);
        tvDistance = findViewById(R.id.tvDist);
        tvSpeed = findViewById(R.id.tvSpeed);
      //  tvTime = findViewById(R.id.tvTime);

        //Runtime permissions so we can track location of user
        // reference [2]
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION

            }, 100);

        }

         // method for when the start button is pressed
        // pause is the boolean that is false everything stops running
        //startStopTapped is a method that either stops or starts the timer. [ref 4]
        // getLocation starts the "OnLocationChanged" method if the location has changed.[ref 3]
        btnStartLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pause = false;
                startStopTapped(timerText);
                getLocation();
            }
        });




        // If this is pressed it restarts the timer[ref 4] and increases the session ID by 1.
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sessionID++ ;
                resetTapped(timerText);
            }
        });
    }

    //this is used above in btnReset. I got it from [ref 4]
    public void resetTapped(View view) {

        if (timerTask != null) {
            timerTask.cancel();

            Pause = true;
            btnStartLocation.setText("Start");
            time = 0.0;
            addDistanceUp = 0;
            Distance = 0.0f ;
            GPSLatitude.clear();
            GPSLongitude.clear();
            i = 0;
            Arrays.fill(Dist,0);

            tvDistance.setText(0 + "m");
            tvSpeed.setText(0 + "");

            timerStarted = false;
            timerText.setText(formatTime(0, 0, 0));

        }

    }

    ;

    //this is used above in btnStart. I got it from [ref 4]
    public void startStopTapped(View view) {
        if (timerStarted == false) {
            timerStarted = true;
            Pause = false;

            startTimer();
        } else {
            timerStarted = false;
            Pause = true;
            Distance = 0.0f ;
            GPSLatitude.clear();
            GPSLongitude.clear();
            i = 0;
            Arrays.fill(Dist,0);
            btnStartLocation.setText("Start");
            timerTask.cancel();
        }
    }

    //this is used above in startStopTapped method. I got it from [ref 4]
    private void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        btnStartLocation.setText("Pause");
                        timerText.setText(getTimerText());
                    }
                });
            }

        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    //this is used above in startStopTapped and startTimer methods . I got it from [ref 4]
    private String getTimerText() {
        int rounded = (int) Math.round(time);

        seconds = ((rounded % 86400) % 3600) % 60;
        minutes = ((rounded % 86400) % 3600) / 60;
        hours = ((rounded % 86400) / 3600);

        return formatTime(seconds, minutes, hours);
    }

   //This formats the time for me.[ref 4]
    private String formatTime(int seconds, int minutes, int hours) {
        return String.format("%02d", hours) + " : " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }



   /// This is from [ref 2]
    // it request the location
    //set to evert 10 seconds if theres been more than 5 meters movement
    @SuppressLint("MissingPermission")
    private void getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 5, MainActivity.this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    ///trying to format
    private String formatSpeedTv(int seconds, int minutes) {
        return String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }

    private String getSpeedinFormat() {
        int rounded = (int) Math.round(split);

        secs = ((rounded % 86400) % 3600) % 60;
        mins = ((rounded % 86400) % 3600) / 60;
       // hours = ((rounded % 86400) / 3600);

        return formatSpeedTv(secs, mins);
    }







    //this is used to run through a Array
    int i = 0;


    //  This is run if the location is changed after 10 seconds.

    @Override
    public void onLocationChanged(@NonNull Location location) {

        if (Pause == false) {

          //gets the gps co-ordinates and stores them in an arraylist.

            GPSLatitude.add(location.getLatitude());
            GPSLongitude.add(location.getLongitude());

           // tvShowStartLoc.setText(location.getLatitude() + "" + location.getLongitude() + "");

            //this is an array that stores the co-ordinates
           // float[] Dist = new float[1];

            //gets the distance between co-ordinates i the arraylist and stores the distance in "Dist"
            if (i >= 1) {
                location.distanceBetween(GPSLatitude.get(i), GPSLongitude.get(i), GPSLatitude.get(i - 1), GPSLongitude.get(i - 1), Dist);

            }
           //so the next time this is run i is one bigger
            i++;

            //leaving a variable equal the distance between the co-ordinates from the arraylist above
            Distance = Dist[0];

            //adds up the distance
            addDistanceUp = addDistanceUp + Distance;

            //getting the time on the clock in seconds
            totalAmountOfSeconds = (hours * 3600) + (minutes * 60) + seconds;


            //getting 500m split in seconds only if distance and time are not = 0

            if (addDistanceUp != 0 & totalAmountOfSeconds != 0) {
                split = (int) (500 / (addDistanceUp / totalAmountOfSeconds));

                //trying to format it to minutes and seconds
                //ref [5]
               /* mins = (split / 60);
                mins = (int) Math.floor(mins);
                secs = split % 60;
                Speed = mins + ":" + secs;
              //  tvSpeed.setText("500m Split:  " + Speed + "");
              */

                tvSpeed.setText(getSpeedinFormat());
                Speed = getSpeedinFormat();
            }

             addDistanceUp = Math.round(addDistanceUp);

            NoDecDistance= (int) addDistanceUp;

             tvDistance.setText(NoDecDistance + "m");


            //ref [1]
            //this is sending the data to the database.
            UserStatsDBModel SendToDb;

            SendToDb = new UserStatsDBModel(sessionID,addDistanceUp,Speed,hours,minutes,seconds) ;

            DatabaseObject dbObject = new DatabaseObject(MainActivity.this);

            dbObject.addOne(SendToDb);

        }
    }
}