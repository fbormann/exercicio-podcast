package br.ufpe.cin.if710.podcast.domain;

public interface PodcastModel {
    String getTitle();
    String getDescription();
    String getDownloadLink();
    String getPublishDate();
    String getFileUri();
    String getId();

    void setFileUri(String fileUri);
}
