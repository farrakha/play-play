package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents coordinates of a specific location on the globe
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Location {
    public double longitude;
    public double latitude;
}
