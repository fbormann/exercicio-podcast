package br.ufpe.cin.if710.podcast.db.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

import br.ufpe.cin.if710.podcast.domain.PodcastModel;

@Entity(tableName = Podcast.TABLE_NAME)
public class Podcast implements PodcastModel {
    /** The name of the Cheese table. */
    public static final String TABLE_NAME = "podcasts";

    @PrimaryKey
    @NonNull
    private String id;
    private String title;
    private String publishDate;
    private String link;
    private String description;
    private String downloadLink;
    private String episodeFileURI;

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDownloadLink() {
        return this.downloadLink;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public void setEpisodeFileURI(String episodeFileURI) {
        this.episodeFileURI = episodeFileURI;
    }

    public String getPublishDate() {
        return this.publishDate;
    }

    public String getFileUri() {
        return this.episodeFileURI;
    }

    public void setFileUri(String fileUri) {
        this.episodeFileURI = fileUri;
    }

    public Podcast(String title, String publishDate, String link, String description,
                   String downloadLink, String episodeFileURI) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.publishDate = publishDate;
        this.link = link;
        this.description = description;
        this.downloadLink = downloadLink;
        this.episodeFileURI = episodeFileURI;
    }

    public String getLink() {
        return link;
    }

    public String getEpisodeFileURI() {
        return episodeFileURI;
    }
}
