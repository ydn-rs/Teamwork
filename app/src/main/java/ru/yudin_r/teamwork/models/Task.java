package ru.yudin_r.teamwork.models;

public class Task {

    private String id, projectId, text, creatorId;
    private int status;

    public Task() {
    }

    public Task(String id, String projectId, String text, String creatorId, int status) {
        this.id = id;
        this.projectId = projectId;
        this.text = text;
        this.creatorId = creatorId;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
