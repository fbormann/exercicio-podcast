package br.ufpe.cin.if710.podcast.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.if710.podcast.db.PodcastDatabase;
import br.ufpe.cin.if710.podcast.db.dao.PodcastDao;
import br.ufpe.cin.if710.podcast.db.entities.Podcast;
import br.ufpe.cin.if710.podcast.domain.XmlFeedParser;
import br.ufpe.cin.if710.podcast.resources.NotificationReceiver;
import br.ufpe.cin.if710.podcast.ui.adapter.XmlFeedAdapter;

public class RSSPullService extends IntentService {
    private NotificationReceiver receiver;
    private static final String Download_RSS =
            "br.ufpe.cin.if710.podcast.services.action.DOWNLOAD_RSS";
    private static final String Download_EPISODE =
            "br.ufpe.cin.if710.podcast.services.action.EPISODE";
    private static final String Download_RSS_FINISHED =
            "br.ufpe.cin.if710.podcast.services.action.DOWNLOAD_RSS_FINISHED";
    private static final String Download_EPISODE_FINISHED =
            "br.ufpe.cin.if710.podcast.services.action.Download_EPISODE_FINISHED";

    private static final String EXTRA_FEED_LINK =
            "br.ufpe.cin.if710.podcast.services.extra.FeedLink";
    private static final String EXTRA_URL = "br.ufpe.cin.if710.podcast.services.extra.Url";
    private static final String EXTRA_ID = "br.ufpe.cin.if710.podcast.services.extra.EpisodeId";

    private String downloadPath;

    public RSSPullService() {
        super("RSSPullService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Download_EPISODE_FINISHED);
        this.registerReceiver(receiver, filter);
    }

    public static void startActionDownloadRSS(Context context, String FeedLink) {
        Intent intent = new Intent(context, RSSPullService.class);
        intent.setAction(Download_RSS);
        intent.putExtra(EXTRA_FEED_LINK, FeedLink);
        context.startService(intent);
    }

    public static void startActionDownloadEpisode(Context context, String url, String id) {
        Intent intent = new Intent(context, RSSPullService.class);
        intent.setAction(Download_EPISODE);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_ID, id);
        context.startService(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(receiver);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (Download_RSS.equals(action)) {
                final String feedLink = intent.getStringExtra(EXTRA_FEED_LINK);
                handleActionDownloadRSS(feedLink);
            } else if (Download_EPISODE.equals(action)) {
                final String url = intent.getStringExtra(EXTRA_URL);
                final String id = intent.getStringExtra(EXTRA_ID);
                handleActionDownloadEpisode(url, id);
            }
        }
    }

    private void handleActionDownloadRSS(String FeedLink) {
        List<Podcast> itemList = new ArrayList<>();
        try {
            itemList = XmlFeedParser.parse(getRssFeed(FeedLink));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        PodcastDao dao = PodcastDatabase.getPodcastDatabase(getApplicationContext())
                .podcastDao();

        for (int i = 0; i < itemList.size(); i++) {
            Podcast podcast = itemList.get(i);
            dao.insert(podcast);
        }

        Intent localIntent = new Intent(Download_RSS_FINISHED);
        sendBroadcast(localIntent);
    }

    private String getRssFeed(String feed) throws IOException {
        InputStream in = null;
        String rssFeed;
        try {
            URL url = new URL(feed);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, count);
            }
            byte[] response = out.toByteArray();
            rssFeed = new String(response, "UTF-8");
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return rssFeed;
    }

    private void handleActionDownloadEpisode(String episodeUrl, String id) {
        InputStream input = null;
        FileOutputStream output = null;
        HttpURLConnection connection = null;
        //build path to where it will save the file
        String fileName = "podcast_"+id; //concatanate with Id
        try {
            URL url = new URL(episodeUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            }

            // download the file
            input = connection.getInputStream();
            File file = new File(getApplicationContext()
                    .getExternalFilesDir(Environment.DIRECTORY_PODCASTS), fileName);
            output = new FileOutputStream(file);
            downloadPath = file.getPath();

            byte data[] = new byte[8192];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            //nothing
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }

        //this.adapter.updateItem(position, downloadPath);
        Intent localIntent = new Intent(Download_EPISODE_FINISHED);
        localIntent.putExtra("downloadPath", downloadPath);
        localIntent.putExtra("id", id);
        sendBroadcast(localIntent);
    }
}
