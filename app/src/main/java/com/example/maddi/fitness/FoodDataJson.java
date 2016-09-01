package com.example.maddi.fitness;

import android.util.Log;
import android.widget.TextView;

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
    List<Map<String,?>> foodList;
    public TextView t;
    public List<Map<String, ?>> getMoviesList() {
        return foodList;
    }
    public int getSize(){
        return foodList.size();
    }
    public HashMap getItem(int i){
        if (i >=0 && i < foodList.size()){
            return (HashMap) foodList.get(i);
        } else
            return null;
    }

    public FoodDataJson() {
        foodList = new ArrayList<Map<String,?>>();
    }
    public void removeItem(int i)
    {
        foodList.remove(i);
    }
    public void addItem(int position, HashMap clone)
    {
        foodList.add(position, clone);
    }

    public void downloadFoodDataJson(String json_url) throws JSONException {
        foodList.clear(); // clear the list

        String foodArray = MyUtility.downloadJSONusingHTTPGetRequest(json_url);
        foodArray = foodArray.toString();
        //foodArray = '{'+foodArray;
        longInfo(foodArray);
        Log.d("FoodArray", foodArray);

        if (foodArray == null){
            Log.d("MyDebugMsg", "Having trouble loading URL: " + json_url);
            return;
        }

        String json = "Assuming that here is your JSON response";
        try {
            JSONObject parentObject = new JSONObject(foodArray);
            JSONArray hitsJsonArray = parentObject.getJSONArray("hits");
            Log.d("hits",hitsJsonArray.toString());
            Log.d("hits length",String.valueOf(hitsJsonArray.length()));
            for (int i = 0; i < hitsJsonArray.length(); ++i) {
                JSONObject f = hitsJsonArray.getJSONObject(i);
                JSONObject fi = f.getJSONObject("fields");
                Log.d("Hits array item:",fi.toString());
                {
                    String iid = fi.getString("item_id");
                    String iname = fi.getString("item_name");
                    String bid = fi.getString("brand_id");
                    String bname = fi.getString("brand_name");
                    String ical = fi.getString("nf_calories");
                    String idesc = fi.getString("item_description");
                    String ifat = fi.getString("nf_total_fat");
                    String iprotein = fi.getString("nf_protein");
                    String icarbs = fi.getString("nf_total_carbohydrate");
                    foodList.add(createFood_brief(iid,iname,bid,bname,ical,idesc,ifat,iprotein,icarbs));
                }
            }

            //And then read attributes like
            /*String iid = feildsObject.getString("item_id");
            String iname = feildsObject.getString("item_name");
            String bid = feildsObject.getString("brand_id");
            String bname = feildsObject.getString("brand_name");
            foodList.add(createFood_brief(iid,iname,bid,bname));*/

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("MyDebugMsg", "JSONException in downloadFoodDataJson");
            e.printStackTrace();
        }

    }

    private static HashMap createFood_brief(String iid,String iname,String bid,String bname,String ical, String idesc, String ifat, String iprotein, String icarbs) {
        HashMap fd = new HashMap();

        fd.put("iid", iid);
        fd.put("iname", iname);
        fd.put("bid",bid);
        fd.put("bname", bname);
        fd.put("ical", ical);
        fd.put("idesc", idesc);
        fd.put("ifat", ifat);
        fd.put("icarbs", icarbs);
        fd.put("iprotein", iprotein);
        return fd;
    }

    public static void longInfo(String str) {
        if(str.length() > 4000) {
            Log.i("FoodArray1:", str.substring(0, 4000));
            longInfo(str.substring(4000));
        } else
            Log.i("FoodArray2", str);
    }
}
