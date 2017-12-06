package br.ufpe.cin.if710.podcast.data.testPodcastDao;

import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.if710.podcast.db.entities.Podcast;

public class PodcastFactory {
    public static List<Podcast> producePodcasts(int n){
        int i = 0;
        ArrayList<Podcast> podcasts = new ArrayList<>();
        while (i < n) {
            podcasts.add(new Podcast("title_"+i, "21/03/03", "link_"+i,
                    "description_"+i, "downloadLink_"+i,
                    "episodeFile_"+i));
            i += 1;
        }
        return podcasts;
    }
}
