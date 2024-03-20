package com.vanthom04.vtshop.models;

public class Slide {
    private String slideId;
    private String title;
    private String slug;
    private String thumbnail;

    public Slide(String slideId, String title, String slug, String thumbnail) {
        this.slideId = slideId;
        this.title = title;
        this.slug = slug;
        this.thumbnail = thumbnail;
    }

    public String getSlideId() {
        return slideId;
    }

    public String getTitle() {
        return title;
    }

    public String getSlug() {
        return slug;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
