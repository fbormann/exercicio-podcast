package br.ufpe.cin.if710.podcast.db.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import br.ufpe.cin.if710.podcast.db.entities.Podcast;


public class ListPodcastViewModel extends ViewModel {
    private MutableLiveData<List<Podcast>> podcasts;

    public LiveData<List<Podcast>> getPodcasts() {
        if (podcasts == null) {
            podcasts = new MutableLiveData<>();
            loadPodcasts();
        }
        return podcasts;
    }

    private void loadPodcasts() {
    }
}
