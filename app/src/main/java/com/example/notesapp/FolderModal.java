package com.example.notesapp;

import com.google.firebase.Timestamp;

public class FolderModal {
    private String id;
    private String folderName;
    private Timestamp timeStamp;

    public FolderModal(String id, String folderName,Timestamp timeStamp) {
        this.id = id;
        this.folderName = folderName;
        this.timeStamp = timeStamp;
    }

    public FolderModal() {

    }

    public String getId() {
        return id;
    }

    public String getFolderName() {
        return folderName;
    }

    public Timestamp getTimestamp() {
        return timeStamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timeStamp = timestamp;
    }
}
