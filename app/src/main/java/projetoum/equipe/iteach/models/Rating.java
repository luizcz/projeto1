package projetoum.equipe.iteach.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties

public class Rating {

    public String userId;
    public String teacherId;
    public String grade;
    public String comment;

    public Rating() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Rating(String userId, String teacherId, String grade, String comment) {
        this.userId = userId;
        this.teacherId = teacherId;
        this.grade = grade;
        this.comment = comment;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("teacherId", teacherId);
        result.put("grade", grade);
        result.put("comment", comment);

        return result;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rating rating = (Rating) o;

        if (userId != null ? !userId.equals(rating.userId) : rating.userId != null) return false;
        if (teacherId != null ? !teacherId.equals(rating.teacherId) : rating.teacherId != null)
            return false;
        if (grade != null ? !grade.equals(rating.grade) : rating.grade != null) return false;
        return comment != null ? comment.equals(rating.comment) : rating.comment == null;

    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (teacherId != null ? teacherId.hashCode() : 0);
        result = 31 * result + (grade != null ? grade.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }
}