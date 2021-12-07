package cs476.mavenproject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RandomBuyers {
    
    Database DB;
    Categories categories = new Categories();
    List<String> savedAddresses = new ArrayList<String>();
    HashSet<Integer> usedAddress = new HashSet<Integer>();

    public RandomBuyers(Database DB){
        this.DB = DB;
    }
            

    private List<String> getNames(int amount){
        List<String> names = new ArrayList<>();

        try{

            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Request request = new Request.Builder()
                        .url("https://randommer.io/api/Name?nameType=firstname&quantity=" + (amount+10))
                        .method("GET", null)
                        .header("accept", "*/*")
                        .header("X-Api-Key", "59968b4ce95e4870ab2a4804deacfcdb")
                        .build();
    
            Response response = client.newCall(request).execute();

            JSONArray jArray = new JSONArray(response.body().string());

            for (int i = 0; i < jArray.length(); i++)
                names.add(jArray.optString(i).toLowerCase());
    
            return names;

        } catch(Exception e){
            e.printStackTrace();

        }

        return null;

    }


    public void createBuyers(int amount){

        String password = "pass";
        List<String> names = getNames(amount);
        String address = "Some address";

        int range = 10;
        int min = 1;
        Random r = new Random();

        for(int i = 0; i<amount; i = i+5){
            DB.createBuyerNode(DB, categories, names.get(i)+r.nextInt(range) + min, password, address);
            DB.createBuyerNode(DB, categories, names.get(i+1)+r.nextInt(range) + min, password, address);
            DB.createBuyerNode(DB, categories, names.get(i+2)+r.nextInt(range) + min, password, address);
            DB.createBuyerNode(DB, categories, names.get(i+3)+r.nextInt(range) + min, password, address);
            DB.createBuyerNode(DB, categories, names.get(i+4)+r.nextInt(range) + min, password, address);

            if(i%100 == 0)System.out.println(i);
        }

    }


}
