package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.oid.ObjectId;
import uk.co.panaxiom.playjongo.PlayJongo;

/**
 * A class representing the FoodTruck entities and collection
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class FoodTruck {
    public static MongoCollection foodTrucks(){
        return PlayJongo.getCollection("trucks");
    }

    @ObjectId
    private String _id;

    /**
     * the name of the food truck
     */
    public String applicant;

    /**
     * the menu items
     */
    public String fooditems;

    /**
     * the location information
     */
    public Location location;

    /**
     * the address
     */
    public String locationdescription;
}
