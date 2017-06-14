package studio.themad.droidcamera.Model;

/**
 * Created by AKHIL on 14-Jun-17.
 */

public class Photo {
    String location;
    String cloud;

    public Photo() {
    }

    public Photo(String location, String cloud) {
        this.location = location;
        this.cloud = cloud;
    }

    public String getLocation() {
        return location;
    }

    public String getCloudStatus() {
        return cloud;
    }
}
