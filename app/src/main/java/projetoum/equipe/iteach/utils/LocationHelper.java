package projetoum.equipe.iteach.utils;

import android.os.StrictMode;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

/**
 * Created by Victor on 19-Mar-17.
 */

public class LocationHelper {

    public static JSONObject getLocationFromGoogle(String placesName) {

        HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?address=" + convertURL(placesName) + "&ka&sensor=false");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {

            e.printStackTrace();
        }

        return jsonObject;
    }

    public static JSONObject getLocationFromGoogleLatLng(LatLng location) {

        HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?latlng=" + location.latitude+","+location.longitude + "&ka&sensor=false");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {

            e.printStackTrace();
        }

        return jsonObject;
    }


    public static LatLng getLatLng(JSONObject jsonObject) {

        Double lon = 0d;
        Double lat = 0d;

        try {

            lon = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new LatLng(lat, lon);
    }

    public static String getLocation(JSONObject jsonObject) {

        String location = "";

        try {

            location = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getString("formatted_address");


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Gets the distance formatted for displaying.
     *
     * @param distanceInMeters The distance in meters
     * @return The formated distance
     */
    public static String getFormatedDistance(double distanceInMeters) {
        DecimalFormat df;
        if (distanceInMeters < Constants.KM_IN_METERS) {
            df = new DecimalFormat("#3");
            return String.valueOf(df.format(distanceInMeters)) + "m";
        } else {
            df = new DecimalFormat("#0.0");
            return String.valueOf(df.format(distanceInMeters / Constants.KM_IN_METERS)) + "Km";
        }
    }

    public static String convertURL(String str) {

        String url = null;
        try {
            url = new String(str.trim().replace(" ", "%20").replace("&", "%26")
                    .replace(",", "%2c").replace("(", "%28").replace(")", "%29")
                    .replace("!", "%21").replace("=", "%3D").replace("<", "%3C")
                    .replace(">", "%3E").replace("#", "%23").replace("$", "%24")
                    .replace("'", "%27").replace("*", "%2A").replace("-", "%2D")
                    .replace(".", "%2E").replace("/", "%2F").replace(":", "%3A")
                    .replace(";", "%3B").replace("?", "%3F").replace("@", "%40")
                    .replace("[", "%5B").replace("\\", "%5C").replace("]", "%5D")
                    .replace("_", "%5F").replace("`", "%60").replace("{", "%7B")
                    .replace("|", "%7C").replace("}", "%7D"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }
}
