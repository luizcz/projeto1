package projetoum.equipe.iteach.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties

public class User {

    public String userId;
    public String name;
    public String email;
    public Double lat;
    public Double lon;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public User(String userId, String name, String email, Double lat, Double lon) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.lat = lat;
        this.lon = lon;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("name", name);
        result.put("email", email);
        result.put("lat", lat);
        result.put("lon", lon);

        return result;
    }

}