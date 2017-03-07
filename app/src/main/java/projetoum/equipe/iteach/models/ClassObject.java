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
        if (o == null || getClass() != o.getClass()) return false;

        ClassObject that = (ClassObject) o;

        if (slots != that.slots) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (data != null ? !data.equals(that.data) : that.data != null) return false;
        if (dataFim != null ? !dataFim.equals(that.dataFim) : that.dataFim != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (diasSemana != null ? !diasSemana.equals(that.diasSemana) : that.diasSemana != null)
            return false;
        if (horaInicio != null ? !horaInicio.equals(that.horaInicio) : that.horaInicio != null)
            return false;
        if (horaFim != null ? !horaFim.equals(that.horaFim) : that.horaFim != null) return false;
        if (imagem != null ? !imagem.equals(that.imagem) : that.imagem != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (lat != null ? !lat.equals(that.lat) : that.lat != null) return false;
        if (lon != null ? !lon.equals(that.lon) : that.lon != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
        if (tags != null ? !tags.equals(that.tags) : that.tags != null) return false;
        if (teacherId != null ? !teacherId.equals(that.teacherId) : that.teacherId != null)
            return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = address != null ? address.hashCode() : 0;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + (dataFim != null ? dataFim.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (diasSemana != null ? diasSemana.hashCode() : 0);
        result = 31 * result + (horaInicio != null ? horaInicio.hashCode() : 0);
        result = 31 * result + (horaFim != null ? horaFim.hashCode() : 0);
        result = 31 * result + (imagem != null ? imagem.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (lat != null ? lat.hashCode() : 0);
        result = 31 * result + (lon != null ? lon.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + slots;
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (teacherId != null ? teacherId.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}