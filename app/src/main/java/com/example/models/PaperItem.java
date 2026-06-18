package com.example.models;

import java.io.Serializable;

public class PaperItem implements Serializable {
    private String title;
    private String thumb;
    private String url;
    private String price;

    public PaperItem() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getThumb() { return thumb; }
    public void setThumb(String thumb) { this.thumb = thumb; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
}
