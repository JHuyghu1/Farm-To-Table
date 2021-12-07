package cs476.mavenproject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.wnameless.json.flattener.JsonFlattener;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GenCoordinates {
    
    class LatLng {
        double latitude = 0;
        double longitude = 0;

        public LatLng(double latitude, double longitude){
            this.latitude = latitude;
            this.longitude = longitude;
        }
        public void setLatitude(double latitude){
            this.latitude = latitude;
        }

        public void setLongitude(double longitude){
            this.longitude = longitude;
        }


    }

    static public LatLng generate(String address){


        address = address.replace(' ','+');
        List<String>found = new ArrayList<String>();
        
        try{

            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Request request = new Request.Builder()
                        .url("https://maps.googleapis.com/maps/api/geocode/json?address="+address+"&key=AIzaSyDLRwlZLh1JB6ureWmaPSj0lgdnkwNSaxg")
                        .method("GET", null)
                        .build();
    
            Response response = client.newCall(request).execute();

            JSONObject jsonObject = new JSONObject(response.body().string());
            
            Map<String, Object> flattenedJsonMap =
                    JsonFlattener.flattenAsMap(jsonObject.toString());
                        int locationFound[] = {0};

                        flattenedJsonMap.forEach((k, v) -> {
                        
                            if(locationFound[0] == 1 || locationFound[0] == 2){
                                found.add(v.toString());
                            }
    
                            if (k.contains("location") && !k.contains("location_")) {
                                locationFound[0] = locationFound[0] +1 ;
                            }


                    });

                    System.out.println(found + "  " + locationFound[0]);

            return null;

        } catch(Exception e){
            e.printStackTrace();

        }

        return null;

    }
}
