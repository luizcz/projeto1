package projetoum.equipe.iteach.models;

import android.net.Uri;

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
    private String imagem ;
    private String data;
    private String subject;

    public ClassObject(String name) {
        this.name = name;
    }

    public ClassObject() {

    }

    public ClassObject(String teacherId, List<String> studentsId, Long time,
                       Double value, String address, Double lat, Double lon,
                       Double slots, List<String> tags, String name, String id) {
        this.teacherId = teacherId;
        this.studentsId = studentsId;

        this.time = time;
        this.value = value;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
        this.slots = slots;
        this.tags = tags;
        this.name = name;
        this.id = id;
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
        result.put("name", name);
        result.put("id", id);
        result.put("data", data);
        result.put("image", imagem);
        result.put("subject", subject);

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassObject)) return false;

        ClassObject that = (ClassObject) o;

        if (!getTeacherId().equals(that.getTeacherId())) return false;
        if (getStudentsId() != null ? !getStudentsId().equals(that.getStudentsId()) : that.getStudentsId() != null)
            return false;
        if (getTime() != null ? !getTime().equals(that.getTime()) : that.getTime() != null)
            return false;
        if (getValue() != null ? !getValue().equals(that.getValue()) : that.getValue() != null)
            return false;
        if (getAddress() != null ? !getAddress().equals(that.getAddress()) : that.getAddress() != null)
            return false;
        if (getLat() != null ? !getLat().equals(that.getLat()) : that.getLat() != null)
            return false;
        if (getLon() != null ? !getLon().equals(that.getLon()) : that.getLon() != null)
            return false;
        if (getSlots() != null ? !getSlots().equals(that.getSlots()) : that.getSlots() != null)
            return false;
        if (getTags() != null ? !getTags().equals(that.getTags()) : that.getTags() != null)
            return false;
        if (!getName().equals(that.getName())) return false;
        return getId().equals(that.getId());

    }

    @Override
    public int hashCode() {
        int result = getTeacherId().hashCode();
        result = 31 * result + (getStudentsId() != null ? getStudentsId().hashCode() : 0);
        result = 31 * result + (getTime() != null ? getTime().hashCode() : 0);
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
        result = 31 * result + (getLat() != null ? getLat().hashCode() : 0);
        result = 31 * result + (getLon() != null ? getLon().hashCode() : 0);
        result = 31 * result + (getSlots() != null ? getSlots().hashCode() : 0);
        result = 31 * result + (getTags() != null ? getTags().hashCode() : 0);
        result = 31 * result + getName().hashCode();
        result = 31 * result + getId().hashCode();
        return result;
    }

}