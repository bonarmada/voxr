package com.bombon.voxr.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bonbonme on 10/31/2017.
 */


public class WavFile {

    @SerializedName("wav_file")
    @Expose
    private String wavFile;

    public String getWavFile() {
        return wavFile;
    }

    public void setWavFile(String wavFile) {
        this.wavFile = wavFile;
    }
}
