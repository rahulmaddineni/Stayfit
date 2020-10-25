package com.example.maddi.fitness;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maddi on 3/29/2016.
 */
public class FoodDataJson {
    List<Map<String, ?>> foodList;

    public int getSize() {
        return foodList.size();
    }

    public FoodDataJson() {
        foodList = new ArrayList<Map<String, ?>>();
    }

    public void addItem(HashMap food) {
        foodList.add(food);
    }

    public void downloadFoodDataJson(String json_url) throws JSONException {
        foodList.clear(); // clear the list for each new request

        String foodJson = HTTPUtility.downloadJSONusingHTTPGetRequest(json_url);

        if (foodJson == null) {
            Log.d("MyDebugMsg", "Having trouble loading URL: " + json_url);
            return;
        }

        try {
            JSONObject parentObject = new JSONObject(foodJson);
            JSONArray hitsJsonArray = parentObject.getJSONArray("hits");
            for (int i = 0; i < hitsJsonArray.length(); ++i) {
                JSONObject hit = hitsJsonArray.getJSONObject(i);
                JSONObject fields = hit.getJSONObject("fields");
                createFood(fields);
            }
        } catch (JSONException e) {
            Log.d("Food Data JSON", "JSONException in downloadFoodDataJson");
            e.printStackTrace();
        }
    }

    private void createFood(JSONObject fields) throws JSONException {
        try {
            HashMap<String, String> food = new HashMap<String, String>();
            food.put("iid", fields.getString("item_id"));
            food.put("iname", fields.getString("item_name"));
            food.put("bid", fields.getString("brand_id"));
            food.put("bname", fields.getString("brand_name"));
            food.put("ical", fields.getString("nf_calories"));
            food.put("idesc", fields.getString("item_description"));
            food.put("ifat", fields.getString("nf_total_fat"));
            food.put("icarbs", fields.getString("nf_protein"));
            food.put("iprotein", fields.getString("nf_total_carbohydrate"));
            addItem(food);
        } catch (JSONException e) {
            Log.d("AddFood Error", "Check json array for food");
            e.printStackTrace();
        }
    }
}
