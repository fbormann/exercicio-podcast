package br.ufpe.cin.if710.podcast.domain;

public class ItemFeed {
    private final String title;
    private final String link;
    private final String pubDate;
    private final String description;
    private final String downloadLink;
    private int id;
    private String fileUri;

    public ItemFeed(String title, String link, String pubDate, String description, String downloadLink) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.description = description;
        this.downloadLink = downloadLink;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getDescription() {
        return description;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public int getId() {
        return this.id;
    }

    public String getFileUri() {
        return this.fileUri;
    }

    @Override
    public String toString() {
        return title;
    }
}