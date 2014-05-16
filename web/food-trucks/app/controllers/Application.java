package controllers;

import models.Bounds;
import models.FoodTruck;

import play.*;
import play.libs.Akka;
import play.mvc.*;
import play.libs.WS;
import play.mvc.Result;

import static play.libs.F.Function;
import static play.libs.F.Promise;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * The main application controller
 */
public class Application extends Controller {

    private static final ObjectMapper _mapper = new ObjectMapper();

    public static final String SF_FOOD_TRUCKS_API = "http://data.sfgov.org/resource/rqzj-sfat.json?";
    public static final String DEFAULT_PROJECTION = "applicant, fooditems, location";

    /*
     * Loads the index page
     */
    public static Result index() {
        return ok(views.html.index.render());
    }


    /**
     * Retrieves food truck information for a given location
     * from the San Francisco gov web service.
     *
     * The request expects a json body containing the NE and SW bounds of the location.
     * The response time from the service for this query varies significantly.
     *
     * @return Result a json array of trucks within the given bounds.
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Promise<Result> liveBounds() {
        JsonNode json = request().body().asJson();
        JsonNode boundsNode = json.get("bounds");
        if(boundsNode != null) {
            try {
                Bounds bounds = _mapper.readValue(boundsNode.toString(), Bounds.class);
                Logger.info("Application-liveBounds: received bounds request: " + bounds.toString());
                String query = formatBoundsWebQuery(bounds);
                //call the web service asynchronously and return a promise of the result
                Promise<Result> resultPromise = WS.url(SF_FOOD_TRUCKS_API)
                        .setQueryParameter("$select", DEFAULT_PROJECTION)
                        .setQueryParameter("$where", query).get().map(
                        new Function<WS.Response, Result>() {
                            public Result apply(WS.Response response) {
                                if(response.getStatus() == 200){
                                    return ok(response.asJson());
                                }else{
                                    Logger.error("Application-liveBounds: Live bounds request failed, service error: " + response.getStatusText());
                                    return internalServerError();
                                }
                            }
                        }
                );
                return resultPromise;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return badRequestPromise();
    }

    /**
     * Retrieves food truck information for a given location
     * from the locally cached data.
     *
     * The request expects a json body containing the NE and SW bounds of the location.
     *
     * @return Result a json array of trucks within the given bounds.
     */
    @BodyParser.Of(BodyParser.Json.class)
    public static Result bounds() {
        if(FoodTruck.foodTrucks().count() == 0){
            Logger.warn("Application-bounds: no data to serve");
            //redirect(liveBounds());
        }
        JsonNode json = request().body().asJson();
        JsonNode boundsNode = json.get("bounds");
        if(boundsNode != null) {
            try {
                Bounds bounds = _mapper.readValue(boundsNode.toString(), Bounds.class);
                Logger.info("Application-bounds: received bounds request: " + bounds.toString());
                Iterable<FoodTruck> result = FoodTruck.foodTrucks().find(formatBoundsDBQuery(bounds)).as(FoodTruck.class);
                return ok(_mapper.writeValueAsString(result));
            } catch (IOException e) {
                e.printStackTrace();
                return internalServerError();
            }
        }else{
            return badRequest("request format not recognized");
        }
    }

    /**
     * Formats the bounds condition for the web service request
     * @param bounds the bounds to use for the web service query
     * @return the formatted query condition
     */
    private static String formatBoundsWebQuery(Bounds bounds){
        String query = String.format("within_box(location,%f,%f,%f,%f)", bounds.topLeftLatitude
                , bounds.topLeftLongitude,  bounds.bottomRightLatitude, bounds.bottomRightLongitude);
        return query;
    }

    /**
     * Formats the bounds condition for the db query
     * @param bounds the bounds to use for the db query
     * @return the formatted query condition
     */
    private static String formatBoundsDBQuery(Bounds bounds){
        String query = String.format("{location: { $geoWithin : { $box : [ [ %f , %f ] , [ %f , %f ] ] } } }"
                , bounds.getBottomLeftLongitude(), bounds.getBottomLeftLatitude()
                , bounds.getTopRightLongitude(), bounds.getTopRightLatitude());
        return query;
    }

    private static Promise<Result> badRequestPromise(){
        Promise<Result> promiseOfResult = Akka.future(
            new Callable<Result>() {
                public Result call() {
                    return badRequest("request format not recognized");
                }
            }
        );
        return promiseOfResult;
    }
}
