package com.example.study_friend;

public class FriendItem {
    //닉네임
    String name;
    //채팅방 생성 날짜
    String date;
    //프로필 사진
    int resourceId;
    //채팅방 타이틀
    String title;

    public FriendItem(int resourceId, String name, String date,String title) {
        this.name = name;
        this.title = title;
        this.date= date;
        this.resourceId = resourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
