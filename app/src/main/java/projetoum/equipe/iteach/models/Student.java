package projetoum.equipe.iteach.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties

public class Student extends User{


    public List<String> favoriteClasses;
    public List<String> enrolledClasses;

    public Student() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Student(String userId, String name, String email, Double lat, Double lon, List<String> favoriteClasses, List<String> enrolledClasses) {
        super(userId, name, email, lat, lon);
        this.favoriteClasses = favoriteClasses;
        this.enrolledClasses = enrolledClasses;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("favoriteClasses", favoriteClasses);
        result.put("enrolledClasses", enrolledClasses);

        return result;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (favoriteClasses != null ? !favoriteClasses.equals(student.favoriteClasses) : student.favoriteClasses != null)
            return false;
        return enrolledClasses != null ? enrolledClasses.equals(student.enrolledClasses) : student.enrolledClasses == null;

    }

    @Override
    public int hashCode() {
        int result = favoriteClasses != null ? favoriteClasses.hashCode() : 0;
        result = 31 * result + (enrolledClasses != null ? enrolledClasses.hashCode() : 0);
        return result;
    }
}