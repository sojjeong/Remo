package com.imaginarywings.capstonedesign.remo.model;

/**
 * Created by rimi on 2017. 6. 13..
 * Copyright (c) 2017 UserInsight Corp.
 */

public class SampleImageModel {
    private int id;
    private String sampleUrl;
    private String guideUrl;

    public SampleImageModel() {}

    public SampleImageModel(int id, String sampleUrl, String guideUrl) {
        this.id = id;
        this.sampleUrl = sampleUrl;
        this.guideUrl = guideUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSampleUrl() {
        return sampleUrl;
    }

    public void setSampleUrl(String sampleUrl) {
        this.sampleUrl = sampleUrl;
    }

    public String getGuideUrl() {
        return guideUrl;
    }

    public void setGuideUrl(String guideUrl) {
        this.guideUrl = guideUrl;
    }
}
