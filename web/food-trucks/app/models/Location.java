package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by afarrakha on 2014-05-15.
 *
 * Represents coordinates of a specific location on the globe
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Location {
    public double longitude;
    public double latitude;
}
