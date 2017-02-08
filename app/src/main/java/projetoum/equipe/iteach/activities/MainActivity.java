package projetoum.equipe.iteach.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.ClassObject;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.Constants;
import projetoum.equipe.iteach.utils.DAO;
import projetoum.equipe.iteach.activities.SearchActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 0;

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private TextView feed;
    private DAO dao;
    private ICallback<Boolean> updateUI;
    private CallbackManager mCallbackManager;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        updateUI = new UpdateUI();
        dao = DAO.getInstace(updateUI, this);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_and_disconnect).setOnClickListener(this);
        findViewById(R.id.put).setOnClickListener(this);
        findViewById(R.id.put_user).setOnClickListener(this);
        findViewById(R.id.search_button).setOnClickListener(this);
        findViewById(R.id.bt_edt_perfil).setOnClickListener(this);

        mStatusTextView = (TextView) findViewById(R.id.txt);
        feed = (TextView) findViewById(R.id.feed);

        mCallbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Facebook", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("Facebook", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Facebook", "facebook:onError", error);
                // ...
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        // Build a GoogleApiClient with access to the Google Sign-In API and the
// options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


    }


    @Override
    public void onStart() {
        super.onStart();
        dao.addAuthStateListener();
    }


    @Override
    public void onStop() {
        super.onStop();
        if (dao.getAuthListener() != null) {
            dao.removeAuthStateListener();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_and_disconnect:
                signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.put:
                dao.fillFeed();
            case R.id.put_user:
                dao.findClassByName("Aula de ingles I", new ICallback<String>() {
                    @Override
                    public void execute(String param) {
                    }
                });

                break;
            case R.id.search_button:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.bt_edt_perfil:
                startActivity(new Intent(this, PerfilActivity.class));
                finish();
                break;
        }
    }




    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("Login Google", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            mStatusTextView.setText(acct.getDisplayName());
            dao.firebaseAuthWithGoogle(acct);
            //updateUI.execute(true);
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI.execute(false);
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("FaceFirebase", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        dao.firebaseAuthWithFacebook(credential);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI.execute(false);
                        if (dao.getAuthListener() != null) {
                            dao.signOut();
                            disconnectFromFacebook();
                            // dao.removeAuthStateListener();

                        }
                    }
                });
    }

    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }


    public class UpdateUI implements ICallback<Boolean> {

        @Override
        public void execute(Boolean param) {


            if (param) {
                findViewById(R.id.sign_in_button).setVisibility(View.GONE);
                findViewById(R.id.login_button).setVisibility(View.GONE);
                findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
                mStatusTextView.setText(dao.getFireBaseUser().getDisplayName());
                dao.getFeed(new ICallback<String>() {
                    @Override
                    public void execute(String param) {
                        feed.setText(param);
                    }
                });
            } else {
                mStatusTextView.setText("signed_out");

                findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
                findViewById(R.id.login_button).setVisibility(View.VISIBLE);
                findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
            }
        }
    }

    ;
}
