package com.lxqhmlwyh.qingtingfm.entities;

public class FMCardView {
    private int content_id;
    private String description;
    private String cover;
    private String title;
    private boolean favorite;
    private int audience_count;

    public int getContent_id() {
        return content_id;
    }

    public void setContent_id(int content_id) {
        this.content_id = content_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getAudience_count() {
        return audience_count;
    }

    public void setAudience_count(int audience_count) {
        this.audience_count = audience_count;
    }

    @Override
    public String toString() {
        return "FMCardView{" +
                "content_id=" + content_id +
                ", description='" + description + '\'' +
                ", cover='" + cover + '\'' +
                ", title='" + title + '\'' +
                ", favorite=" + favorite +
                ", audience_count=" + audience_count +
                '}';
    }
}