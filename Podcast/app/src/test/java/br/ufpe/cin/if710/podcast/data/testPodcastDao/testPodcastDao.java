package br.ufpe.cin.if710.podcast.data.testPodcastDao;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import br.ufpe.cin.if710.podcast.db.PodcastDatabase;
import br.ufpe.cin.if710.podcast.db.entities.Podcast;

@RunWith(MockitoJUnitRunner.class)
public class testPodcastDao {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    private PodcastDatabase podcastDatabase;
    @Mock
    Context mMockContext;

    @Before
    public void setUp(){
        podcastDatabase = Room.inMemoryDatabaseBuilder(mMockContext, PodcastDatabase.class).build();
    }

    @Test
    public void insertAllData(){

        new AsyncTask<Void, Void, Integer>() {
            @Override
            public Integer doInBackground(Void... params) {
                List<Podcast> podcasts = PodcastFactory.producePodcasts(10);
                for (int i = 0; i < podcasts.size(); i++) {
                    podcastDatabase.podcastDao().insertAll(podcasts.get(i));
                }
                List<Podcast> podcastsAfterInsert;
                podcastsAfterInsert = podcastDatabase.podcastDao().getAllPodcasts()
                        .getValue();
                System.out.println("whatever");
                assert (podcastsAfterInsert.isEmpty());
                return new Integer(3);
            }
        }.execute();
    }

    @Test
    public void updateData() {
        new Thread(new Runnable(){
            public void run() {
                String description = "nova description";
                List<Podcast> podcasts = PodcastFactory.producePodcasts(1);
                for (int i = 0; i < podcasts.size(); i++) {
                    podcastDatabase.podcastDao().insertAll(podcasts.get(i));
                }
                List<Podcast> podcastsAfterInsert = podcastDatabase.podcastDao().getAllPodcasts()
                        .getValue();

                podcastsAfterInsert.get(0).setDescription(description);
                podcastDatabase.podcastDao().updatePodcast(podcastsAfterInsert.get(0));
                assert (podcastDatabase.podcastDao().getAllPodcasts().getValue().get(0)
                        .getDescription().equals(description));
            }
        });
    }

    @After
    public void tearDown(){
        podcastDatabase.close();
    }
}
