package com.example.weatherstation;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    TextView tv_pressure, tv_light, tv_temp;
    SensorManager sensorManager;
    Sensor light, pressure, temp;
    float currentLight, currentPressure, currentTemp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        tv_pressure = findViewById( R.id.tv_pressure );
        tv_light = findViewById( R.id.tv_light );
        tv_temp = findViewById( R.id.tv_tempreture );

        sensorManager = (SensorManager) getSystemService( SENSOR_SERVICE );


        Timer upDateTimer = new Timer( "weather upDate" );
        upDateTimer.scheduleAtFixedRate( new TimerTask() {
            @Override
            public void run() {
                upDateGui();

            }
        }, 0, 1000 );

    }

    private void upDateGui() {
        runOnUiThread( new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                if (currentPressure != Float.NaN) {
                    tv_pressure.setText( currentPressure + " hpa" );
                    tv_pressure.invalidate();
                }
                if (currentLight != Float.NaN) {
                    String str = "sunny";
                    if (currentLight <= SensorManager.LIGHT_CLOUDY) {
                        str = "night";

                    } else if (currentLight <= SensorManager.LIGHT_OVERCAST) {
                        str = "cloudy";
                    }else if(currentLight<=SensorManager.LIGHT_SUNLIGHT){
                        str="overCast";
                    }
                    tv_light.setText( str );
                    tv_light.invalidate();

                }
                if (currentTemp != Float.NaN) {
                    tv_temp.setText( currentTemp + " C" );
                    tv_temp.invalidate();
                }

            }
        } );
    }

    final SensorEventListener lightEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            currentLight = event.values[0];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


    final SensorEventListener pressureEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            currentPressure = event.values[0];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


    final SensorEventListener tempEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            currentTemp = event.values[0];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onResume() {
        light = sensorManager.getDefaultSensor( Sensor.TYPE_LIGHT );
        if (light != null) {
            sensorManager.registerListener( lightEventListener, light, SensorManager.SENSOR_DELAY_NORMAL );
        } else {
            tv_light.setText( "light sensor not found" );
        }

        pressure = sensorManager.getDefaultSensor( Sensor.TYPE_PRESSURE );
        if (pressure != null) {
            sensorManager.registerListener( pressureEventListener, pressure, SensorManager.SENSOR_DELAY_NORMAL );
        } else {
            tv_pressure.setText( "pressure sensor not found" );
        }

        temp = sensorManager.getDefaultSensor( Sensor.TYPE_AMBIENT_TEMPERATURE );
        if (temp != null) {
            sensorManager.registerListener( tempEventListener, temp, SensorManager.SENSOR_DELAY_NORMAL );
        } else {
            tv_temp.setText( "tempereture sensor is not found" );
        }

        super.onResume();

    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener( lightEventListener, light );
        sensorManager.unregisterListener( tempEventListener, temp );
        sensorManager.unregisterListener( pressureEventListener, pressure );

        super.onPause();
    }

}