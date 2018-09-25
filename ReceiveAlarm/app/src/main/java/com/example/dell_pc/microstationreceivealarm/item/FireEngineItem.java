package com.example.dell_pc.microstationreceivealarm.item;

import com.baidu.mapapi.map.Marker;

/**
 * Created by Administrator on 2018/3/21 0021.
 */

public class FireEngineItem {


    /**
     * name : 堤口路大润发微型消防车
     * longitude : 116.99125450667
     * latitude : 36.682330825567
     * level : 12
     * pic : null
     * material_type : 水
     * driver : 崔通
     * driver_phone : 18596095937
     * master : 董兆麟
     * master_phone : 17615838028
     * equipment : 直流水枪2把，多功能水枪1把，水带6盘（80mm2盘，65mm4盘），空呼器4具
     * long : 116.99125450668
     * lati : 36.682330825567
     */

    private String name;
    private String longitude;
    private String latitude;
    private String level;
    private Object pic;
    private String material_type;
    private String driver;
    private String driver_phone;
    private String master;
    private String master_phone;
    private String equipment;
    private String longX;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    private String lati;
    private Marker marker;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Object getPic() {
        return pic;
    }

    public void setPic(Object pic) {
        this.pic = pic;
    }

    public String getMaterial_type() {
        return material_type;
    }

    public void setMaterial_type(String material_type) {
        this.material_type = material_type;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDriver_phone() {
        return driver_phone;
    }

    public void setDriver_phone(String driver_phone) {
        this.driver_phone = driver_phone;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getMaster_phone() {
        return master_phone;
    }

    public void setMaster_phone(String master_phone) {
        this.master_phone = master_phone;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getLongX() {
        return longX;
    }

    public void setLongX(String longX) {
        this.longX = longX;
    }

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }
}
