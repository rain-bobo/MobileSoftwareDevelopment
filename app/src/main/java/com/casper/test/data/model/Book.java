package com.casper.test.data.model;

import java.io.Serializable;

public class Book implements Serializable {
    private String title;

    public Book(String title, int coverResourceId) {
        this.title = title;
        this.coverResourceId = coverResourceId;
    }

    private int coverResourceId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCoverResourceId() {
        return coverResourceId;
    }

    public void setCoverResourceId(int coverResourceId) {
        this.coverResourceId = coverResourceId;
    }
}
