package net.tomo1560.zenly.api;

public class UserLocation {

    /**
     * ユーザID
     */
    public String userId;

    /**
     * 座標
     */
    private Location location;

    /**
     *
     * @param userId
     * @param latitude
     * @param longitude
     */
    public UserLocation(String userId, double latitude, double longitude) {
        this.userId = userId;
        this.location = new Location(latitude, longitude);
    }

    public String getUserId() {
        return userId;
    }

    public Location getLocation() {
        return location;
    }
}
