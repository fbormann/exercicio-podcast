package br.ufpe.cin.if710.podcast.services;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.if710.podcast.db.PodcastDBHelper;
import br.ufpe.cin.if710.podcast.db.PodcastProvider;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.domain.XmlFeedParser;

public class RSSPullService extends IntentService {
    private static final String Download_RSS = "br.ufpe.cin.if710.podcast.services.action.DOWNLOAD_RSS";
    private static final String Download_RSS_ACTION = "br.ufpe.cin.if710.podcast.services.action.BAZ";

    private static final String EXTRA_FEED_LINK = "br.ufpe.cin.if710.podcast.services.extra.FeedLink";
    private static final String EXTRA_PARAM2 = "br.ufpe.cin.if710.podcast.services.extra.PARAM2";

    public RSSPullService() {
        super("RSSPullService");
    }

    public static void startActionDownloadRSS(Context context, String FeedLink) {
        Intent intent = new Intent(context, RSSPullService.class);
        intent.setAction(Download_RSS);
        intent.putExtra(EXTRA_FEED_LINK, FeedLink);
        context.startService(intent);
    }

    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, RSSPullService.class);
        intent.setAction(Download_RSS_ACTION);
        intent.putExtra(EXTRA_FEED_LINK, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (Download_RSS.equals(action)) {
                final String feedLink = intent.getStringExtra(EXTRA_FEED_LINK);
                handleActionDownloadRSS(feedLink);
            } else if (Download_RSS_ACTION.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_FEED_LINK);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    private void handleActionDownloadRSS(String FeedLink) {
        List<ItemFeed> itemList = new ArrayList<>();
        try {
            itemList = XmlFeedParser.parse(getRssFeed(FeedLink));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        ContentResolver resolver = getContentResolver();
        for (int i = 0; i < itemList.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(PodcastDBHelper.columns[1], itemList.get(i).getTitle());
            values.put(PodcastDBHelper.columns[2], itemList.get(i).getPubDate());
            values.put(PodcastDBHelper.columns[3], itemList.get(i).getLink());
            values.put(PodcastDBHelper.columns[4], itemList.get(i).getDescription());
            values.put(PodcastDBHelper.columns[5], itemList.get(i).getDownloadLink());
            values.put(PodcastDBHelper.columns[6], "");
            if (resolver.update(PodcastProviderContract.EPISODE_LIST_URI, values, null, null) != 0) {
                resolver.insert(PodcastProviderContract.EPISODE_LIST_URI, values);
            }
        }

        Intent localIntent = new Intent(Download_RSS);
        sendBroadcast(localIntent);
    }

    private String getRssFeed(String feed) throws IOException {
        InputStream in = null;
        String rssFeed = "";
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

    private void handleActionBaz(String param1, String param2) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
