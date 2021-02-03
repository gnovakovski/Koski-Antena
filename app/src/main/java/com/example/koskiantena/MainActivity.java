package com.example.koskiantena;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    ImageView imgStatic, imgTv;
    TextView txtSignal;
    private SensorManager sensorManager;
    public static DecimalFormat DECIMAL_FORMATTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        imgStatic = findViewById(R.id.imgStatic);
        imgTv = findViewById(R.id.imgTv);
        txtSignal = findViewById(R.id.txtSignal);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DECIMAL_FORMATTER = new DecimalFormat("#.000", symbols);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        getSystemService(SENSOR_SERVICE);
        getSupportActionBar().hide();

        Glide.with(this)
                .load(R.drawable.pow)
                .asGif()
                .into(imgTv);
        Glide.with(this)
                .load(R.drawable.st) // aqui é teu gif
                .asGif()
                .into(imgStatic);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            float magX = event.values[0];
            float magY = event.values[1];
            float magZ = event.values[2];
            double magnitude = Math.sqrt((magX * magX) + (magY * magY) + (magZ * magZ));
            int min = 1;
            int max = 100;
            int minM = 0;
            int maxM = 150;
            double total = (magnitude - min)/max;
            Log.d("magnitude", magnitude+ "");
            Log.d("total", 1/total+"");
            Log.d("tv", (magnitude - minM)/maxM+"");
            txtSignal.setText(DECIMAL_FORMATTER.format(1/total));
            imgStatic.setAlpha(Float.parseFloat(Double.toString(1/total)));
            imgTv.setAlpha(Float.parseFloat(Double.toString(((magnitude - minM)/maxM))));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}