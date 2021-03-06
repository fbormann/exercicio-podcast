package br.ufpe.cin.if710.podcast.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.db.entities.Podcast;
import br.ufpe.cin.if710.podcast.services.MediaPlayerService;
import br.ufpe.cin.if710.podcast.services.RSSPullService;
import br.ufpe.cin.if710.podcast.ui.EpisodeDetailActivity;
import br.ufpe.cin.if710.podcast.ui.MainActivity;
public class XmlFeedAdapter extends ArrayAdapter<Podcast> {

    int linkResource;
    private Context context;
    private static final String ACTION_PLAY = "br.ufpe.cin.if701.podcast.action.PLAY";
    private static final String ACTION_PAUSE = "br.ufpe.cin.if701.podcast.action.PAUSE";

    public XmlFeedAdapter(Context context, int resource, List<Podcast> objects) {
        super(context, resource, objects);
        linkResource = resource;
        this.context = context;
    }

    public void updateItem(int position, String fileUri) {
        getItem(position).setFileUri(fileUri);
        notifyDataSetChanged();
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

    //http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
    static class ViewHolder {
        TextView item_title;
        TextView item_date;
        Button actionButton;
        int position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), linkResource, null);
            holder = new ViewHolder();
            holder.item_title = convertView.findViewById(R.id.item_title);
            holder.item_date = convertView.findViewById(R.id.item_date);
            holder.actionButton = convertView.findViewById(R.id.item_action);
            holder.position = position;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item_title.setText(getItem(position).getTitle());
        holder.item_date.setText(getItem(position).getPublishDate().toString());

        holder.item_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openDetails = new Intent(context, EpisodeDetailActivity.class);
                openDetails.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                openDetails.putExtra("feed_title", getItem(position).getTitle());
                openDetails.putExtra("feed_date", getItem(position).getPublishDate());
                openDetails.putExtra("feed_download_link", getItem(position).getDownloadLink());
                openDetails.putExtra("feed_description", getItem(position).getDescription());
                context.startActivity(openDetails);
            }
        });

        if (getItem(position).getFileUri() != null)
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
                        new RSSPullService()
                                .startActionDownloadEpisode(context, getItem(position).getDownloadLink(),
                                        String.valueOf(position));
                        btn.setText("tocar");
                        //notifyDataSetChanged();

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
}