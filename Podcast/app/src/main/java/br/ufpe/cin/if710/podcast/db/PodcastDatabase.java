package br.ufpe.cin.if710.podcast.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import br.ufpe.cin.if710.podcast.db.dao.PodcastDao;
import br.ufpe.cin.if710.podcast.db.entities.Podcast;

@Database(entities = {Podcast.class}, version = 1)
public abstract class PodcastDatabase extends RoomDatabase {
    private static PodcastDatabase instance;

    public abstract PodcastDao podcastDao();

    public static PodcastDatabase getPodcastDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    PodcastDatabase.class, "podcast-database").build();
        }
        return instance;
    }

    public static void destroy() {
        instance = null;
    }
}
