package com.bombon.voxr.model;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.RealmObject;

/**
 * Created by bonbonme on 10/31/2017.
 */


public class Emotion extends RealmObject {

    @SerializedName("anger")
    @Expose
    private Double anger;
    @SerializedName("fear")
    @Expose
    private Double fear;
    @SerializedName("happiness")
    @Expose
    private Double happiness;
    @SerializedName("neutrality")
    @Expose
    private Double neutrality;
    @SerializedName("sadness")
    @Expose
    private Double sadness;

    public Double getAnger() {
        return anger;
    }

    public void setAnger(Double anger) {
        this.anger = anger;
    }

    public Double getFear() {
        return fear;
    }

    public void setFear(Double fear) {
        this.fear = fear;
    }

    public Double getHappiness() {
        return happiness;
    }

    public void setHappiness(Double happiness) {
        this.happiness = happiness;
    }

    public Double getNeutrality() {
        return neutrality;
    }

    public void setNeutrality(Double neutrality) {
        this.neutrality = neutrality;
    }

    public Double getSadness() {
        return sadness;
    }

    public void setSadness(Double sadness) {
        this.sadness = sadness;
    }

    @Override
    public String toString() {
        return "Emotion{" +
                "anger=" + anger +
                ", fear=" + fear +
                ", happiness=" + happiness +
                ", neutrality=" + neutrality +
                ", sadness=" + sadness +
                '}';
    }
}
