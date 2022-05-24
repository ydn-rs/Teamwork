package ru.yudin_r.teamwork.models;

import java.util.ArrayList;

public class Project {

    private String id, title, creatorId;
    private ArrayList<String> users;

    public Project() {
    }

    public Project(String id, String title, String creatorId, ArrayList<String> users) {
        this.id = id;
        this.title = title;
        this.creatorId = creatorId;
        this.users = users;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }
}
