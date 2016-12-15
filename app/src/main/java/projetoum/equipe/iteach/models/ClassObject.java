package projetoum.equipe.iteach.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties

public class ClassObject {

    public String teacherId;
    public List<String> studentsId;
    public Long time;
    public Double value;
    public String address;
    public Double lat;
    public Double lon;
    public Double slots;
    public List<String> tags;

    public ClassObject() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public ClassObject(String teacherId, List<String> studentsId, Long time,
                 Double value, String address, Double lat, Double lon,
                 Double slots, List<String> tags) {
        this.teacherId = teacherId;
        this.studentsId = studentsId;
        this.time = time;
        this.value = value;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
        this.slots = slots;
        this.tags = tags;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("teacherId", teacherId);
        result.put("studentsId", studentsId);
        result.put("time", time);
        result.put("value", value);
        result.put("address", address);
        result.put("lat", lat);
        result.put("lon", lon);
        result.put("slots", slots);
        result.put("tags", tags);

        return result;
    }

}