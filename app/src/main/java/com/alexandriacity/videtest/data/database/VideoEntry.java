package com.alexandriacity.videtest.data.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "videos")
public class VideoEntry {

    @PrimaryKey
    private int number;
    private int courseId;
    private String name;
    private String duration;
    private String imageUrl;
    private String link;

    @Ignore
    public VideoEntry(){}

    public VideoEntry(int number, int courseId, String name, String duration, String imageUrl, String link) {
        this.number = number;
        this.courseId = courseId;
        this.name = name;
        this.duration = duration;
        this.imageUrl = imageUrl;
        this.link = link;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
