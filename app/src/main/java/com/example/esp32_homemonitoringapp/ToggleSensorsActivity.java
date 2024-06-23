package com.example.esp32_homemonitoringapp;

import static android.app.PendingIntent.getActivity;
import static android.app.Service.START_NOT_STICKY;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ToggleSensorsActivity extends AppCompatActivity {
    Button logoutBtn;
    ToggleButton tglHumidity, tglTemperature, tglFlame, tglGas, tglVibrations, tglMotion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_toggle_sensors);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        logoutBtn = findViewById(R.id.backToggleButton);
        tglHumidity= findViewById(R.id.toggleHumidityButton);
        tglTemperature = findViewById(R.id.toggleTemperatureButton);
        tglFlame = findViewById(R.id.toggleFlameButton);
        tglGas = findViewById(R.id.toggleGasButton);
        tglVibrations = findViewById(R.id.toggleVibrationsButton);
        tglMotion = findViewById(R.id.toggleMotionButton);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://homemonitoringapplogindata-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myToggleGasSensorRef = database.getReference("ToggleButtons/Gas");
        DatabaseReference myToggleHumiditySensorRef = database.getReference("ToggleButtons/Humidity");
        DatabaseReference myToggleTemperatureSensorRef = database.getReference("ToggleButtons/Temperature");
        DatabaseReference myToggleFireSensorRef = database.getReference("ToggleButtons/Flames");
        DatabaseReference myToggleVibrationsSensorRef = database.getReference("ToggleButtons/Vibrations");
        DatabaseReference myToggleMotionSensorRef = database.getReference("ToggleButtons/Motion");



        SharedPreferences sharedHumidityPreferences = getSharedPreferences("saveHumidity",MODE_PRIVATE);

        tglHumidity.setChecked(sharedHumidityPreferences.getBoolean("valueHumidity",true));
        tglHumidity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tglHumidity.isChecked())
                {
                    SharedPreferences.Editor humidityEditor = getSharedPreferences("saveHumidity",MODE_PRIVATE).edit();
                    humidityEditor.putBoolean("valueHumidity",true);
                    humidityEditor.apply();
                    tglHumidity.setChecked(true);
                }
                else
                {
                    SharedPreferences.Editor humidityEditor = getSharedPreferences("saveHumidity",MODE_PRIVATE).edit();
                    humidityEditor.putBoolean("valueHumidity",false);
                    humidityEditor.apply();
                    tglHumidity.setChecked(false);
                }
            }
        });

        SharedPreferences sharedTemperaturePreferences = getSharedPreferences("saveTemperature",MODE_PRIVATE);

        tglTemperature.setChecked(sharedTemperaturePreferences.getBoolean("valueTemperature",true));
        tglTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tglTemperature.isChecked())
                {
                    SharedPreferences.Editor temperatureEditor = getSharedPreferences("saveTemperature",MODE_PRIVATE).edit();
                    temperatureEditor.putBoolean("valueTemperature",true);
                    temperatureEditor.apply();
                    tglTemperature.setChecked(true);
                }
                else
                {
                    SharedPreferences.Editor temperatureEditor = getSharedPreferences("saveTemperature",MODE_PRIVATE).edit();
                    temperatureEditor.putBoolean("valueTemperature",false);
                    temperatureEditor.apply();
                    tglTemperature.setChecked(false);
                }
            }
        });

        SharedPreferences sharedFlamePreferences = getSharedPreferences("saveFlame",MODE_PRIVATE);

       tglFlame.setChecked(sharedFlamePreferences.getBoolean("valueFlame",true));
        tglFlame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tglFlame.isChecked())
                {
                    SharedPreferences.Editor flameEditor = getSharedPreferences("saveFlame",MODE_PRIVATE).edit();
                    flameEditor.putBoolean("valueFlame",true);
                    flameEditor.apply();
                    tglFlame.setChecked(true);
                }
                else
                {
                    SharedPreferences.Editor flameEditor = getSharedPreferences("saveFlame",MODE_PRIVATE).edit();
                    flameEditor.putBoolean("valueFlame",false);
                    flameEditor.apply();
                    tglFlame.setChecked(false);
                }
            }
        });

        SharedPreferences sharedGasPreferences = getSharedPreferences("saveGas",MODE_PRIVATE);

        tglGas.setChecked(sharedGasPreferences.getBoolean("valueGas",true));
        tglGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tglGas.isChecked())
                {
                    SharedPreferences.Editor gasEditor = getSharedPreferences("saveGas",MODE_PRIVATE).edit();
                    gasEditor.putBoolean("valueGas",true);
                    gasEditor.apply();
                    tglGas.setChecked(true);
                }
                else
                {
                    SharedPreferences.Editor gasEditor = getSharedPreferences("saveGas",MODE_PRIVATE).edit();
                    gasEditor.putBoolean("valueGas",false);
                    gasEditor.apply();
                    tglGas.setChecked(false);
                }
            }
        });

        SharedPreferences sharedVibrationPreferences = getSharedPreferences("saveVibrations",MODE_PRIVATE);

        tglVibrations.setChecked(sharedVibrationPreferences.getBoolean("valueVibrations",true));
        tglVibrations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tglVibrations.isChecked())
                {
                    SharedPreferences.Editor vibrationEditor = getSharedPreferences("saveVibrations",MODE_PRIVATE).edit();
                    vibrationEditor.putBoolean("valueVibrations",true);
                    vibrationEditor.apply();
                    tglVibrations.setChecked(true);
                }
                else
                {
                    SharedPreferences.Editor vibrationEditor = getSharedPreferences("saveVibrations",MODE_PRIVATE).edit();
                    vibrationEditor.putBoolean("valueVibrations",false);
                    vibrationEditor.apply();
                    tglVibrations.setChecked(false);
                }
            }
        });

        SharedPreferences sharedMotionPreferences = getSharedPreferences("saveMotion",MODE_PRIVATE);

        tglMotion.setChecked(sharedMotionPreferences.getBoolean("valueMotion",true));
        tglMotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tglMotion.isChecked())
                {
                    SharedPreferences.Editor motionEditor = getSharedPreferences("saveMotion",MODE_PRIVATE).edit();
                    motionEditor.putBoolean("valueMotion",true);
                    motionEditor.apply();
                    tglMotion.setChecked(true);
                }
                else
                {
                    SharedPreferences.Editor motionEditor = getSharedPreferences("saveMotion",MODE_PRIVATE).edit();
                    motionEditor.putBoolean("valueMotion",false);
                    motionEditor.apply();
                    tglMotion.setChecked(false);
                }
            }
        });


        tglHumidity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!tglHumidity.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Humidity Sensor Enabled", Toast.LENGTH_SHORT).show();
                    myToggleHumiditySensorRef.setValue(true);

                } else {
                    Toast.makeText(getApplicationContext(), "Humidity Sensor Disabled", Toast.LENGTH_SHORT).show();
                    myToggleHumiditySensorRef.setValue(false);

                }
            }
        });

        tglTemperature.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!tglTemperature.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Temperature Sensor Enabled", Toast.LENGTH_SHORT).show();
                    myToggleTemperatureSensorRef.setValue(true);

                } else {
                    Toast.makeText(getApplicationContext(), "Temperature Sensor Disabled", Toast.LENGTH_SHORT).show();
                    myToggleTemperatureSensorRef.setValue(false);

                }
            }
        });

        tglFlame.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!tglFlame.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Flame Sensor Enabled", Toast.LENGTH_SHORT).show();
                    myToggleFireSensorRef.setValue(true);
                } else {
                    Toast.makeText(getApplicationContext(), "Flame Sensor Disabled", Toast.LENGTH_SHORT).show();
                    myToggleFireSensorRef.setValue(false);
                }
            }
        });

        tglGas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!tglGas.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Gas Sensor Enabled", Toast.LENGTH_SHORT).show();
                    myToggleGasSensorRef.setValue(true);
                } else {
                    Toast.makeText(getApplicationContext(), "Gas Sensor Disabled", Toast.LENGTH_SHORT).show();
                    myToggleGasSensorRef.setValue(false);
                }
            }
        });

        tglVibrations.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!tglVibrations.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Vibration Sensor Enabled", Toast.LENGTH_SHORT).show();
                    myToggleVibrationsSensorRef.setValue(true);
                } else {
                    Toast.makeText(getApplicationContext(), "Vibration Sensor Disabled", Toast.LENGTH_SHORT).show();
                    myToggleVibrationsSensorRef.setValue(false);
                }
            }
        });

        tglMotion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!tglMotion.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Motion Sensor Enabled", Toast.LENGTH_SHORT).show();
                    myToggleMotionSensorRef.setValue(true);
                } else {
                    Toast.makeText(getApplicationContext(), "Motion Sensor Disabled", Toast.LENGTH_SHORT).show();
                    myToggleMotionSensorRef.setValue(false);
                }
            }
        });



        logoutBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(ToggleSensorsActivity.this, MainActivity.class));
        }
    });
}




}