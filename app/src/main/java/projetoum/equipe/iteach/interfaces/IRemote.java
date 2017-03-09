package projetoum.equipe.iteach.interfaces;

import android.location.Location;

import java.util.List;

import projetoum.equipe.iteach.adapter.ClassAdapter;
import projetoum.equipe.iteach.adapter.UserAdapter;
import projetoum.equipe.iteach.models.ClassObject;
import projetoum.equipe.iteach.models.Rating;
import projetoum.equipe.iteach.models.User;

/**
 * Created by Victor on 13-Dec-16.
 */

public interface IRemote {


    public void createUser(User user, ICallback callback);
    public void deleteUser(User user, ICallback callback);
    public void updateUser(User user, ICallback callback);
    public void deleteUser(String userID, ICallback callback);
    public void searchUser(String anything,UserAdapter adapter);
    public void findUserById(String id,ICallback callback);


    public void rateUser(Rating rating, User user, ICallback callback);
    public void rateUser(Rating rating, String userID, ICallback callback);

    public void createClass(ClassObject classObject, ICallback callback);
    public void deleteClass(ClassObject classObject, ICallback callback);
    public void updateClass(ClassObject classObject, ICallback callback);
    public void deleteClass(String classID, ICallback callback);

    public void findClassByName(String name, ICallback callback);
    //public List<ClassObject> findClassByName(String name);
    public List<ClassObject> findClassByTag(String tag);
    public void findClassById(String id,ICallback<ClassObject> callback);
    public List<ClassObject> findClassByLocation(Location loc);
    public void findClassByTeacher(String userID, ICallback<List<ClassObject>> callback);
    public List<ClassObject> findClassByAttendee(String userID);
    public void searchClass(String anything,ClassAdapter adapter);


    public void loadFirstTeachers(UserAdapter adapter);
}
