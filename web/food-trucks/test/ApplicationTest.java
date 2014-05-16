import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Bounds;
import org.junit.*;

import play.libs.Json;
import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;

import static org.junit.Assert.fail;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;


public class ApplicationTest {

    private static final ObjectMapper _mapper = new ObjectMapper();

    @Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertThat(a).isEqualTo(2);
    }


    @Test
    public void callBounds(){
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                Bounds bounds = new Bounds();
                bounds.topLeftLatitude = 37.80;
                bounds.topLeftLongitude = -122.46;
                bounds.bottomRightLatitude = 37.76;
                bounds.bottomRightLongitude = -122.40;
                Result result = callAction(controllers.routes.ref.Application.bounds(), new FakeRequest().withJsonBody(Json.toJson(bounds)));
                assertThat(status(result)).isEqualTo(OK);
                assertThat(contentType(result)).isEqualTo("application/json");
                String resultNode = contentAsString(result);
                System.out.println(resultNode);
                assertThat(resultNode.length()).isGreaterThan(0);
            }
        });
    }

    @Test
    public void callLiveBounds(){
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                Bounds bounds = new Bounds();
                bounds.topLeftLatitude = 37.80;
                bounds.topLeftLongitude = -122.46;
                bounds.bottomRightLatitude = 37.76;
                bounds.bottomRightLongitude = -122.40;
                Result result = callAction(controllers.routes.ref.Application.liveBounds(), new FakeRequest().withJsonBody(Json.toJson(bounds)));
                assertThat(status(result)).isEqualTo(OK);
                assertThat(contentType(result)).isEqualTo("application/json");
                String resultNode = contentAsString(result);
                System.out.println(resultNode);
                assertThat(resultNode.length()).isGreaterThan(0);
            }
        });
    }
}
