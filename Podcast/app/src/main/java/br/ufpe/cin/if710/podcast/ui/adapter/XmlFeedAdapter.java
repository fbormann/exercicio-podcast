package br.ufpe.cin.if710.podcast.ui.adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.db.PodcastDBHelper;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.services.MediaPlayerService;
import br.ufpe.cin.if710.podcast.services.RSSPullService;
import br.ufpe.cin.if710.podcast.ui.EpisodeDetailActivity;
import br.ufpe.cin.if710.podcast.ui.MainActivity;

public class XmlFeedAdapter extends ArrayAdapter<ItemFeed> {

    int linkResource;
    private Context context;
    private static final String ACTION_PLAY = "br.ufpe.cin.if701.podcast.action.PLAY";
    private static final String ACTION_PAUSE = "br.ufpe.cin.if701.podcast.action.PAUSE";

    public XmlFeedAdapter(Context context, int resource, List<ItemFeed> objects) {
        super(context, resource, objects);
        linkResource = resource;
        this.context = context;
    }

    public void updateItem(int position, String fileUri) {
        getItem(position).setFileUri(fileUri);
        notifyDataSetChanged();
    }

    public XmlFeedAdapter getReference() {
        return this;
    }
    /**
     * public abstract View getView (int position, View convertView, ViewGroup parent)
     * <p>
     * Added in API level 1
     * Get a View that displays the data at the specified position in the data set. You can either create a View manually or inflate it from an XML layout file. When the View is inflated, the parent View (GridView, ListView...) will apply default layout parameters unless you use inflate(int, android.view.ViewGroup, boolean) to specify a root view and to prevent attachment to the root.
     * <p>
     * Parameters
     * position	The position of the item within the adapter's data set of the item whose view we want.
     * convertView	The old view to reuse, if possible. Note: You should check that this view is non-null and of an appropriate type before using. If it is not possible to convert this view to display the correct data, this method can create a new view. Heterogeneous lists can specify their number of view types, so that this View is always of the right type (see getViewTypeCount() and getItemViewType(int)).
     * parent	The parent that this view will eventually be attached to
     * Returns
     * A View corresponding to the data at the specified position.
     */


	/*
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.itemlista, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.item_title);
		textView.setText(items.get(position).getTitle());
	    return rowView;
	}
	/**/

    //http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
    static class ViewHolder {
        TextView item_title;
        TextView item_date;
        Button actionButton;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), linkResource, null);
            holder = new ViewHolder();
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            holder.item_date = (TextView) convertView.findViewById(R.id.item_date);
            holder.actionButton = (Button) convertView.findViewById(R.id.item_action);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item_title.setText(getItem(position).getTitle());
        holder.item_date.setText(getItem(position).getPubDate());

        holder.item_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openDetails = new Intent(context, EpisodeDetailActivity.class);
                openDetails.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                openDetails.putExtra("feed_title", getItem(position).getTitle());
                openDetails.putExtra("feed_date", getItem(position).getPubDate());
                openDetails.putExtra("feed_download_link", getItem(position).getDownloadLink());
                openDetails.putExtra("feed_description", getItem(position).getDescription());
                context.startActivity(openDetails);
            }
        });

        if (!getItem(position).getFileUri().equals("")) {
            holder.actionButton.setText("tocar");
        }

        holder.actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button btn = (Button) view;
                Intent intent;
                switch (btn.getText().toString()) {
                    case "baixar":
                        /*new DownloadEpisodeTask(getReference(), position)
                                .execute(getItem(position).getDownloadLink(),
                                        String.valueOf(getItem(position).getId()));*/
                        new RSSPullService()
                                .startActionDownloadEpisode(context, getItem(position).getDownloadLink(),
                                        String.valueOf(getItem(position).getId()));
                        btn.setText("tocar");
                        notifyDataSetChanged();
                        break;

                    case "tocar":
                        btn.setText("parar");

                        intent = new Intent(context, MediaPlayerService.class);
                        intent.setAction(ACTION_PLAY);
                        intent.putExtra("file_uri", getItem(position).getFileUri());
                        if (MainActivity.mediaPlayerState == "null") {

                        }
                        context.startService(intent);
                        break;

                    case "parar":
                        btn.setText("tocar");
                        intent = new Intent(context, MediaPlayerService.class);
                        intent.setAction(ACTION_PAUSE);
                        context.startService(intent);
                        break;
                }
            }
        });
        return convertView;
    }



    private class DownloadInfo {
        public String downloadPath;
        public int id;
        public String errorMessage;

        public DownloadInfo(String downloadPath, int id) {
            this.downloadPath = downloadPath;
            this.id = id;
        }

        public DownloadInfo(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    private class DownloadEpisodeTask extends AsyncTask<String, Integer, DownloadInfo> {

        private XmlFeedAdapter adapter;
        private int position;

        public DownloadEpisodeTask(XmlFeedAdapter adapter, int position) {
            this.adapter = adapter;
            this.position = position;
        }

        @Override
        protected DownloadInfo doInBackground(String... objects) {
            InputStream input = null;
            FileOutputStream output = null;
            HttpURLConnection connection = null;
            //build path to where it will save the file
            String fileName = "podcast_"+objects[1]; //concatanate with Id
            String path = "";
            try {
                URL url = new URL(objects[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return new DownloadInfo("Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage());
                }

                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PODCASTS), fileName);
                output = new FileOutputStream(file);
                path = file.getPath();
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return new DownloadInfo(e.toString());
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
            return new DownloadInfo(path, Integer.parseInt(objects[1]));
        }

        protected void onPostExecute(DownloadInfo result) {
            super.onPostExecute(result);
            ContentResolver resolver = context.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(PodcastDBHelper.columns[0], result.id);
            values.put(PodcastDBHelper.columns[6], result.downloadPath);
            int modified = resolver.update(PodcastProviderContract.EPISODE_LIST_URI, values,
                    PodcastDBHelper._ID+"=?", new String[]{String.valueOf(result.id)});
            adapter.updateItem(position, result.downloadPath);
        }
    }
}