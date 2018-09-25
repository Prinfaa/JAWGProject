package com.example.dell_pc.microstationreceivealarm.item;

import com.baidu.mapapi.map.Marker;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class CarItem {
    private String ID;
    private String name;
    private String materialType;
    private String weight;
    private String driver;
    private String driverPhone;
    private String master;
    private String masterPhone;
    private String equipment;
    private String picUrl;
    private String level;
    private String lon;
    private String lat;
    private Marker marker;


    public CarItem(String ID, String name, String materialType, String weight, String driver, String driverPhone, String master, String masterPhone, String equipment, String picUrl, String level, String lat, String lon, Marker marker) {
        setID(ID);
        setName(name);
        setMaterialType(materialType);
        setWeight(weight);
        setDriver(driver);
        setDriverPhone(driverPhone);
        setMaster(master);
        setMasterPhone(masterPhone);
        setEquipment(equipment);
        setPicUrl(picUrl);
        setLevel(level);
        setLat(lat);
        setLon(lon);
        setMarker(marker);
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

    public String getPicUrl() {
        return picUrl;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getDriver() {
        return driver;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public String getEquipment() {
        return equipment;
    }

    public String getMaterialType() {
        return materialType;
    }

    public String getMaster() {
        return master;
    }

    public String getWeight() {
        return weight;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getMasterPhone() {
        return masterPhone;
    }

    public void setMasterPhone(String masterPhone) {
        this.masterPhone = masterPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public void setWeight(String weight) {
        this.weight = weight;
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

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }
}


