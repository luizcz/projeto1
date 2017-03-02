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
    private int slots;
    private String subject;
    private List<String> tags;
    private String teacherId;
    private Long time;
    private Double value;
    private List<String> alunos;

    public ClassObject(String name) {
        this.name = name;
    }

    public ClassObject() {

    }

    public ClassObject(String teacherId, Long time,
                       Double value, String address, Double lat, Double lon,
                       int slots, List<String> tags, String name, String id, List<String> alunos) {
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
        this.alunos = alunos;
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
        result.put("alunos", alunos);

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

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
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

    public List<String> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<String> alunos) {
        this.alunos = alunos;
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

        if (getSlots() != that.getSlots()) return false;
        if (!getTeacherId().equals(that.getTeacherId())) return false;
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
        if (getTags() != null ? !getTags().equals(that.getTags()) : that.getTags() != null)
            return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null)
            return false;
        if (!getId().equals(that.getId())) return false;
        if (getImagem() != null ? !getImagem().equals(that.getImagem()) : that.getImagem() != null)
            return false;
        if (getData() != null ? !getData().equals(that.getData()) : that.getData() != null)
            return false;
        if (getSubject() != null ? !getSubject().equals(that.getSubject()) : that.getSubject() != null)
            return false;
        if (getDiasSemana() != null ? !getDiasSemana().equals(that.getDiasSemana()) : that.getDiasSemana() != null)
            return false;
        if (getDataFim() != null ? !getDataFim().equals(that.getDataFim()) : that.getDataFim() != null)
            return false;
        if (getHoraInicio() != null ? !getHoraInicio().equals(that.getHoraInicio()) : that.getHoraInicio() != null)
            return false;
        return getHoraFim() != null ? getHoraFim().equals(that.getHoraFim()) : that.getHoraFim() == null;

    }

    @Override
    public int hashCode() {
        int result = getTeacherId().hashCode();
        result = 31 * result + (getTime() != null ? getTime().hashCode() : 0);
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
        result = 31 * result + (getLat() != null ? getLat().hashCode() : 0);
        result = 31 * result + (getLon() != null ? getLon().hashCode() : 0);
        result = 31 * result + getSlots();
        result = 31 * result + (getTags() != null ? getTags().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + getId().hashCode();
        result = 31 * result + (getImagem() != null ? getImagem().hashCode() : 0);
        result = 31 * result + (getData() != null ? getData().hashCode() : 0);
        result = 31 * result + (getSubject() != null ? getSubject().hashCode() : 0);
        result = 31 * result + (getDiasSemana() != null ? getDiasSemana().hashCode() : 0);
        result = 31 * result + (getDataFim() != null ? getDataFim().hashCode() : 0);
        result = 31 * result + (getHoraInicio() != null ? getHoraInicio().hashCode() : 0);
        result = 31 * result + (getHoraFim() != null ? getHoraFim().hashCode() : 0);
        return result;
    }
}