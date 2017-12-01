package br.ufpe.cin.if710.podcast.ui;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.db.entities.Podcast;
import br.ufpe.cin.if710.podcast.db.viewmodels.ListPodcastViewModel;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.services.RSSPullService;
import br.ufpe.cin.if710.podcast.ui.adapter.XmlFeedAdapter;

public class MainActivity extends AppCompatActivity {

    //ao fazer envio da resolucao, use este link no seu codigo!
    private final String RSS_FEED = "http://leopoldomt.com/if710/fronteirasdaciencia.xml";
    private ReceiverRSSData receiver;
    private ListView items;
    private List<Podcast> podcasts;
    private XmlFeedAdapter adapter;
    private static final String Download_RSS_FINISHED =
            "br.ufpe.cin.if710.podcast.services.action.DOWNLOAD_RSS_FINISHED";
    private static final String Download_EPISODE_FINISHED =
            "br.ufpe.cin.if710.podcast.services.action.Download_EPISODE_FINISHED";
    private static final String Download_RSS = "br.ufpe.cin.if710.podcast.services.action.DOWNLOAD_RSS";
    public static boolean started = false;
    public static boolean paused = false;
    public static String mediaPlayerState = "null";
    private ListPodcastViewModel mModel;
    private LifecycleRegistry mLifecycleRegistry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);
        setTheme(R.style.Theme_AppCompat_DayNight);
        setContentView(R.layout.activity_main);
        items = findViewById(R.id.items);
        podcasts = new ArrayList<>();
        adapter = new XmlFeedAdapter(getApplicationContext(), R.layout.itemlista, podcasts);

        //Room
        mModel = ViewModelProviders.of(this).get(ListPodcastViewModel.class);

        mModel.getPodcasts(getApplication()).observe(this, new Observer<List<Podcast>>() {
            @Override
            public void onChanged(@Nullable List<Podcast> podcasts) {
                if (podcasts != null) {
                    adapter.clear();
                    adapter.addAll(podcasts);
                }
            }
        });

        //atualizar o list view
        items.setAdapter(adapter);
        items.setTextFilterEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mLifecycleRegistry.markState(Lifecycle.State.RESUMED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    @NonNull
    public Lifecycle getLifecycle() {
        return this.mLifecycleRegistry;
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

        mLifecycleRegistry.markState(Lifecycle.State.STARTED);

        receiver = new ReceiverRSSData();
        IntentFilter filter = new IntentFilter("br.ufpe.cin.if710.podcast.services.action.DOWNLOAD_RSS");
        filter.addAction(Download_EPISODE_FINISHED);
        filter.addAction(Download_RSS_FINISHED);
        registerReceiver(receiver, filter);

        if (!started) {
            //check if there is internet connection available
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                new RSSPullService().startActionDownloadRSS(getApplicationContext(), RSS_FEED);
            } else {
                //if there isn't, just download the data from database
                Intent localIntent = new Intent(Download_RSS);
                sendBroadcast(localIntent);
            }
        } else {
            //if there isn't, just download the data from database
            Intent localIntent = new Intent(Download_RSS_FINISHED);
            sendBroadcast(localIntent);
        }

        started = true;
        paused = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        XmlFeedAdapter adapter = (XmlFeedAdapter) items.getAdapter();
        adapter.clear();
        unregisterReceiver(receiver);
    }

    @Override
    protected  void onPause() {
        super.onPause();
        paused = true;
    }

    public class ReceiverRSSData extends BroadcastReceiver {

        public ReceiverRSSData() {}

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Download_RSS_FINISHED)) {
                List<Podcast> podcasts = mModel.getPodcasts(getApplication()).getValue();
            }

            if (intent.getAction().equals(Download_EPISODE_FINISHED)) {
                if (!paused) {
                    String downloadPath = intent.getStringExtra("downloadPath");
                    String id = intent.getStringExtra("id");

                    adapter.updateItem(Integer.parseInt(id), downloadPath);
                    //TODO: update downloadPath based on id
                }
                //create notification push that download is finished
            }
        }

        //TODO: modify to read data from a list of items
        private List<ItemFeed> readFromCursor(Cursor cursor) {
            List<ItemFeed> items = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    ItemFeed item = new ItemFeed(cursor.getString(1), cursor.getString(3), cursor.getString(2),
                            cursor.getString(4), cursor.getString(5));
                    item.setId(cursor.getInt(0));
                    item.setFileUri(cursor.getString(6));
                    items.add(item);
                } while (cursor.moveToNext());
            }
            return items;
        }
    }
}
