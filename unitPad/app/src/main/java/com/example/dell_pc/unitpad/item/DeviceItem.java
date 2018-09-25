package com.example.dell_pc.unitpad.item;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class DeviceItem {
    private String rtuID;
    private String typeID;
    private String type;
    private String position;
    private String value;
    private String maxValue;
    private String standardValue;
    private String state;
    private boolean online;
    private String lastTimestamp;


    public DeviceItem(String rtuID, String typeID, String type, String position, String value, String maxalue, String standardValue, String state, boolean online, String lastTimestamp) {
        setRtuID(rtuID);
        setTypeID(typeID);
        setType(type);
        setPosition(position);
        setValue(value);
        setState(state);
        setOnline(online);
        setLastTimestamp(lastTimestamp);
        setMaxValue(maxalue);
        setStandardValue(standardValue);
    }

    public String getRtuID() {
        return rtuID;
    }

    public void setRtuID(String rtuID) {
        this.rtuID = rtuID;
    }

    public boolean isOnline() {
        return online;
    }

    public String getLastTimestamp() {
        return lastTimestamp;
    }

    public String getState() {
        return state;
    }

    public String getType() {
        return type;
    }

    public String getTypeID() {
        return typeID;
    }

    public String getValue() {
        return value;
    }

    public void setLastTimestamp(String lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public String getStandardValue() {
        return standardValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public void setStandardValue(String standardValue) {
        this.standardValue = standardValue;
    }
}

