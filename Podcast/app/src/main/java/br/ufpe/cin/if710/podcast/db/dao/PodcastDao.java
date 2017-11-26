package br.ufpe.cin.if710.podcast.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.ufpe.cin.if710.podcast.db.entities.Podcast;

@Dao
public interface PodcastDao {
    @Query("SELECT * FROM podcast")
    LiveData<List<Podcast>> getAll();

    @Insert
    void insertAll(Podcast... podcast);

    @Insert
    void insert(Podcast podcast);

    @Update
    public void updatePodcast(Podcast podcast);
}
