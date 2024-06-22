package com.example.esp32_homemonitoringapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SensorInformationActivity extends AppCompatActivity {
    Button logoutBtn,temperatureInfoBtn,gasInfoBtn,flameInfoBtn,motionInfoBtn,vibrationInfoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sensor_information);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        logoutBtn = findViewById(R.id.backButton2);
        temperatureInfoBtn = findViewById(R.id.temperatureInfoButton);
        gasInfoBtn = findViewById(R.id.gasInfoButton);
        flameInfoBtn = findViewById(R.id.flameInfoButton);
        motionInfoBtn = findViewById(R.id.motionInfoButton);
        vibrationInfoBtn = findViewById(R.id.vibrationInfoButton);

        temperatureInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SensorInformationActivity.this, TemperatureHumiditySensorInfoActivity.class));
            }
        });


        gasInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SensorInformationActivity.this, GasSensorInfoActivity.class));
            }
        });

        flameInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SensorInformationActivity.this, FlameSensorInfoActivity.class));
            }
        });

        vibrationInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SensorInformationActivity.this, VibrationSensorInfoActivity.class));
            }
        });

        motionInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SensorInformationActivity.this, MotionSensorInfoActivity.class));
            }
        });

        temperatureInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SensorInformationActivity.this, TemperatureHumiditySensorInfoActivity.class));
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SensorInformationActivity.this, MainActivity.class));
            }
        });
    }
}