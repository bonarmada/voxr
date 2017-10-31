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
    @SerializedName("user_emotion")
    @Expose
    private String userEmotion;

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

    public String getUserEmotion() {
        return userEmotion;
    }

    public void setUserEmotion(String userEmotion) {
        this.userEmotion = userEmotion;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", dateCreated='" + dateCreated + '\'' +
                ", emotion=" + emotion +
                ", filePath='" + filePath + '\'' +
                ", userEmotion='" + userEmotion + '\'' +
                '}';
    }

    public String getEmotionResult() {
        if (getUserEmotion().equals("anger"))
            return EmotionEnum.ANGER;
        if (getUserEmotion().equals("fear"))
            return EmotionEnum.FEAR;
        if (getUserEmotion().equals("happiness"))
            return EmotionEnum.HAPPY;
        if (getUserEmotion().equals("neutrality"))
            return EmotionEnum.NEUTRAL;
        if (getUserEmotion().equals("sadness"))
            return EmotionEnum.SAD;
        else
            return null;
    }

}