package br.ufpe.cin.if710.podcast.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import java.io.File;

import br.ufpe.cin.if710.podcast.resources.StatefulMediaPlayer;

public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener {

    StatefulMediaPlayer player = null;
    private static final String ACTION_PLAY = "br.ufpe.cin.if701.podcast.action.PLAY";
    private static final String ACTION_PAUSE = "br.ufpe.cin.if701.podcast.action.PAUSE";
    File file = null;

    public MediaPlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(ACTION_PLAY)) {
            String uri = intent.getStringExtra("file_uri");
            Uri myUri = Uri.parse(uri);

            if (player != null) {
                if (player.getDataSource().equals(myUri)) {
                    player.seekTo(player.getCurrentPosition());
                    player.start();
                } else {
                    try {
                        player.release();
                        player = new StatefulMediaPlayer();
                        player.setAudioAttributes(new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH).build());
                        player.setDataSource(this, myUri);
                        player.setOnPreparedListener(this);
                        player.prepareAsync();
                    } catch (Exception e) {
                        String message = e.getMessage();
                        player.release();
                    }
                }

            } else {

                player = new StatefulMediaPlayer();

                player.setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH).build());
                try {
                    player.setDataSource(this, myUri);
                    player.setOnPreparedListener(this);
                    player.prepareAsync();
                } catch (Exception e) {
                    String message = e.getMessage();
                }
            }
        }

        if (intent.getAction().equals(ACTION_PAUSE)) {
            player.pause();
        }

        return START_NOT_STICKY;
    }

    public void onPrepared(MediaPlayer player) {
        player.start();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
                boolean deleted = file.delete();
            }
        });
    }

    public void setDataUri() {}

    @Override
    public void onDestroy() {
        if (player != null) player.release();
    }


}
