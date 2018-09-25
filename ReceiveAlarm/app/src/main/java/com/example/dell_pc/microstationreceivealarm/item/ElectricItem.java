package com.example.dell_pc.microstationreceivealarm.item;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class ElectricItem {
    private String ID;
    private String constructionName;
    private String position;
    private String current;
    private String MaxCurrent;
    private String temp;
    private String maxTemp;
    private String lat;
    private String lon;
    private String lastTime;


    public ElectricItem(String ID, String constructionName, String position, String current, String maxCurrent, String temp, String maxTemp, String lat, String lon, String lastTime) {
        setID(ID);
        setLat(lat);
        setLon(lon);
        setLastTime(lastTime);
        setConstructionName(constructionName);
        setPosition(position);
        setCurrent(current);
        setMaxCurrent(maxCurrent);
        setTemp(temp);
        setMaxTemp(maxTemp);
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

    public void setConstructionName(String constructionName) {
        this.constructionName = constructionName;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCurrent() {
        return current;
    }

    public String getMaxCurrent() {
        return MaxCurrent;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public String getTemp() {
        return temp;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public void setMaxCurrent(String maxCurrent) {
        MaxCurrent = maxCurrent;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}

