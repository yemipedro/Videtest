package com.alexandriacity.videtest.data.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class CourseEntry {

    @PrimaryKey
    private int id;
    private String name;
    private String userName;
    private String link;
    private String imageUrl;
    private long numberOfViews;
    private String channelName;
    private String profileImageUrl;
    private long numberOfSubscribers;

    @Ignore
    public CourseEntry(){}

    public CourseEntry(int id, String name, String userName, String link, String imageUrl, long numberOfViews, String channelName, String profileImageUrl, long numberOfSubscribers) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.link = link;
        this.imageUrl = imageUrl;
        this.numberOfViews = numberOfViews;
        this.channelName = channelName;
        this.profileImageUrl = profileImageUrl;
        this.numberOfSubscribers = numberOfSubscribers;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getNumberOfViews() {
        return numberOfViews;
    }

    public void setNumberOfViews(long numberOfViews) {
        this.numberOfViews = numberOfViews;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public long getNumberOfSubscribers() {
        return numberOfSubscribers;
    }

    public void setNumberOfSubscribers(long numberOfSubscribers) {
        this.numberOfSubscribers = numberOfSubscribers;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
