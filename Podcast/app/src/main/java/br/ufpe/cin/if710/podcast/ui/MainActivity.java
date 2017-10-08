package br.ufpe.cin.if710.podcast.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.db.PodcastDBHelper;
import br.ufpe.cin.if710.podcast.db.PodcastProvider;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.domain.XmlFeedParser;
import br.ufpe.cin.if710.podcast.services.RSSPullService;
import br.ufpe.cin.if710.podcast.ui.adapter.XmlFeedAdapter;

public class MainActivity extends Activity {

    //ao fazer envio da resolucao, use este link no seu codigo!
    private final String RSS_FEED = "http://leopoldomt.com/if710/fronteirasdaciencia.xml";
    //TODO teste com outros links de podcast

    private ListView items;
    private List<ItemFeed> feedItems;
    private XmlFeedAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        items = (ListView) findViewById(R.id.items);
        feedItems = new ArrayList<>();
        adapter = new XmlFeedAdapter(getApplicationContext(), R.layout.itemlista, feedItems);

        //atualizar o list view
        items.setAdapter(adapter);
        items.setTextFilterEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //new DownloadXmlTask().execute(RSS_FEED);
        ReceiverRSSData receiver = new ReceiverRSSData();
        IntentFilter filter = new IntentFilter("br.ufpe.cin.if710.podcast.services.action.DOWNLOAD_RSS");
        registerReceiver(receiver, filter);
        new RSSPullService().startActionDownloadRSS(getApplicationContext(), RSS_FEED);
    }

    @Override
    protected void onStop() {
        super.onStop();
        XmlFeedAdapter adapter = (XmlFeedAdapter) items.getAdapter();
        adapter.clear();
    }


    private void updateListView(final List<ItemFeed> data) {
        //so it can modify the data even though I'm calling from a BroadCastReceiver
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (ItemFeed u : data) {
                    feedItems.add(u);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    public class ReceiverRSSData extends BroadcastReceiver {

        public ReceiverRSSData() {}

        @Override
        public void onReceive(Context context, Intent intent) {
            ContentResolver resolver = context.getContentResolver();
            Cursor cursor = resolver.query(PodcastProviderContract.EPISODE_LIST_URI, PodcastDBHelper.columns,
                    null, null, null);
            List<ItemFeed> items = readFromCursor(cursor);
            cursor.close();
            updateListView(items);
        }

        private List<ItemFeed> readFromCursor(Cursor cursor) {
            List<ItemFeed> items = new ArrayList<ItemFeed>();
            if (cursor.moveToFirst()) {
                do {
                    ItemFeed item = new ItemFeed(cursor.getString(1), null, cursor.getString(2),
                            cursor.getString(4), null);
                    items.add(item);
                } while (cursor.moveToNext());
            }
            return items;
        }
    }
}
