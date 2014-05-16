package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.oid.ObjectId;
import uk.co.panaxiom.playjongo.PlayJongo;

/**
 * Created by afarrakha on 2014-05-10.
 *
 * A class representing the FoodTruck entities and collection
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class FoodTruck {
    public static MongoCollection foodTrucks(){
        return PlayJongo.getCollection("trucks");
    }

    @ObjectId
    private String _id;

    public String applicant;

    public String fooditems;

    public Location location;
}
