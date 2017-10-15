package br.ufpe.cin.if710.podcast.services;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import java.io.File;

public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener {

    MediaPlayer player = null;
    private static final String ACTION_PLAY = "br.ufpe.cin.if701.podcast.action.PLAY";

    public MediaPlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(ACTION_PLAY)) {
            player = new MediaPlayer();
            player.setAudioAttributes(new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH).build());
            String uri = intent.getStringExtra("file_uri");
            Uri myUri = Uri.parse(uri);
            try {
                player.setDataSource(this, myUri);
                player.setOnPreparedListener(this);
                player.prepareAsync();
            } catch (Exception e) {
                //none;
            }

        }

        return START_NOT_STICKY;
    }

    public void onPrepared(MediaPlayer player) {
        player.start();
    }

    @Override
    public void onDestroy() {
        if (player != null) player.release();
    }
}
