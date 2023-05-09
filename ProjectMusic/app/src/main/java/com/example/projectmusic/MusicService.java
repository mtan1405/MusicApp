package com.example.projectmusic;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.example.projectmusic.PlayListManager.PlayListManager;

public class MusicService extends Service {
    MediaPlayer mediaPlayer ;
    PlayListManager playListManager ;
    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setup();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {

        super.onCreate();
    }
    void setup () {

        if (playListManager == null) {
            playListManager = PlayListManager.getPlayListManager();
        }
        if (playListManager.getMediaPlayer() == null ) {
            mediaPlayer = new MediaPlayer() ;
            playListManager.setMediaPlayer(mediaPlayer);
            playListManager.setupMediaPlayer();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}