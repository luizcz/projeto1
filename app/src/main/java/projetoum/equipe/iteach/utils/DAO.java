package projetoum.equipe.iteach.utils;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Random;

import projetoum.equipe.iteach.activities.MainActivity;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.interfaces.IRemote;
import projetoum.equipe.iteach.models.ClassObject;
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

    private DAO() {
        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("FireBase", "onAuthStateChanged:signed_in:" + user.getUid());
                    callback.execute(true);


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

    public void firebaseAuthWithFacebook(AuthCredential credential){
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


    public void getFeed(final ICallback<String> feedUpdate) {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Fire base event update", "Value is: " + value);
                feedUpdate.execute(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Fire base event update", "Failed to read value.", error.toException());
                feedUpdate.execute("Failed to get the feed");
            }
        });


    }



    public void fillFeed() {
        if(mAuth.getCurrentUser() != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("message");
            myRef.setValue("Feed " + new Random().nextInt(999));
        }
    }


    @Override
    public void createUser(User user, final ICallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(user.getUserId());
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
    public void deleteUser(User user, final ICallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(user.getUserId());
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
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(user.getUserId());
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
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(userID);
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
    }
}
