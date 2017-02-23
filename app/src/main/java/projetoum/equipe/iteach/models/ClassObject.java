package projetoum.equipe.iteach.models;

import android.content.res.Resources;
import android.net.Uri;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import projetoum.equipe.iteach.R;

@IgnoreExtraProperties

public class ClassObject {

    private String address;
    private String data;
    private String dataFim;
    private String description;
    private List<String> diasSemana;
    private String horaInicio;
    private String horaFim;
    private String imagem;
    private String id;
    private Double lat;
    private Double lon;
    private String name;
    private Double slots;
    private String subject;
    private List<String> tags;
    private String teacherId;
    private Long time;
    private Double value;

    public ClassObject(String name) {
        this.name = name;
    }

    public ClassObject() {

    }

    public ClassObject(String teacherId, Long time,
                       Double value, String address, Double lat, Double lon,
                       Double slots, List<String> tags, String name, String id) {
        this.teacherId = teacherId;
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
        result.put("description", description);
        result.put("image", imagem);
        result.put("subject", subject);
        result.put("diasSemana", diasSemana);
        result.put("dataFim", dataFim);
        result.put("horaInicio", horaInicio);
        result.put("horaFim", horaFim);

        return result;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getDiasSemana() {
        return diasSemana;
    }

    public void setDiasSemana(List<String> diasSemana) {
        this.diasSemana = diasSemana;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public String getValorFormatado(){
        if (value == null){
            return "0";
        } else {
            DecimalFormat df = new DecimalFormat("##.##");
            df.setRoundingMode(RoundingMode.DOWN);
            return "R$ " + df.format(value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassObject)) return false;

        ClassObject that = (ClassObject) o;
        return getId().equals(that.getId());

    }

    @Override
    public int hashCode() {
        int result = getTeacherId().hashCode();
        result = 31 * result + (getTime() != null ? getTime().hashCode() : 0);
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
        result = 31 * result + (getLat() != null ? getLat().hashCode() : 0);
        result = 31 * result + (getLon() != null ? getLon().hashCode() : 0);
        result = 31 * result + (getSlots() != null ? getSlots().hashCode() : 0);
        result = 31 * result + (getTags() != null ? getTags().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + getId().hashCode();
        return result;
    }
}