package br.ufpe.cin.if710.podcast.db.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import br.ufpe.cin.if710.podcast.db.PodcastDatabase;
import br.ufpe.cin.if710.podcast.db.dao.PodcastDao;
import br.ufpe.cin.if710.podcast.db.entities.Podcast;

/**
 * Created by fbormann on 25/11/17.
 */

public class ListPodcastViewModel extends ViewModel {
    private MutableLiveData<List<Podcast>> podcasts;

    public LiveData<List<Podcast>> getPodcasts() {
        if (podcasts == null) {
            podcasts = new MutableLiveData<List<Podcast>>();
            loadPodcasts();
        }
        return podcasts;
    }

    private void loadPodcasts() {
    }
}
