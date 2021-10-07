package com.example.appmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ImageView btnplay;
    private ImageView btnpause;
    private ServiceConnection serviceConnection;
    private boolean isConnected;
    private MusicPlayerService musicPlayerService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectService();
        btnplay = findViewById(R.id.imagePlay);
        btnpause = findViewById(R.id.imagePause);
        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isConnected){
                    return;
                }
                boolean isplay = musicPlayerService.play();
                Toast.makeText(musicPlayerService, isplay ? "Playing music" : "Pause Music", Toast.LENGTH_SHORT).show();

            }
        });
        btnpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isConnected) {
                    return;
                }
                boolean ispause = musicPlayerService.pause();
                Toast.makeText(musicPlayerService, ispause ? "Pause Music" : "Playing Music", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void connectService() {
        Intent intent = new Intent(this, MusicPlayerService.class);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MusicPlayerService.MyServiceBinder myBinder = (MusicPlayerService.MyServiceBinder) service;

                musicPlayerService = myBinder.getService();
                isConnected = true;
                Toast.makeText(musicPlayerService, "Connected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isConnected = false;
                Toast.makeText(musicPlayerService, "Disconnected", Toast.LENGTH_SHORT).show();
                musicPlayerService = null;

            }
        };
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }
}