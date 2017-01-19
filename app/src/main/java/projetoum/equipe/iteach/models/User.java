package projetoum.equipe.iteach.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties

public class User {

    public String userId;
    public String name;
    public String email;
    public Double lat;
    public Double lon;
    public List<String> favoriteClasses;
    public List<String> enrolledClasses;
    public String bio;
    public String creationDate;
    public String accountId;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public User(String userId, String name, String email, Double lat, Double lon, List<String> favoriteClasses, List<String> enrolledClasses, String bio, String creationDate, String accountId) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.lat = lat;
        this.lon = lon;
        this.favoriteClasses = favoriteClasses;
        this.enrolledClasses = enrolledClasses;
        this.bio = bio;
        this.creationDate = creationDate;
        this.accountId = accountId;
    }

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("name", name);
        result.put("email", email);
        result.put("lat", lat);
        result.put("lon", lon);
        result.put("favoriteClasses", favoriteClasses);
        result.put("enrolledClasses", enrolledClasses);
        result.put("bio", bio);
        result.put("creationDate", creationDate);
        result.put("accountId", accountId);
        return result;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public List<String> getFavoriteClasses() {
        return favoriteClasses;
    }

    public void setFavoriteClasses(List<String> favoriteClasses) {
        this.favoriteClasses = favoriteClasses;
    }

    public List<String> getEnrolledClasses() {
        return enrolledClasses;
    }

    public void setEnrolledClasses(List<String> enrolledClasses) {
        this.enrolledClasses = enrolledClasses;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (!getUserId().equals(user.getUserId())) return false;
        if (!getName().equals(user.getName())) return false;
        if (!getEmail().equals(user.getEmail())) return false;
        if (getLat() != null ? !getLat().equals(user.getLat()) : user.getLat() != null)
            return false;
        if (getLon() != null ? !getLon().equals(user.getLon()) : user.getLon() != null)
            return false;
        if (getFavoriteClasses() != null ? !getFavoriteClasses().equals(user.getFavoriteClasses()) : user.getFavoriteClasses() != null)
            return false;
        if (getEnrolledClasses() != null ? !getEnrolledClasses().equals(user.getEnrolledClasses()) : user.getEnrolledClasses() != null)
            return false;
        if (getBio() != null ? !getBio().equals(user.getBio()) : user.getBio() != null)
            return false;
        if (!getCreationDate().equals(user.getCreationDate())) return false;
        return getAccountId() != null ? getAccountId().equals(user.getAccountId()) : user.getAccountId() == null;

    }

    @Override
    public int hashCode() {
        int result = getUserId().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getEmail().hashCode();
        result = 31 * result + (getLat() != null ? getLat().hashCode() : 0);
        result = 31 * result + (getLon() != null ? getLon().hashCode() : 0);
        result = 31 * result + (getFavoriteClasses() != null ? getFavoriteClasses().hashCode() : 0);
        result = 31 * result + (getEnrolledClasses() != null ? getEnrolledClasses().hashCode() : 0);
        result = 31 * result + (getBio() != null ? getBio().hashCode() : 0);
        result = 31 * result + getCreationDate().hashCode();
        result = 31 * result + (getAccountId() != null ? getAccountId().hashCode() : 0);
        return result;
    }
}