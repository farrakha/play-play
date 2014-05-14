package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Bounds;
import play.*;
import play.libs.Akka;
import play.libs.Json;
import play.mvc.*;
import play.libs.WS;
import play.mvc.Result;

import static play.libs.F.Function;
import static play.libs.F.Promise;
import services.SFFoodTruckService;
import views.html.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.Callable;

public class Application extends Controller {

    private static final ObjectMapper _mapper = new ObjectMapper();

    public static final String SF_FOOD_TRUCKS_API = "http://data.sfgov.org/resource/rqzj-sfat.json?";

    public static Result index() {
        return ok(views.html.index.render());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Promise<Result> bounds() {
        JsonNode json = request().body().asJson();
        JsonNode boundsNode = json.get("bounds");
        if(boundsNode != null) {
            try {
                Bounds bounds = _mapper.readValue(boundsNode.toString(), Bounds.class);
                String query = formatBoundsQuery(bounds);
                Promise<Result> resultPromise = WS.url(SF_FOOD_TRUCKS_API)
                        .setQueryParameter("$select", "applicant, latitude, longitude")
                        .setQueryParameter("$where", query).get().map(
                        new Function<WS.Response, Result>() {
                            public Result apply(WS.Response response) {
                                return ok(response.asJson());
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

    private static String formatBoundsQuery(Bounds bounds){
        String query = String.format("within_box(location,%f,%f,%f,%f)", bounds.topLeftLatitude
                , bounds.topLeftLongitude,  bounds.bottomRightLatitude, bounds.bottomRightLongitude);
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
