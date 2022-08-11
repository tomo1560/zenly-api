package net.tomo1560.zenly.api;

public class Location {

    /**
     * 緯度
     */
    public double latitude;

    /**
     * 経度
     */
    public double longitude;


    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
