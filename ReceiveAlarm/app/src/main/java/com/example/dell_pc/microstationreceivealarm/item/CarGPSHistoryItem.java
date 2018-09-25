package com.example.dell_pc.microstationreceivealarm.item;

import com.baidu.mapapi.map.Marker;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class CarGPSHistoryItem {
    private String ID;
    private String name;
    private String level;
    private String lon;
    private String lat;
    private String dateTime;
//    private Marker marker;


    public CarGPSHistoryItem(String ID, String name, String level, String lat, String lon, String dateTime) {
        setID(ID);
        setName(name);
        setLevel(level);
        setLat(lat);
        setLon(lon);
//        setMarker(marker);
        setDateTime(dateTime);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}


