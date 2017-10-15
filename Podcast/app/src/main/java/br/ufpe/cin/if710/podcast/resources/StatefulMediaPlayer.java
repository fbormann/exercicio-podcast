package br.ufpe.cin.if710.podcast.resources;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

public class StatefulMediaPlayer extends MediaPlayer {
    private Uri dataSource;



    @Override
    public void setDataSource(Context context, Uri uri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        super.setDataSource(context, uri);
        this.dataSource = uri;
    }

    public Uri getDataSource() {
        return this.dataSource;
    }
}
