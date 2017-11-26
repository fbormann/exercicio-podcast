package br.ufpe.cin.if710.podcast.db.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import br.ufpe.cin.if710.podcast.domain.PodcastModel;

/**
 * Created by fbormann on 25/11/17.
 */

@Entity
public class Podcast implements PodcastModel {
    @PrimaryKey
    private int uId;
    private String title;
    private Date publishDate;
    private String link;
    private String description;
    private String downloadLink;
    private String episodeFileURI;

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDownloadLink() {
        return this.downloadLink;
    }

    public Date getPublishDate() {
        return this.publishDate;
    }

    public Podcast(int id, String title, Date publishDate, String link, String description,
                   String downloadLink, String episodeFileURI) {
        this.uId = id;
        this.title = title;
        this.publishDate = publishDate;
        this.link = link;
        this.description = description;
        this.downloadLink = downloadLink;
        this.episodeFileURI = episodeFileURI;
    }
}
