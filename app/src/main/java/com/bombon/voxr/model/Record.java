package com.bombon.voxr.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Vaughn on 9/22/17.
 */

public class Record extends RealmObject {

    @SerializedName("id")
    @Expose
    @PrimaryKey
    private Integer id;
    @SerializedName("date_created")
    @Expose
    private String dateCreated;
    @SerializedName("emotion")
    @Expose
    private Emotion emotion;
    @SerializedName("file_path")
    @Expose
    private String filePath;

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Emotion getEmotion() {
        return emotion;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}