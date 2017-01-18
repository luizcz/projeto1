package projetoum.equipe.iteach.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties

public class ClassObject {

    private String teacherId;
    private List<String> studentsId;
    private Long time;
    private Double value;
    private String address;
    private Double lat;
    private Double lon;
    private Double slots;
    private List<String> tags;
    private String name;
    private String id;

    public ClassObject(String name) {
       this.name = name;
    }

    public ClassObject(){

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

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public List<String> getStudentsId() {
        return studentsId;
    }

    public void setStudentsId(List<String> studentsId) {
        this.studentsId = studentsId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getSlots() {
        return slots;
    }

    public void setSlots(Double slots) {
        this.slots = slots;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassObject that = (ClassObject) o;

        if (teacherId != null ? !teacherId.equals(that.teacherId) : that.teacherId != null)
            return false;
        if (studentsId != null ? !studentsId.equals(that.studentsId) : that.studentsId != null)
            return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (lat != null ? !lat.equals(that.lat) : that.lat != null) return false;
        if (lon != null ? !lon.equals(that.lon) : that.lon != null) return false;
        if (slots != null ? !slots.equals(that.slots) : that.slots != null) return false;
        return tags != null ? tags.equals(that.tags) : that.tags == null;

    }

    @Override
    public int hashCode() {
        int result = teacherId != null ? teacherId.hashCode() : 0;
        result = 31 * result + (studentsId != null ? studentsId.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (lat != null ? lat.hashCode() : 0);
        result = 31 * result + (lon != null ? lon.hashCode() : 0);
        result = 31 * result + (slots != null ? slots.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }
}