package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.oid.ObjectId;
import uk.co.panaxiom.playjongo.PlayJongo;

/**
 * Created by afarrakha on 2014-05-10.
 */
public class FoodTruck {
    public static MongoCollection foodTrucks(){
        return PlayJongo.getCollection("trucks");
    }

    @JsonProperty("_id")
    public ObjectId id;

}
