import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.routes;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Bounds;
import models.FoodTruck;
import org.junit.*;

import play.libs.Json;
import play.mvc.*;
import play.test.*;

import static org.junit.Assert.fail;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;


public class ApplicationTest {

    private static final ObjectMapper _mapper = new ObjectMapper();
    private static Bounds bounds;
    private static FakeApplication app;
    private static TestServer server;

    @BeforeClass
    public static void before(){
        app = fakeApplication();
        server = testServer(3211, app);
        start(server);
        bounds = new Bounds();
        bounds.topLeftLatitude = 37.80;
        bounds.topLeftLongitude = -122.46;
        bounds.bottomRightLatitude = 37.76;
        bounds.bottomRightLongitude = -122.40;
    }

    @AfterClass
    public static void after(){
        stop(server);
        stop(app);
    }

    @Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertThat(a).isEqualTo(2);
    }

    @Test
    public void callLiveBounds(){
        ObjectNode request = Json.newObject();
        request.put("bounds", Json.toJson(bounds));
        Result result = callAction(routes.ref.Application.liveBounds(), new FakeRequest().withJsonBody(request));
        assertThat(status(result)).isEqualTo(OK);
        String resultNode = contentAsString(result);
        System.out.println(resultNode);
        assertThat(resultNode.length()).isGreaterThan(0);
        try {
            List<FoodTruck> trucks = _mapper.readValue(resultNode, new TypeReference<List<FoodTruck>>(){});
            assertThat(trucks.size()).isGreaterThan(0);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void callBounds(){
        ObjectNode request = Json.newObject();
        request.put("bounds", Json.toJson(bounds));
        Result result = callAction(routes.ref.Application.bounds(), new FakeRequest().withJsonBody(request));
        assertThat(status(result)).isEqualTo(OK);
        String resultString = contentAsString(result);
        System.out.println(resultString);
        JsonNode resultNode = Json.parse(resultString);
        String trucksString= resultNode.get("trucks").asText();
        try {
            List<FoodTruck> trucks = _mapper.readValue(trucksString, new TypeReference<List<FoodTruck>>(){});
            assertThat(trucksString.length()).isGreaterThan(0);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

}
