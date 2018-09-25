package com.jinganweigu.RoadWayFire.ToolsUtils.widget;

import android.os.Parcel;
import android.os.Parcelable;

import com.videogo.openapi.bean.EZDeviceInfo;

/**
 * 作者： Prinfaa .
 * 创建时间：2018/1/16 0016 09:52
 * 功能描述：
 */

public class EZDeviceAddLatAndLangInfo implements Parcelable {

    private EZDeviceInfo deviceSerical;
    private double lat;
    private double lag;

    public EZDeviceAddLatAndLangInfo() {
    }

    protected EZDeviceAddLatAndLangInfo(Parcel in) {
        deviceSerical = in.readParcelable(EZDeviceInfo.class.getClassLoader());
        lat = in.readDouble();
        lag = in.readDouble();
    }

    public static final Creator<EZDeviceAddLatAndLangInfo> CREATOR = new Creator<EZDeviceAddLatAndLangInfo>() {
        @Override
        public EZDeviceAddLatAndLangInfo createFromParcel(Parcel in) {
            return new EZDeviceAddLatAndLangInfo(in);
        }

        @Override
        public EZDeviceAddLatAndLangInfo[] newArray(int size) {
            return new EZDeviceAddLatAndLangInfo[size];
        }
    };

    public EZDeviceInfo getDeviceSerical() {
        return deviceSerical;
    }

    public void setDeviceSerical(EZDeviceInfo deviceSerical) {
        this.deviceSerical = deviceSerical;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLag() {
        return lag;
    }

    public void setLag(double lag) {
        this.lag = lag;
    }

    @Override
    public String toString() {
        return "EZDeviceAddLatAndLangInfo{" +
                "deviceSerical='" + deviceSerical + '\'' +
                ", lat=" + lat +
                ", lag=" + lag +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {


        parcel.writeParcelable(deviceSerical, i);
        parcel.writeDouble(lat);
        parcel.writeDouble(lag);
    }
}
