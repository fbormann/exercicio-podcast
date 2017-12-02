package br.ufpe.cin.if710.podcast.db.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import br.ufpe.cin.if710.podcast.db.PodcastDatabase;
import br.ufpe.cin.if710.podcast.db.dao.PodcastDao;
import br.ufpe.cin.if710.podcast.db.entities.Podcast;

public class ListPodcastViewModel extends AndroidViewModel {
    private LiveData<List<Podcast>> podcasts;

    public ListPodcastViewModel(Application application) {
        super(application);
    }

    public LiveData<List<Podcast>> getPodcasts(Application application) {
        if (podcasts == null) {
            podcasts = new MutableLiveData<>();
            loadPodcasts(application);
        }
        return podcasts;
    }

    private void loadPodcasts(Application application) {
        PodcastDao dao = PodcastDatabase.getPodcastDatabase(application.getApplicationContext())
                .podcastDao();

        podcasts = dao.getAllPodcasts();
    }
}
