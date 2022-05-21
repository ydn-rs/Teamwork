package ru.yudin_r.teamwork.models;

public class Task {
    private String id, boardId, text, creatorId;
    private int status;

    public Task() {
    }

    public Task(String id, String boardId, String text, String creatorId, int status) {
        this.id = id;
        this.boardId = boardId;
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

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
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
