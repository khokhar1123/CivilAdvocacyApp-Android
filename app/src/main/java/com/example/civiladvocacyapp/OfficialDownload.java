package com.example.civiladvocacyapp;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class OfficialDownload {
    private static MainActivity mainActivity;
    private static RequestQueue queue;
    private static final String API_Key = "key=AIzaSyAvnDd8yIpVjcKYcFCFuEETvevvFUWRM3I";
    private static final String DATA_URL = "https://www.googleapis.com/civicinfo/v2/representatives?";
    private static final String ADDR_URL = "&address=";
//    "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyAvnDd8yIpVjcKYcFCFuEETvevvFUWRM3I&address=60616"

    public static void downloadOfficial(MainActivity mainActivityIn, String postal_code ) {
        mainActivity = mainActivityIn;
        queue = Volley.newRequestQueue(mainActivity);

        Uri dataUri = Uri.parse(DATA_URL + API_Key + ADDR_URL + postal_code);
        String formatedURL = dataUri.toString();

        Response.Listener<JSONObject> listener =
              response -> mainActivity.updateData(parseJSON(response.toString()));
        //        response  ->parseJSON(response.toString());
        Response.ErrorListener error =
                error1 -> mainActivity.downloadFailed();
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, formatedURL,null,listener, error);
        queue.add(jsonObjectRequest);
    }

    private static ArrayList<Official> parseJSON(String s) {
        String res="";
        ArrayList<Official> oList = new ArrayList<>();
        try {
            JSONObject jObjMain = new JSONObject(s);
            JSONObject normalized_info = null;
            normalized_info = jObjMain.getJSONObject("normalizedInput");
            String line1 = normalized_info.getString("line1");
            String city = normalized_info.getString("city");
            String state = normalized_info.getString("state");
            String zip =normalized_info.getString("zip");
            if(line1.equals("")){

                res = city+ ", " +state + " " + zip;

            } else{
                res = line1 + "," +city+ ", " +state + " " + zip;
            }
            mainActivity.getLocationFromVolley(res);
            JSONArray offices_info = jObjMain.getJSONArray("offices");
            JSONArray officials_info = jObjMain.getJSONArray("officials");

            //loop through the officials_info, create official obj for each entry, put them into the arraylist in order
            for(int i = 0; i < officials_info.length(); i++){
                JSONObject jOfficial =  (JSONObject) officials_info.get(i);
                Official temp = new Official();
                temp.setName(jOfficial.getString("name"));
                temp.setParty(jOfficial.getString("party"));
                if(jOfficial.has("photoUrl")){
                    String url = jOfficial.getString("photoUrl");
                    url = url.replaceFirst("(?i)^http://", "https://");
                    temp.setI_url(url);
                }
                if(jOfficial.has("phones")){
                    temp.setNum(jOfficial.getJSONArray("phones").get(0).toString());
                }
                if(jOfficial.has("emails")){
                    temp.setEmail(jOfficial.getJSONArray("emails").get(0).toString());
                }
                if(jOfficial.has("urls")){
                    temp.setWebsite(jOfficial.getJSONArray("urls").get(0).toString());
                }


                StringBuilder address = new StringBuilder();
                if(jOfficial.has("address")){
                    JSONObject jAddress = (JSONObject) jOfficial.getJSONArray("address").get(0);
                    if(jAddress.has("line1")){
                        address.append(jAddress.get("line1")).append(" ");
                    }
                    if(jAddress.has("city")){
                        address.append(jAddress.get("city")).append(", ");
                    }
                    if(jAddress.has("state")){
                        address.append(jAddress.get("state")).append(" ");
                    }
                    if(jAddress.has("zip")){
                        address.append(jAddress.get("zip"));
                    }
                    temp.setAddress(address.toString());
                }

                if(jOfficial.has("channels")){
                    JSONArray jChannels = jOfficial.getJSONArray("channels");
                    for(int j = 0; j < jChannels.length(); j++){
                        JSONObject ch = (JSONObject) jChannels.get(j);
                        if(ch.get("type").toString().equals("Facebook")){
                            temp.setFacebook(ch.get("id").toString());
                        }
                        if(ch.get("type").toString().equals("Twitter")){
                            temp.setTwitter(ch.get("id").toString());
                        }
                        if(ch.get("type").toString().equals("YouTube")){
                            temp.setYoutube(ch.get("id").toString());
                        }
                    }
                }
                oList.add(temp);
            }
            //loop through the offices_info, for each entry, check the officialIndices field and loop through it,by get the object from arraylist by the index and put the office name to the object
            for(int i = 0; i < offices_info.length(); i++){
                JSONObject office = (JSONObject) offices_info.get(i);
                JSONArray indices = office.getJSONArray("officialIndices");
                String office_name = office.getString("name");
                for(int j = 0; j < indices.length(); j++){
                    int outter_index = (int)indices.get(j);
                    oList.get(outter_index).setOffice(office_name);
                }
            }
            return oList;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}