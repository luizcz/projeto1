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

    public static JSONObject getLocationFormGoogle(String placesName) {

        HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?address=" + placesName + "&ka&sensor=false");
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
}
