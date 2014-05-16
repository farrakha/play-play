import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.FoodTruck;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.F;
import play.libs.WS;

import java.io.IOException;
import java.util.List;

/**
 * Created by afarrakha on 2014-05-15.
 */
public class Global extends GlobalSettings{

    private static final ObjectMapper _mapper = new ObjectMapper();

    @Override
    public void onStart(Application app) {
        Logger.info("Application has started");
        loadDataFromService();
    }

    @Override
    public void onStop(Application app) {
        Logger.info("Application shutdown...");
    }


    /**
     * Asynchronously loads data from the web service and updates the database
     */
    public void loadDataFromService(){
        WS.url(controllers.Application.SF_FOOD_TRUCKS_API)
                .setQueryParameter("$select", controllers.Application.DEFAULT_PROJECTION).setQueryParameter("status", "APPROVED").get().map(
                        new F.Function<WS.Response, List<FoodTruck>>() {
                            public List<FoodTruck> apply(WS.Response response) {
                                List<FoodTruck> trucks = null;
                                if(response.getStatus() == 200){
                                    String responseBody = response.getBody();
                                    if(responseBody.length() > 0){
                                        try {
                                            trucks = _mapper.readValue(responseBody, new TypeReference<List<FoodTruck>>(){});
                                            updateDataBase(trucks);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                return trucks;
                            }
                        }
                );
    }

    /**
     * Updates the database with new trucks data
     * @param trucks the new list of trucks
     */
    private void updateDataBase(List<FoodTruck> trucks){
        if(trucks != null && trucks.size() > 0){
            FoodTruck.foodTrucks().remove();
            for(FoodTruck truck: trucks){
                FoodTruck.foodTrucks().insert(truck);
            }
            FoodTruck.foodTrucks().ensureIndex("{location: '2d'}");
            System.out.println("new data size: " + FoodTruck.foodTrucks().count());
        }else{
            Logger.warn("Global-updateDataBase: Received no data from service");
        }
    }
}
