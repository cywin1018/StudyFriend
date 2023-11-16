package com.example.study_friend;

public class FriendItem {
    String name;
    String date;
    int resourceId;

    public FriendItem(int resourceId, String name, String date) {
        this.name = name;
        this.date= date;
        this.resourceId = resourceId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}
