package com.bombon.voxr.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by bonbonme on 10/31/2017.
 */


public class Emotion extends RealmObject{

    @SerializedName("anger")
    @Expose
    private Double anger;
    @SerializedName("fear")
    @Expose
    private Integer fear;
    @SerializedName("happiness")
    @Expose
    private Double happiness;
    @SerializedName("neutrality")
    @Expose
    private Integer neutrality;
    @SerializedName("sadness")
    @Expose
    private Integer sadness;

    public Double getAnger() {
        return anger;
    }

    public void setAnger(Double anger) {
        this.anger = anger;
    }

    public Integer getFear() {
        return fear;
    }

    public void setFear(Integer fear) {
        this.fear = fear;
    }

    public Double getHappiness() {
        return happiness;
    }

    public void setHappiness(Double happiness) {
        this.happiness = happiness;
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
