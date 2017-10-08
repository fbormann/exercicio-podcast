package br.ufpe.cin.if710.podcast.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import br.ufpe.cin.if710.podcast.R;

public class EpisodeDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_detail);

        TextView feedTitle = (TextView) findViewById(R.id.feed_item_title);
        TextView feedDescription = (TextView) findViewById(R.id.feed_item_description);
        TextView feedDownloadLink = (TextView) findViewById(R.id.feed_item_download_link);
        TextView feedDate = (TextView) findViewById(R.id.feed_item_date);


        feedTitle.setText(getIntent().getStringExtra("feed_title"));
        feedDescription.setText(getIntent().getStringExtra("feed_description"));
        feedDownloadLink.setText(getIntent().getStringExtra("feed_download_link"));
        feedDate.setText(getIntent().getStringExtra("feed_date"));
    }
}
