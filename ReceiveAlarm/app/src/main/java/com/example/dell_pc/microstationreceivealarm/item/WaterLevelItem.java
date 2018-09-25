package com.example.dell_pc.microstationreceivealarm.item;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class WaterLevelItem {
    private String ID;
    private String constructionName;
    private String position;
    private String type;
    private String level;
    private String maxLevel;
    private String referValue;
    private String lat;
    private String lon;
    private String lastTime;


    public WaterLevelItem(String ID, String constructionName, String position, String type, String level, String maxLevel, String referValue, String lat, String lon, String lastTime) {
        setID(ID);
        setType(type);
        setLat(lat);
        setLon(lon);
        setLastTime(lastTime);
        setConstructionName(constructionName);
        setPosition(position);
        setLevel(level);
        setMaxLevel(maxLevel);
        setReferValue(referValue);
    }

    public String getConstructionName() {
        return constructionName;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getLon() {
        return lon;
    }

    public String getID() {
        return ID;
    }

    public String getLastTime() {
        return lastTime;
    }

    public String getLat() {
        return lat;
    }

    public String getPosition() {
        return position;
    }

    public String getReferValue() {
        return referValue;
    }

    public String getType() {
        return type;
    }

    public void setConstructionName(String constructionName) {
        this.constructionName = constructionName;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setReferValue(String referValue) {
        this.referValue = referValue;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public String getMaxLevel() {
        return maxLevel;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setMaxLevel(String maxLevel) {
        this.maxLevel = maxLevel;
    }
}

