package com.bombon.voxr.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Vaughn on 9/22/17.
 */

public class Record extends RealmObject {

    @SerializedName("anger")
    @Expose
    public Float anger;
    @SerializedName("date_created")
    @Expose
    public String dateCreated;
    @SerializedName("fear")
    @Expose
    public Integer fear;
    @SerializedName("file_path")
    @Expose
    public String filePath;
    @SerializedName("happiness")
    @Expose
    public Float happiness;
    @SerializedName("id")
    @Expose
    @PrimaryKey
    public Integer id;
    @SerializedName("neutrality")
    @Expose
    public Integer neutrality;
    @SerializedName("sadness")
    @Expose
    public Integer sadness;

    public Float getAnger() {
        return anger;
    }

    public void setAnger(Float anger) {
        this.anger = anger;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Integer getFear() {
        return fear;
    }

    public void setFear(Integer fear) {
        this.fear = fear;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Float getHappiness() {
        return happiness;
    }

    public void setHappiness(Float happiness) {
        this.happiness = happiness;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNeutrality() {
        return neutrality;
    }

    public void setNeutrality(Integer neutrality) {
        this.neutrality = neutrality;
    }

    public Integer getSadness() {
        return sadness;
    }

    public void setSadness(Integer sadness) {
        this.sadness = sadness;
    }
}