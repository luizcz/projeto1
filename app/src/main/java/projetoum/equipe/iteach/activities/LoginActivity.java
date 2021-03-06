package projetoum.equipe.iteach.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.DAO;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    public static final int RC_SIGN_IN = 0;

    private GoogleApiClient mGoogleApiClient;
    private DAO dao;
    private ICallback<Boolean> updateUI;
    private LoginActivity mContext;
    private CallbackManager mCallbackManager;
    private LoginButton loginButton;
    private View btGoogle;
    private View btFb;
    private View signinLabel;
    private View progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        updateUI = new UpdateUI();
        dao = DAO.getInstace(updateUI, this);
        mContext = this;

        btGoogle = findViewById(R.id.bt_login_google);
        btGoogle.setOnClickListener(this);
        btGoogle.setVisibility(View.INVISIBLE);

        btFb = findViewById(R.id.bt_login_face);
        btFb.setOnClickListener(this);
        btFb.setVisibility(View.INVISIBLE);

        signinLabel = findViewById(R.id.signin_label);
        signinLabel.setVisibility(View.INVISIBLE);

        progress = findViewById(R.id.progressBar2);


        mCallbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Facebook", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                dao.getCurrentUser(new ICallback<User>() {
                    @Override
                    public void execute(User param) {
                        if (param.getFirstTime() == null || param.getFirstTime()) {
                            startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
                        } else
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                });
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
            case R.id.bt_login_google:
                signIn();
                break;
            case R.id.bt_login_face:
                loginButton.performClick();
                break;
        }
    }

    //Google methods start here
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

        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("Login Google", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            final GoogleSignInAccount acct = result.getSignInAccount();
            //Toast.makeText(this, acct.getDisplayName(), Toast.LENGTH_SHORT).show();
            Log.i("Login", acct.getDisplayName());
            dao.firebaseAuthWithGoogle(acct);
            dao.getCurrentUser(new ICallback<User>() {
                @Override
                public void execute(User param) {
                    param.highResURI = acct.getPhotoUrl().toString();
                    dao.updateUser(param, new ICallback() {
                        @Override
                        public void execute(Object param) {

                        }
                    });
                    if (param.getFirstTime() == null || param.getFirstTime()) {
                        startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
                    } else
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            });


        } else {
            // Toast.makeText(this, "login fail", Toast.LENGTH_SHORT).show();
            Log.i("Login", "login fail");

        }
    }


    //    private void signOut() {
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        updateUI.execute(false);
//                        if (dao.getAuthListener() != null) {
//                            dao.signOut();
//                            // dao.removeAuthStateListener();
//
//                        }
//                    }
//                });
//    }

    //Facebook methods start here
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("FaceFirebase", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        dao.firebaseAuthWithFacebook(credential);
    }


//    public void disconnectFromFacebook() {
//
//        if (AccessToken.getCurrentAccessToken() == null) {
//            return; // already logged out
//        }
//
//        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
//                .Callback() {
//            @Override
//            public void onCompleted(GraphResponse graphResponse) {
//
//                LoginManager.getInstance().logOut();
//
//            }
//        }).executeAsync();
//    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "connection failed", Toast.LENGTH_SHORT).show();
    }


    public class UpdateUI implements ICallback<Boolean> {

        @Override
        public void execute(Boolean param) {


            if (param) {
                Log.i("Login", "status: signed_in");
                dao.getCurrentUser(new ICallback<User>() {
                    @Override
                    public void execute(User param) {
                        if (param == null) {
                            dao.signOut();
                            showLoginBt(1);
                        } else {
                            if (param.getFirstTime() == null || param.getFirstTime()) {
                                startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
                            } else
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
            } else {
                showLoginBt(2);
            }
        }

        private void showLoginBt(int qt) {
            Log.i("Login", "status: signed_out");
            if (qt == 2)
                btGoogle.setVisibility(View.VISIBLE);
            btFb.setVisibility(View.VISIBLE);
            signinLabel.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        }
    }
}
