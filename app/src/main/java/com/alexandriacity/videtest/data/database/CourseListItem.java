package com.alexandriacity.videtest.data.database;

public class CourseListItem {

    private int id;
    private String name;
    private String imageUrl;
    private long numberOfViews;

    public CourseListItem(int id, String name, String imageUrl, long numberOfViews) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.numberOfViews = numberOfViews;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNumberOfViews() {
        return numberOfViews;
    }

    public void setNumberOfViews(long numberOfViews) {
        this.numberOfViews = numberOfViews;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
