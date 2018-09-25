package com.example.dell_pc.microstationreceivealarm.item;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class AlarmKeyItem {
    private String ID;
    private String name;
    private String address;
    private String lat;
    private String lon;
    private String lastTime;
    private String constructionID;


    public AlarmKeyItem(String ID, String name, String address, String lat, String lon, String lastTime, String constructionID) {
        setAddress(address);
        setID(ID);
        setName(name);
        setLastTime(lastTime);
        setLon(lon);
        setLat(lat);
        setConstructionID(constructionID);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }


    public String getID() {
        return ID;
    }


    public void setAddress(String address) {
        this.address = address;
    }


    public void setID(String ID) {
        this.ID = ID;
    }

    public String getLastTime() {
        return lastTime;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getConstructionID() {
        return constructionID;
    }

    public void setConstructionID(String constructionID) {
        this.constructionID = constructionID;
    }
}

