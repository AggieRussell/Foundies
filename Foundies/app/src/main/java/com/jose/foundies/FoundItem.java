package com.jose.foundies;

/**
 * Created by kylepreston on 3/29/17.
 */

public class FoundItem {

    String id, category1, category2, category3, lat, lng, timestamp, user;

    public FoundItem(String id, String category1, String category2, String category3, String lat, String lng, String timestamp, String user) {
        this.id = id;
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
        this.lat = lat;
        this.lng = lng;
        this.timestamp = timestamp;
        this.user = user;
    }

    public FoundItem() {
    }

    public String getId() {
        return id;
    }

    public String getCategory1() {
        return category1;
    }

    public String getCategory2() {
        return category2;
    }

    public String getCategory3() {
        return category3;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public void setCategory3(String category3) {
        this.category3 = category3;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
