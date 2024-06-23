package com.example.esp32_homemonitoringapp;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SensorValuesActivity extends AppCompatActivity {

    Button logoutBtn;
    TextView gas,humidity,temperature,fire,vibrations,motion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sensor_values);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Notification

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        {
            if(ContextCompat.checkSelfPermission(SensorValuesActivity.this, Manifest.permission.POST_NOTIFICATIONS)!= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(SensorValuesActivity.this,new String[]{Manifest.permission.POST_NOTIFICATIONS},101);
            }
        }



        logoutBtn = findViewById(R.id.backButton);
        gas = findViewById(R.id.gasSensor);
        humidity = findViewById(R.id.humiditySensor);
        temperature = findViewById(R.id.temperatureSensor);
        fire = findViewById(R.id.fireSensor);
        vibrations = findViewById(R.id.vibrationSensor);
        motion = findViewById(R.id.motionSensor);


        FirebaseDatabase database = FirebaseDatabase.getInstance("https://homemonitoringapplogindata-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myGasSensorRef = database.getReference("GasSensor/ppm");
        DatabaseReference myHumiditySensorRef = database.getReference("DHT11_Data/Humidity");
        DatabaseReference myTemperatureSensorRef = database.getReference("DHT11_Data/Temperature");
        DatabaseReference myFireSensorRef = database.getReference("FlacaraSensor/Flacara");
        DatabaseReference myVibrationsSensorRef = database.getReference("VibrationSensor/Vibrations");
        DatabaseReference myMotionSensorRef = database.getReference("MotionSensor/Motion");


        DatabaseReference myToggleGasSensorRef = database.getReference("ToggleButtons/Gas");
        DatabaseReference myToggleHumiditySensorRef = database.getReference("ToggleButtons/Humidity");
        DatabaseReference myToggleTemperatureSensorRef = database.getReference("ToggleButtons/Temperature");
        DatabaseReference myToggleFireSensorRef = database.getReference("ToggleButtons/Flames");
        DatabaseReference myToggleVibrationsSensorRef = database.getReference("ToggleButtons/Vibrations");
        DatabaseReference myToggleMotionSensorRef = database.getReference("ToggleButtons/Motion");




        ///////////////////////////////////////////////////////
        myToggleGasSensorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue().toString();
                if (value.equals("true")) {
                    myGasSensorRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Acest cod este apelat o data cu valoarea initiala si apoi de cate ori se actualizeaza datele
                            String gasSensorValue  = dataSnapshot.getValue().toString();
                            gas.setText(gasSensorValue );

                            double gasValue = Double.parseDouble(gasSensorValue);
                            if (gasValue >= 500 && gasValue <= 550) {
                                // Verifica daca myToggleGasSensorRef este true inainte de a apela makeGasNotification()
                                myToggleGasSensorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot toggleSnapshot) {
                                        boolean toggleValue = Boolean.parseBoolean(toggleSnapshot.getValue().toString());
                                        if (toggleValue) {
                                            makeGasNotification();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // A esuat citirea valorii
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // A esuat citirea valorii
                        }
                    });
                } else if (value.equals("false")) {
                    gas.setText("Disabled!");
                    gas.setTextColor(Color.RED);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // A esuat citirea valorii
            }
        });


        myToggleHumiditySensorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue().toString();
                if (value.equals("true")) {
                    myHumiditySensorRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String humiditySensorValue  = dataSnapshot.getValue().toString();
                            humidity.setText(humiditySensorValue );

                            double humidityValue = Double.parseDouble(humiditySensorValue);
                            if (humidityValue >= 90 && humidityValue <= 90.5) {
                                myToggleHumiditySensorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot toggleSnapshot) {
                                        boolean toggleValue = Boolean.parseBoolean(toggleSnapshot.getValue().toString());
                                        if (toggleValue) {
                                            makeHumidityNotification();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // A esuat citirea valorii
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // A esuat citirea valorii
                        }
                    });
                } else if (value.equals("false")) {
                    humidity.setText("Disabled!");
                    humidity.setTextColor(Color.RED);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // A esuat citirea valorii
            }
        });


        myToggleTemperatureSensorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue().toString();
                if (value.equals("true")) {
                    myTemperatureSensorRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String temperatureSensorValue = dataSnapshot.getValue().toString();
                            temperature.setText(temperatureSensorValue);

                            double tempValue = Double.parseDouble(temperatureSensorValue);
                            if (tempValue >= 50 && tempValue <= 50.5) {
                                myToggleTemperatureSensorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot toggleSnapshot) {
                                        boolean toggleValue = Boolean.parseBoolean(toggleSnapshot.getValue().toString());
                                        if (toggleValue) {
                                            makeTemperatureNotification();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // A esuat citirea valorii
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // A esuat citirea valorii
                        }
                    });
                } else if (value.equals("false")) {
                    temperature.setText("Disabled!");
                    temperature.setTextColor(Color.RED);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // A esuat citirea valorii
            }
        });


        myToggleFireSensorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue().toString();
                if(value.equals("true"))
                {
                    myFireSensorRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String fireSensorValue  = dataSnapshot.getValue().toString();
                            if(fireSensorValue.equals("0"))
                            {
                                myToggleFireSensorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot toggleSnapshot) {
                                        boolean toggleValue = Boolean.parseBoolean(toggleSnapshot.getValue().toString());
                                        if (toggleValue) {
                                            makeFlamesNotification();
                                            fire.setText("Detected !");
                                        } else {
                                            fire.setText("Not Detected !");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // A esuat citirea valorii
                                    }
                                });
                            } else {
                                fire.setText("Not Detected !");
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Failed to read value
                        }
                    });
                }
                if(value.equals("false"))
                {
                    fire.setText("Disabled!");
                    fire.setTextColor(Color.RED);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myToggleVibrationsSensorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue().toString();
                if(value.equals("true"))
                {
                    myVibrationsSensorRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String vibrationSensorValue = dataSnapshot.getValue().toString();
                            if(vibrationSensorValue.equals("1"))
                            {
                                myToggleVibrationsSensorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot toggleSnapshot) {
                                        boolean toggleValue = Boolean.parseBoolean(toggleSnapshot.getValue().toString());
                                        if (toggleValue) {
                                            makeVibrationsNotification();
                                            vibrations.setText("Detected !");
                                        } else {
                                            vibrations.setText("Not Detected !");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // A esuat citirea valorii
                                    }
                                });
                            } else {
                                vibrations.setText("Not Detected !");
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Failed to read value
                        }
                    });
                }
                if(value.equals("false"))
                {
                    vibrations.setText("Disabled!");
                    vibrations.setTextColor(Color.RED);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        myToggleMotionSensorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue().toString();
                if(value.equals("true"))
                {
                    myMotionSensorRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String motionSensorValue = dataSnapshot.getValue().toString();
                            if(motionSensorValue.equals("1"))
                            {
                                myToggleMotionSensorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot toggleSnapshot) {
                                        boolean toggleValue = Boolean.parseBoolean(toggleSnapshot.getValue().toString());
                                        if (toggleValue) {
                                            makeMotionNotification();
                                            motion.setText("Detected !");
                                        } else {
                                            motion.setText("Not Detected !");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // A esuat citirea valorii
                                    }
                                });
                            } else {
                                motion.setText("Not Detected !");
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Failed to read value
                        }
                    });
                }
                if(value.equals("false"))
                {
                    motion.setText("Disabled!");
                    motion.setTextColor(Color.RED);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SensorValuesActivity.this, MainActivity.class));
            }
        });
    }


    public void makeMotionNotification()
    {
        String chanelID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),chanelID);
        builder.setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("MOTION DETECTED !")
                .setContentText("Motion sensor detected movement, check notification timestamp !")
                .setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Intent intent = new Intent(getApplicationContext(),SensorValuesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(chanelID);
            if(notificationChannel == null)
            {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(chanelID,"Motion Description",importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        notificationManager.notify(0,builder.build());
    }


    public void makeVibrationsNotification()
    {
        String chanelID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),chanelID);
        builder.setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("VIBRATIONS DETECTED !")
                .setContentText("Vibration sensor detected vibrations, check notification timestamp !")
                .setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Intent intent = new Intent(getApplicationContext(),SensorValuesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(chanelID);
            if(notificationChannel == null)
            {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(chanelID,"Vibrations Description",importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        notificationManager.notify(0,builder.build());
    }

    public void makeFlamesNotification()
    {
        String chanelID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),chanelID);
        builder.setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("FIRE DETECTED !")
                .setContentText("Flame sensor detected fire, check notification timestamp !")
                .setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Intent intent = new Intent(getApplicationContext(),SensorValuesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(chanelID);
            if(notificationChannel == null)
            {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(chanelID,"Flames Description",importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        notificationManager.notify(0,builder.build());
    }


    public void makeTemperatureNotification()
    {
        String chanelID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),chanelID);
        builder.setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("HIGH TEMPERATURE DETECTED !")
                .setContentText("DHT11 sensor detected high temperature level, check notification timestamp !")
                .setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Intent intent = new Intent(getApplicationContext(),SensorValuesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(chanelID);
            if(notificationChannel == null)
            {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(chanelID,"Temperature Description",importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        notificationManager.notify(0,builder.build());
    }


    public void makeHumidityNotification()
    {
        String chanelID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),chanelID);
        builder.setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("HIGH HUMIDITY DETECTED !")
                .setContentText("DHT11 sensor detected high humidity level, check notification timestamp !")
                .setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Intent intent = new Intent(getApplicationContext(),SensorValuesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(chanelID);
            if(notificationChannel == null)
            {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(chanelID,"Humidity Description",importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        notificationManager.notify(0,builder.build());
    }


    public void makeGasNotification()
    {
        String chanelID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),chanelID);
        builder.setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("HIGH GAS PPM LEVEL DETECTED !")
                .setContentText("Gas sensor detected high gas ppm level, check notification timestamp !")
                .setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Intent intent = new Intent(getApplicationContext(),SensorValuesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(chanelID);
            if(notificationChannel == null)
            {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(chanelID,"Gas Description",importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        notificationManager.notify(0,builder.build());
    }

}



