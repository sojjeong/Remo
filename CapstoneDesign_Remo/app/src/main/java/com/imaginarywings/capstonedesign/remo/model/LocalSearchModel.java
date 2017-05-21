package com.imaginarywings.capstonedesign.remo.model;

import java.util.List;

/**
 * Created by S.JJ on 2017-05-17.
 */

public class LocalSearchModel {
    private String lastBuildDate;
    private Integer total;
    private Integer start;
    private Integer display;

    private List<LocalSearchItemModel> itemlist;

    private String getLastBuildDate() { return lastBuildDate; }

    public Integer getTotal() { return total; }

    public Integer getStart() { return start; }

    public Integer getDisplay() { return display; }

    public List<LocalSearchItemModel> getItems() { return itemlist; }

    @Override
    public String toString() {
        String string = "LocalSearchModel { " +
                "lastBuildDate = " + lastBuildDate + '\'' +
                ", total=" + total +
                ", start=" + start +
                ", display=" + display +
                ", itemlist = " + itemlist +
                '}';

        return string;
    }
}
