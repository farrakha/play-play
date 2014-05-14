package services;

import play.libs.WS;
import play.mvc.Result;
import views.html.*;
import play.*;


import static play.libs.F.Function;
import static play.libs.F.Promise;

/**
 * Created by afarrakha on 2014-05-10.
 */
public class SFFoodTruckService {

    public static final String SF_FOOD_TRUCKS_API = "http://data.sfgov.org/";

    private SFFoodTruckService(){
    }

//    public static Promise<Result> getTrucksNearLocation(double topLeftLatitude, double topLeftLongitude, double bottomRightLatitude, double bottomRightLongitude ){
//        //Soda2Consumer consumer = Soda2Consumer.newConsumer(SF_FOOD_TRUCKS_API);
//
//        //ClientResponse response = consumer.query("rqzj-sfat", HttpLowLevel.JSON_TYPE, query);
//        //String payload = response.getEntity(String.class);
//
//        String query = String.format("rqzj-sfat.json?$where=within_box(location, %f, %f, %f, %f)", topLeftLatitude, topLeftLongitude, bottomRightLatitude, bottomRightLongitude);
//        final Promise<Result> resultPromise = WS.url(SF_FOOD_TRUCKS_API+query).get().map(
//                new Function<WS.Response, Result>() {
//                    public Result apply(WS.Response response) {
//                        System.out.println(response.asJson());
//                        return ok("Hello");
//                    }
//                }
//        );
//        return resultPromise;
//    }

}
