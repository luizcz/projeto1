package projetoum.equipe.iteach.utils;

import android.location.Location;

import java.util.List;

import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.interfaces.IRemote;
import projetoum.equipe.iteach.models.ClassObject;
import projetoum.equipe.iteach.models.Rating;
import projetoum.equipe.iteach.models.User;

/**
 * Created by Victor on 13-Dec-16.
 */

public class DAO implements IRemote {
    private static DAO instance;

    private DAO(){

 }
    public static DAO getInstace(){
        if(instance == null){
            instance = new DAO();
        }
        return instance;
    }

    @Override
    public void createUser(User user, ICallback callback) {

    }

    @Override
    public void deleteUser(User user, ICallback callback) {

    }

    @Override
    public void updateUser(User user, ICallback callback) {

    }

    @Override
    public void deleteUser(String userID, ICallback callback) {

    }

    @Override
    public void rateUser(Rating rating, User user, ICallback callback) {

    }

    @Override
    public void rateUser(Rating rating, String userID, ICallback callback) {

    }

    @Override
    public void createClass(ClassObject classObject, ICallback callback) {

    }

    @Override
    public void deleteClass(ClassObject classObject, ICallback callback) {

    }

    @Override
    public void updateClass(ClassObject classObject, ICallback callback) {

    }

    @Override
    public void deleteClass(String classID, ICallback callback) {

    }

    @Override
    public List<ClassObject> findClassByName(String name) {
        return null;
    }

    @Override
    public List<ClassObject> findClassByTag(String tag) {
        return null;
    }

    @Override
    public List<ClassObject> findClassByLocation(Location loc) {
        return null;
    }

    @Override
    public List<ClassObject> findClassByTeacher(String userID) {
        return null;
    }

    @Override
    public List<ClassObject> findClassByAttendee(String userID) {
        return null;
    }

    @Override
    public List<ClassObject> searchClass(String anything) {
        return null;
    }
}
