package com.imaginarywings.capstonedesign.remo.model;

/**
 * Created by S.JJ on 2017-05-17.
 */

public class LocalSearchItemModel {
    private String title;
    private String link;
    private String category;
    private String description;
    private String telephone;
    private String address;
    private String roadAddress;
    private String mapx;
    private String mapy;

    public String getTitle() { return title; }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getAddress() {
        return address;
    }

    public String getRoadAddress() {
        return roadAddress;
    }

    public String getMapx() { return mapx; }

    public String getMapy() {
        return mapy;
    }

    @Override
    public String toString() {
        return "LocalSearchModel{" +
                "title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", telephone='" + telephone + '\'' +
                ", address='" + address + '\'' +
                ", roadAddress='" + roadAddress + '\'' +
                ", mapx='" + mapx + '\'' +
                ", mapy='" + mapy + '\'' +
                '}';
    }
}
