package projetoum.equipe.iteach.models;

        import com.google.firebase.database.Exclude;
        import com.google.firebase.database.IgnoreExtraProperties;

        import java.util.HashMap;
        import java.util.Map;

@IgnoreExtraProperties

public class Teacher extends User{

    public String bio;
    public String creationDate;
    public String accountId;

    public Teacher() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Teacher(String userId, String name, String email, Double lat, Double lon, String bio, String creationDate, String accountId) {
        super(userId, name, email, lat, lon);
        this.bio = bio;
        this.creationDate = creationDate;
        this.accountId = accountId;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("bio", bio);
        result.put("creationDate", creationDate);
        result.put("accountId", accountId);

        return result;
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
        if (o == null || getClass() != o.getClass()) return false;

        Teacher teacher = (Teacher) o;

        if (bio != null ? !bio.equals(teacher.bio) : teacher.bio != null) return false;
        if (creationDate != null ? !creationDate.equals(teacher.creationDate) : teacher.creationDate != null)
            return false;
        return accountId != null ? accountId.equals(teacher.accountId) : teacher.accountId == null;

    }

    @Override
    public int hashCode() {
        int result = bio != null ? bio.hashCode() : 0;
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (accountId != null ? accountId.hashCode() : 0);
        return result;
    }
}