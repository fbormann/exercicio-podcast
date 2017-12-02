package br.ufpe.cin.if710.podcast.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.ufpe.cin.if710.podcast.db.entities.Podcast;

@Dao
public interface PodcastDao {
    @Query("SELECT * FROM " + Podcast.TABLE_NAME)
    LiveData<List<Podcast>> getAllPodcasts();

    @Insert
    void insertAll(Podcast... podcast);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Podcast podcast);

    @Update
    void updatePodcast(Podcast podcast);
}
