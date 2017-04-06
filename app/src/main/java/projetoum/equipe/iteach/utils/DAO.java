package projetoum.equipe.iteach.utils;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import projetoum.equipe.iteach.activities.MainActivity;
import projetoum.equipe.iteach.activities.SearchActivity;
import projetoum.equipe.iteach.adapter.ClassAdapter;
import projetoum.equipe.iteach.adapter.FeedAdapter;
import projetoum.equipe.iteach.adapter.UserAdapter;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.interfaces.IRemote;
import projetoum.equipe.iteach.models.ClassObject;
import projetoum.equipe.iteach.models.FeedItem;
import projetoum.equipe.iteach.models.Rating;
import projetoum.equipe.iteach.models.User;

/**
 * Created by Victor on 13-Dec-16.
 */

public class DAO implements IRemote {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static DAO instance;
    private ICallback<Boolean> callback;
    private Context ctx;
    private boolean resultado;
    private User currentUser;
    private FirebaseDatabase mDatabase;

    private DAO() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("FireBase", "onAuthStateChanged:signed_in:" + user.getUid());

                    final FirebaseDatabase database = getFirebaseInstance();
                    DatabaseReference myRef = database.getReference(Constants.FIREBASE_LOCATION_USER + "/" + user.getUid());


                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (!dataSnapshot.exists()) {
                                Calendar cal = Calendar.getInstance();
                                cal.add(Calendar.DATE, 1);
                                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                String formatted = format.format(cal.getTime());

                                createUser(new User(user.getUid(), user.getDisplayName(), user.getEmail(), user.getPhotoUrl().toString(), formatted, true), new ICallback() {
                                    @Override
                                    public void execute(Object param) {

                                    }
                                });
                                callback.execute(true);
                            } else {
                                callback.execute(true);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                } else {
                    // User is signed out
                    Log.d("FireBase", "onAuthStateChanged:signed_out");
                    callback.execute(false);
                }
                // ...
            }
        };

    }

    public static DAO getInstace(ICallback<Boolean> callback, Context ctx) {
        if (instance == null) {
            instance = new DAO();
        }

        instance.setCallback(callback);
        instance.setContext(ctx);

        return instance;
    }

    public static DAO getInstace(Context ctx) {
        if (instance == null) {
            instance = new DAO();
        }

        instance.setContext(ctx);

        return instance;
    }


    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Firebase Login", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) ctx, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Firebase Login", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("Firebase Login", "signInWithCredential", task.getException());
                            Toast.makeText((Activity) ctx, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    public void firebaseAuthWithFacebook(AuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) ctx, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("FaceFirebase", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("FaceFirebase", "signInWithCredential", task.getException());
                            Toast.makeText((Activity) ctx, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public FirebaseUser getFireBaseUser() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            // Name, email address, and profile photo Url
//            String name = user.getDisplayName();
//            String email = user.getEmail();
//            Uri photoUrl = user.getPhotoUrl();
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getToken() instead.
//            String uid = user.getUid();
//        }
        return user;
    }

    public void getFirstClasses(final ICallback callback) {
        DatabaseReference ref = getFirebaseInstance().getReference(Constants.FIREBASE_LOCATION_CLASS);
        Query q = ref.limitToFirst(30);

        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ClassObject classObject = dataSnapshot.getValue(ClassObject.class);
                classObject.setId(dataSnapshot.getKey());
                callback.execute(classObject);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                ClassObject classObject = dataSnapshot.getValue(ClassObject.class);
                classObject.setId(dataSnapshot.getKey());
                //adapter.update(classObject);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ClassObject classObject = dataSnapshot.getValue(ClassObject.class);
                classObject.setId(dataSnapshot.getKey());
                //adapter.remove(classObject);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadFirstClasses(final ClassAdapter adapter) {
        DatabaseReference ref = getFirebaseInstance().getReference(Constants.FIREBASE_LOCATION_CLASS);
        Query q = ref.limitToFirst(30);

        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ClassObject classObject = dataSnapshot.getValue(ClassObject.class);
                classObject.setId(dataSnapshot.getKey());
                adapter.add(classObject);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                ClassObject classObject = dataSnapshot.getValue(ClassObject.class);
                classObject.setId(dataSnapshot.getKey());
                adapter.update(classObject);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ClassObject classObject = dataSnapshot.getValue(ClassObject.class);
                classObject.setId(dataSnapshot.getKey());
                adapter.remove(classObject);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void loadFeed(final FeedAdapter adapter) {
        DatabaseReference ref = getFirebaseInstance().getReference(Constants.FIREBASE_LOCATION_USER + "/" + getFireBaseUser().getUid() + "/feed");
        Query q = ref.orderByKey();

        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final FeedItem item = dataSnapshot.getValue(FeedItem.class);
                item.id = dataSnapshot.getKey();
                try {
                    if (item.aulaID != null && !item.aulaID.isEmpty() && item.status != FeedItem.STATUS_SHOWED)
                        findClassById(item.aulaID, new ICallback<ClassObject>() {
                            @Override
                            public void execute(ClassObject param) {
                                param.setId(item.aulaID);
                                item.setAula(param);
                                System.out.println("Adiconado");
                                adapter.add(item);
                                if (ctx instanceof MainActivity)
                                    ((MainActivity) ctx).refreshFeedCount();
                            }
                        });
                } catch (Exception e) {

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                final FeedItem item = dataSnapshot.getValue(FeedItem.class);
                item.id = dataSnapshot.getKey();
                try {
                    if (item.aulaID != null && !item.aulaID.isEmpty())
                        findClassById(item.aulaID, new ICallback<ClassObject>() {
                            @Override
                            public void execute(ClassObject param) {
                                item.setAula(param);
                                adapter.update(item);
                            }
                        });
                } catch (Exception e) {

                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                final FeedItem item = dataSnapshot.getValue(FeedItem.class);
                item.id = dataSnapshot.getKey();
                try {
                    if (item.aulaID != null && !item.aulaID.isEmpty())
                        findClassById(item.aulaID, new ICallback<ClassObject>() {
                            @Override
                            public void execute(ClassObject param) {
                                item.setAula(param);
                                adapter.remove(item, -1);
                                if (ctx instanceof MainActivity)
                                    ((MainActivity) ctx).refreshFeedCount();
                            }
                        });
                } catch (Exception e) {

                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void getCurrentUser(final ICallback<User> userICallback) {

        FirebaseAuth firebaseAuth = mAuth;
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            FirebaseDatabase database = getFirebaseInstance();
            DatabaseReference myRef = database.getReference(Constants.FIREBASE_LOCATION_USER + "/" + user.getUid());

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        userICallback.execute(dataSnapshot.getValue(User.class));
                    } catch (Exception e) {
                        userICallback.execute(null);
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    userICallback.execute(null);
                }
            });
        }

    }

    @Override
    public void createUser(User user, final ICallback callback) {
        FirebaseDatabase database = getFirebaseInstance();
        DatabaseReference myRef = database.getReference(Constants.FIREBASE_LOCATION_USER + "/" + user.getUserId());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.execute(Constants.REQUEST_OK);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.execute(Constants.REQUEST_BAD);
            }
        });

        myRef.setValue(user);
      /*  Query q = myRef.orderByKey();
        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println(dataSnapshot.getValue());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        //System.out.println(myRef.);

    }

    @Override
    public void deleteUser(User user, final ICallback callback) {
        FirebaseDatabase database = getFirebaseInstance();
        DatabaseReference myRef = database.getReference(Constants.FIREBASE_LOCATION_USER + "/" + user.getUserId());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.execute(Constants.REQUEST_OK);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.execute(Constants.REQUEST_BAD);
            }
        });
        myRef.removeValue();

    }

    @Override
    public void updateUser(User user, final ICallback callback) {
        FirebaseDatabase database = getFirebaseInstance();
        DatabaseReference myRef = database.getReference(Constants.FIREBASE_LOCATION_USER + "/" + user.getUserId());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.execute(Constants.REQUEST_OK);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.execute(Constants.REQUEST_BAD);
            }
        });
        myRef.setValue(user);
    }

    @Override
    public void deleteUser(String userID, final ICallback callback) {
        FirebaseDatabase database = getFirebaseInstance();
        DatabaseReference myRef = database.getReference(Constants.FIREBASE_LOCATION_USER + "/" + userID);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.execute(Constants.REQUEST_OK);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.execute(Constants.REQUEST_BAD);
            }
        });
        myRef.removeValue();
    }

    @Override
    public void rateUser(Rating rating, User user, ICallback callback) {

    }

    @Override
    public void rateUser(Rating rating, String userID, ICallback callback) {

    }

    @Override
    public void createClass(final ClassObject classObject, final ICallback callback) {
        FirebaseDatabase database = getFirebaseInstance();
        DatabaseReference myRef = database.getReference(Constants.FIREBASE_LOCATION_CLASS);

        DatabaseReference freeRef = myRef.push();


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.execute(classObject.getId());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.execute(Constants.REQUEST_BAD);
            }
        });
        getLocationFromAddress(classObject);
        myRef.child(freeRef.getKey()).setValue(classObject);
        classObject.setId(freeRef.getKey());
    }

    public void getLocationFromAddress(ClassObject classe) {


        try {
            LatLng source = LocationHelper.getLatLng(LocationHelper.getLocationFormGoogle(classe.getAddress().trim()));

            classe.setLat(source.latitude);
            classe.setLon(source.longitude);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getLocationFromAddress(User user) {

        try {
            LatLng source = LocationHelper.getLatLng(LocationHelper.getLocationFormGoogle(user.local.trim()));
            user.lat = (source.latitude);
            user.lon = (source.longitude);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void deleteClass(ClassObject classObject, final ICallback callback) {
        FirebaseDatabase database = getFirebaseInstance();
        DatabaseReference myRef = database.getReference(Constants.FIREBASE_LOCATION_CLASS + "/" + classObject.getId());


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.execute(Constants.REQUEST_OK);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.execute(Constants.REQUEST_BAD);
            }
        });

        myRef.removeValue();

    }

    @Override
    public void updateClass(ClassObject classObject, final ICallback callback) {
        FirebaseDatabase database = getFirebaseInstance();
        DatabaseReference myRef = database.getReference(Constants.FIREBASE_LOCATION_CLASS + "/" + classObject.getId());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.execute(Constants.REQUEST_OK);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.execute(Constants.REQUEST_BAD);
            }
        });

        myRef.setValue(classObject);
    }

    @Override
    public void deleteClass(String classID, final ICallback callback) {
        FirebaseDatabase database = getFirebaseInstance();
        DatabaseReference myRef = database.getReference(Constants.FIREBASE_LOCATION_CLASS + "/" + classID);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.execute(Constants.REQUEST_OK);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.execute(Constants.REQUEST_BAD);
            }
        });

        myRef.removeValue();

    }

    @Override
    public void findClassByName(String name, final ICallback callback) {
        FirebaseDatabase database = getFirebaseInstance();
        DatabaseReference myRef = database.getReference(Constants.FIREBASE_LOCATION_CLASS);
        Query q = myRef.orderByChild("name");
        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println(dataSnapshot.getValue());
                System.out.println(((HashMap) dataSnapshot.getValue()).get("name"));
                callback.execute("nadanadanadna");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean userFirsTime() {

        FirebaseAuth firebaseAuth = mAuth;
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            FirebaseDatabase database = getFirebaseInstance();
            DatabaseReference myRef = database.getReference(Constants.FIREBASE_LOCATION_USER + "/" + user.getUid());


            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    resultado = user.getFirstTime();
                    //callback.execute(true);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        // ...
        return resultado;
    }

    public void findUserByTag(final List<String> tags, final ICallback<User> callback) {
        DatabaseReference ref = getFirebaseInstance().getReference(Constants.FIREBASE_LOCATION_USER);
        Query q = ref.orderByKey();
        q.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        User user = dataSnapshot.getValue(User.class);
                                        for (String tag : tags) {
                                            if (user.tags == null || user.tags.isEmpty()) break;
                                            for (String utag : user.tags) {
                                                if (utag.toLowerCase().equals(tag.toLowerCase())) {
                                                    callback.execute(user);
                                                    break;
                                                }

                                            }
                                        }

                                    }

                                    @Override
                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                }

        );
    }

    public void findUserWithinDistance(final Double latitude, final Double longitude, final ICallback<User> callback) {
        if (latitude != null && longitude != null) {
            DatabaseReference ref = getFirebaseInstance().getReference(Constants.FIREBASE_LOCATION_USER);
            Query q = ref.orderByKey();
            q.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                            User user = dataSnapshot.getValue(User.class);
                                            if (user.lat != null && user.lon != null) {

                                                Location startPoint = new Location("user");
                                                startPoint.setLatitude(user.getLat());
                                                startPoint.setLongitude(user.getLon());

                                                Location endPoint = new Location("class");
                                                endPoint.setLatitude(latitude);
                                                endPoint.setLongitude(longitude);

                                                int distance = (int) startPoint.distanceTo(endPoint);

                                                if (distance <= user.classRange)
                                                    callback.execute(user);

                                            }


                                        }

                                        @Override
                                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    }

            );
        }
    }

    @Override
    public List<ClassObject> findClassByTag(String tag) {
        return null;
    }

    @Override
    public void findClassById(String id, final ICallback<ClassObject> callback) {
        FirebaseDatabase database = getFirebaseInstance();
        DatabaseReference myRef = database.getReference(Constants.FIREBASE_LOCATION_CLASS + "/" + id);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ClassObject classObject = dataSnapshot.getValue(ClassObject.class);
                callback.execute(classObject);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.execute(null);
            }
        });
    }

    public void findClassByIdOnce(String id, final ICallback<ClassObject> callback) {
        FirebaseDatabase database = getFirebaseInstance();
        final DatabaseReference myRef = database.getReference(Constants.FIREBASE_LOCATION_CLASS + "/" + id);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myRef.removeEventListener(this);
                ClassObject classObject = dataSnapshot.getValue(ClassObject.class);
                callback.execute(classObject);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.execute(null);
            }
        });
    }

    @Override
    public List<ClassObject> findClassByLocation(Location loc) {
        return null;
    }


    /**
     * Retorna uma lista com os IDs das classes que o usuário passado como parâmetro está matriculado
     *
     * @param userID   - ID do Usuário
     * @param callback - Callback
     */
    @Override
    public void findClassesByUser(final String userID, final ICallback<List<String>> callback) {
        DatabaseReference refClass = FirebaseDatabase.getInstance().getReference("user-class");
        DatabaseReference newClassUser = refClass.child(userID);
        newClassUser.addValueEventListener(new ValueEventListener() {

            List<String> classes = new ArrayList<>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    classes.add(data.getKey());
                }
                callback.execute(classes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public List<ClassObject> findClassByAttendee(String userID) {
        return null;
    }

    @Override
    public void searchClass(final String anything, final ClassAdapter adapter) {
        adapter.removeAll();
        final String lower = anything.toLowerCase();
        DatabaseReference ref = getFirebaseInstance().getReference(Constants.FIREBASE_LOCATION_CLASS);
        Query q = ref.orderByKey();


        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    if (adapter.getItemCount() < Constants.MAX_ITEM_COUNT && ((String) dataSnapshot.child("name").getValue()).toLowerCase().contains(lower)) {
                        ClassObject classObject = dataSnapshot.getValue(ClassObject.class);
                        classObject.setId(dataSnapshot.getKey());
                        if (ctx instanceof SearchActivity)
                            adapter.add(classObject);
                    }
                } catch (NullPointerException e) {

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (adapter.getItemCount() < Constants.MAX_ITEM_COUNT && ((String) dataSnapshot.child("name").getValue()).toLowerCase().contains(lower)) {
                    ClassObject classObject = dataSnapshot.getValue(ClassObject.class);
                    classObject.setId(dataSnapshot.getKey());
                    if (ctx instanceof SearchActivity)
                        adapter.update(classObject);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (adapter.getItemCount() < Constants.MAX_ITEM_COUNT && ((String) dataSnapshot.child("name").getValue()).toLowerCase().contains(lower)) {
                    ClassObject classObject = dataSnapshot.getValue(ClassObject.class);
                    classObject.setId(dataSnapshot.getKey());
                    if (ctx instanceof SearchActivity)
                        adapter.remove(classObject);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void searchUser(final String anything, final UserAdapter adapter) {
        adapter.removeAll();
        final String lower = anything.toLowerCase();
        DatabaseReference ref = getFirebaseInstance().getReference(Constants.FIREBASE_LOCATION_USER);
        Query q = ref.orderByKey();


        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    if (adapter.getItemCount() < Constants.MAX_ITEM_COUNT && ((String) dataSnapshot.child("name").getValue()).toLowerCase().contains(lower)) {
                        User user = dataSnapshot.getValue(User.class);
                        adapter.add(user);
                    }
                } catch (NullPointerException e) {

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (adapter.getItemCount() < Constants.MAX_ITEM_COUNT && ((String) dataSnapshot.child("name").getValue()).toLowerCase().contains(lower)) {
                    User user = dataSnapshot.getValue(User.class);
                    adapter.update(user);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (adapter.getItemCount() < Constants.MAX_ITEM_COUNT && ((String) dataSnapshot.child("name").getValue()).toLowerCase().contains(lower)) {
                    User user = dataSnapshot.getValue(User.class);
                    adapter.remove(user);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void findUserById(String id, final ICallback callback) {
        FirebaseDatabase database = getFirebaseInstance();
        DatabaseReference myRef = database.getReference(Constants.FIREBASE_LOCATION_USER + "/" + id);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                try {
                    callback.execute(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.execute(null);
            }
        });
    }


    public void findUserByIdOnce(String id, final ICallback callback) {
        FirebaseDatabase database = getFirebaseInstance();
        DatabaseReference myRef = database.getReference(Constants.FIREBASE_LOCATION_USER + "/" + id);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                try {
                    callback.execute(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.execute(null);
            }
        });
    }


    @Override
    public void loadFirstTeachers(final UserAdapter adapter) {
        DatabaseReference ref = getFirebaseInstance().getReference(Constants.FIREBASE_LOCATION_USER);
        Query q = ref.limitToFirst(30);

        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                adapter.add(user);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                adapter.update(user);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                adapter.remove(user);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void countVagaOcupadasClass(String classId, final ICallback<Long> callback) {
        DatabaseReference refClass = FirebaseDatabase.getInstance().getReference("class-user");
        DatabaseReference newClassUser = refClass.child(classId);
        newClassUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.execute(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    public void avaliarProfessor(final String idProfessor, String idAluno, final Double nota, User professor, final ICallback<Double> callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("professor-aluno");
        DatabaseReference newUserClass = ref.child(idProfessor);
        newUserClass.child(idAluno).setValue(nota);
        findUserByIdOnce(idProfessor, new ICallback() {
            @Override
            public void execute(Object param) {
                User prof = (User) param;
                if (prof.getUserId() == null) {
                    prof.userId = idProfessor;
                }
                if (prof.getNotas() == null) {
                    prof.setNotas(new ArrayList<Double>());
                }
                prof.getNotas().add(nota);
                updateUser(prof, new ICallback() {
                    @Override
                    public void execute(Object param) {
                        callback.execute(nota);
                    }
                });
            }
        });
    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    public void setCallback(ICallback<Boolean> callback) {
        this.callback = callback;
    }

    public void addAuthStateListener() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void removeAuthStateListener() {
        mAuth.removeAuthStateListener(mAuthListener);
    }

    public Object getAuthListener() {
        return mAuthListener;
    }

    public void setContext(Context context) {
        this.ctx = context;
    }


    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
    }

    public FirebaseDatabase getFirebaseInstance() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
            mDatabase.setLogLevel(Logger.Level.NONE);
        }

        return mDatabase;
    }
}
