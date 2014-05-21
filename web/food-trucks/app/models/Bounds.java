package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A simple object for passing around a map bounding box
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Bounds {
    public double topLeftLatitude;
    public double topLeftLongitude;
    public double bottomRightLatitude;
    public double bottomRightLongitude;

    public double getTopLeftLatitude(){
        return topLeftLatitude;
    }

    public double getTopLeftLongitude(){
        return topLeftLongitude;
    }

    public double getBottomRightLatitude(){
        return bottomRightLatitude;
    }

    public double getBottomRightLongitude(){
        return bottomRightLongitude;
    }

    public double getTopRightLatitude(){
        return topLeftLatitude;
    }

    public double getTopRightLongitude(){
        return bottomRightLongitude;
    }

    public double getBottomLeftLatitude(){
        return bottomRightLatitude;
    }

    public double getBottomLeftLongitude(){
        return topLeftLongitude;
    }

    public String toString(){
        return String.format("%s , %s , %s , %s", topLeftLongitude, topLeftLatitude, bottomRightLongitude, bottomRightLatitude);
    }
}
