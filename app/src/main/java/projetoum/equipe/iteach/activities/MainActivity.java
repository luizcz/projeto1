package projetoum.equipe.iteach.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.DAO;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private DAO dao;
    private ICallback<Boolean> updateUI;
    private CallbackManager mCallbackManager;
    private NavigationView navigationView;
    private String googleHighResPhotoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_feed);

        FacebookSdk.sdkInitialize(getApplicationContext());
        updateUI = new MainActivity.UpdateUI();
        dao = DAO.getInstace(updateUI, this);


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_feed) {
            //startActivity(new Intent(this,MainActivity.class));
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, PerfilActivity.class).putExtra("id", dao.getFireBaseUser().getUid()));

        } else if (id == R.id.nav_my_class) {
            //startActivity(new Intent(this,CourseActivity.class));

        } else if (id == R.id.nav_options) {
            // startActivity(new Intent(this,OptionsActivity.class));

        } else if (id == R.id.nav_class) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("busca", "aula");
            startActivity(intent);

        } else if (id == R.id.nav_teacher) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("busca", "user");
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            dao.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onStart() {
        super.onStart();
        dao.addAuthStateListener();
    }

    @Override
    protected void onResume() {
        if (navigationView != null)
            navigationView.setCheckedItem(R.id.nav_feed);
        super.onResume();
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
            case R.id.floatingActionButton:
                startActivity(new Intent(this, CadastroAulaActivity.class));
                break;
          /*  case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_and_disconnect:
                signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.put:
                dao.fillFeed();
            case R.id.bt_edt_perfil:
                startActivity(new Intent(this, CadastroActivity.class));
                finish();
                break;*/
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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


                View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);

                ((TextView) header.findViewById(R.id.label_name)).setText(dao.getFireBaseUser().getDisplayName());
                ((TextView) header.findViewById(R.id.label_email)).setText(dao.getFireBaseUser().getEmail());
                Picasso.with(getBaseContext()).load(dao.getFireBaseUser().getPhotoUrl()).into(((ImageView) header.findViewById(R.id.card_aula_img)));
                dao.getCurrentUser(new ICallback<User>() {
                    @Override
                    public void execute(User param) {
                        User update = param;
                        update.setLowResURI(dao.getFireBaseUser().getPhotoUrl().toString());


                        for (UserInfo profile : dao.getFireBaseUser().getProviderData()) {
                            String firstProvider = profile.getProviderId();


                            if (firstProvider.equals("facebook.com")) {
                                String facebookUserId = profile.getUid();
                                String photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=500";
                                update.highResURI = photoUrl;
                                dao.updateUser(param, new ICallback() {
                                    @Override
                                    public void execute(Object param) {

                                    }
                                });
                            } else if (firstProvider.equals("google.com")) {
                                loadGoogleUserDetails();
                                /*if (googleHighResPhotoUrl != null && !googleHighResPhotoUrl.isEmpty()) {
                                    update.setHighResURI(googleHighResPhotoUrl);
                                }*/
                            }
                        }


                    }
                });

            } else {
            }
        }
    }

    private static final int RC_SIGN_IN = 8888;

    public void loadGoogleUserDetails() {
        try {
            // Configure sign-in to request the user's ID, email address, and basic profile. ID and
            // basic profile are included in DEFAULT_SIGN_IN.
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            System.out.println("onConnectionFailed");
                        }
                    })
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from
        //   GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                final GoogleSignInAccount acct = result.getSignInAccount();
                // Get account information
                
                dao.getCurrentUser(new ICallback<User>() {
                    @Override
                    public void execute(User param) {
                        param.highResURI = acct.getPhotoUrl().toString();
                        dao.updateUser(param, new ICallback() {
                            @Override
                            public void execute(Object param) {

                            }
                        });

                    }
                });

            }
        }
    }

}