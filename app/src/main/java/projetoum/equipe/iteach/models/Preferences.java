package projetoum.equipe.iteach.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Victor on 13-Dec-16.
 */

@IgnoreExtraProperties

public class Preferences {

    public String googleAccount;
    public String facebookAccount;
    public Double maxDistance;
    public List<String> favoriteTags;

    public Preferences() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Preferences(String googleAccount, String facebookAccount, Double maxDistance, List<String> favoriteTags, String payment) {
        this.googleAccount = googleAccount;
        this.facebookAccount = facebookAccount;
        this.maxDistance = maxDistance;
        this.favoriteTags = favoriteTags;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("googleAccount", googleAccount);
        result.put("facebookAccount", facebookAccount);
        result.put("maxDistance", maxDistance);
        result.put("favoriteTags", favoriteTags);

        return result;
    }

    public String getGoogleAccount() {
        return googleAccount;
    }

    public void setGoogleAccount(String googleAccount) {
        this.googleAccount = googleAccount;
    }

    public String getFacebookAccount() {
        return facebookAccount;
    }

    public void setFacebookAccount(String facebookAccount) {
        this.facebookAccount = facebookAccount;
    }

    public Double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(Double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public List<String> getFavoriteTags() {
        return favoriteTags;
    }

    public void setFavoriteTags(List<String> favoriteTags) {
        this.favoriteTags = favoriteTags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Preferences that = (Preferences) o;

        if (googleAccount != null ? !googleAccount.equals(that.googleAccount) : that.googleAccount != null)
            return false;
        if (facebookAccount != null ? !facebookAccount.equals(that.facebookAccount) : that.facebookAccount != null)
            return false;
        if (maxDistance != null ? !maxDistance.equals(that.maxDistance) : that.maxDistance != null)
            return false;
        return favoriteTags != null ? favoriteTags.equals(that.favoriteTags) : that.favoriteTags == null;

    }

    @Override
    public int hashCode() {
        int result = googleAccount != null ? googleAccount.hashCode() : 0;
        result = 31 * result + (facebookAccount != null ? facebookAccount.hashCode() : 0);
        result = 31 * result + (maxDistance != null ? maxDistance.hashCode() : 0);
        result = 31 * result + (favoriteTags != null ? favoriteTags.hashCode() : 0);
        return result;
    }
}
