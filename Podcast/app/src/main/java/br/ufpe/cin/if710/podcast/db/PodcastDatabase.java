package br.ufpe.cin.if710.podcast.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import br.ufpe.cin.if710.podcast.db.dao.PodcastDao;
import br.ufpe.cin.if710.podcast.db.entities.Podcast;

@Database(entities = {Podcast.class}, version = 3)
public abstract class PodcastDatabase extends RoomDatabase {
    public static PodcastDatabase instance;

    public abstract PodcastDao podcastDao();

    public static PodcastDatabase getPodcastDatabase(Context context) {
        if (instance == null) {
            RoomDatabase.Builder<PodcastDatabase> builder = Room.databaseBuilder(context,
                    PodcastDatabase.class, "podcast-database")
                    .fallbackToDestructiveMigration();
            return builder.build();
        }
        return instance;
    }

    public static void destroy() {
        instance = null;
    }
}
