package br.ufpe.cin.if710.podcast.domain;

import java.util.Date;

/**
 * Created by fbormann on 25/11/17.
 */

public interface PodcastModel {
    public String getTitle();
    public String getDescription();
    public String getDownloadLink();
    public Date getPublishDate();
}
