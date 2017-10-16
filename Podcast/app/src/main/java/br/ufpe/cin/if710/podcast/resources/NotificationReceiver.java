package br.ufpe.cin.if710.podcast.resources;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String Download_EPISODE_FINISHED =
            "br.ufpe.cin.if710.podcast.services.action.Download_EPISODE_FINISHED";

    public NotificationReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Download_EPISODE_FINISHED)) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle("download acabou")
                    .setContentText("download do podcast acabou")
                    .setSmallIcon(android.R.drawable.arrow_up_float);
            int mNotificationId = 001;

            NotificationManagerCompat mNotifyManager = NotificationManagerCompat.from(context);
            mNotifyManager.notify(mNotificationId, mBuilder.build());
        }
    }
}
