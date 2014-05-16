package models;

/*
 * A simple object for passing around a map bounding box
 *
 */
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
        return topLeftLongitude + " , " + topLeftLatitude + " , " + bottomRightLongitude + " , " + bottomRightLatitude;
    }
}
