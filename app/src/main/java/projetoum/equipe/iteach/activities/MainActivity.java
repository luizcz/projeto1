package projetoum.equipe.iteach.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
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
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.adapter.FeedAdapter;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.ClassObject;
import projetoum.equipe.iteach.models.FeedItem;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.DAO;

public class MainActivity extends DrawerActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private ICallback<Boolean> updateUI;
    public static Location mLastLocation;
    private RecyclerView recycler;
    private View feedEmptyText, feedEmptyImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init(R.id.nav_feed);

        feedEmptyImg = findViewById(R.id.feed_empty_img);
        feedEmptyText = findViewById(R.id.feed_empty_label);

        recycler = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(mLayoutManager);
        recycler.setAdapter(new FeedAdapter(this, new ArrayList<FeedItem>()));


        ItemTouchHelper mIth = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        final int fromPos = viewHolder.getAdapterPosition();
                        final int toPos = target.getAdapterPosition();
                        // move item in `fromPos` to `toPos` in adapter.
                        return true;// true if moved, false otherwise
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        ((FeedAdapter)recycler.getAdapter()).removeAt(viewHolder.getAdapterPosition());
                    }
                });

        mIth.attachToRecyclerView(recycler);



        dao.loadFeed((FeedAdapter) recycler.getAdapter());

        FacebookSdk.sdkInitialize(getApplicationContext());
        updateUI = new MainActivity.UpdateUI();
        dao = DAO.getInstace(updateUI, this);

        buildGoogleApiClient();
        installShortCut();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            showExitDialog();
        }
    }

    private void showExitDialog() {
        AlertDialog exitDialog = new AlertDialog.Builder(MainActivity.this,
                R.style.MyDialogTheme).create();
        exitDialog.setTitle(R.string.sair);
        exitDialog.setMessage(getString(R.string.deseja_sair));
        exitDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        exitDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        exitDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        dao.addAuthStateListener();
        mGoogleApiClient.connect();
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
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void installShortCut(){
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean isAppInstalled = appPreferences.getBoolean("isAppInstalled",false);

        if(!isAppInstalled){

            Intent shortcutIntent = new Intent(getApplicationContext(),LoginActivity.class);
            shortcutIntent.setAction(Intent.ACTION_MAIN);
            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "SocialTeach");
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,Intent.ShortcutIconResource.
                    fromContext(getApplicationContext(), R.drawable.ic_launcher));
            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

            getApplicationContext().sendBroadcast(intent);
            SharedPreferences.Editor editor = appPreferences.edit();
            editor.putBoolean("isAppInstalled", true);
            editor.apply();
        }
    }

    public void disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null,
                HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
            }
        }).executeAsync();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void refreshFeedCount() {
        if (recycler.getAdapter().getItemCount() <= 0) {
            feedEmptyImg.setVisibility(View.VISIBLE);
            feedEmptyText.setVisibility(View.VISIBLE);
        }
        if (recycler.getAdapter().getItemCount() >= 0) {
            feedEmptyImg.setVisibility(View.GONE);
            feedEmptyText.setVisibility(View.GONE);
        }
    }

    private class UpdateUI implements ICallback<Boolean> {
        @Override
        public void execute(Boolean param) {
            if (param) {


                View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);

                ((TextView) header.findViewById(R.id.label_name)).setText(dao.getFireBaseUser().
                        getDisplayName());
                ((TextView) header.findViewById(R.id.label_email)).setText(dao.getFireBaseUser().
                        getEmail());
                Picasso.with(getBaseContext()).load(dao.getFireBaseUser().getPhotoUrl()).into(
                        (ImageView) header.findViewById(R.id.card_aula_img));
                dao.getCurrentUser(new ICallback<User>() {
                    @Override
                    public void execute(User user) {
                        user.setLowResURI(dao.getFireBaseUser().getPhotoUrl().toString());


                        for (UserInfo profile : dao.getFireBaseUser().getProviderData()) {
                            String firstProvider = profile.getProviderId();


                            if (firstProvider.equals("facebook.com")) {
                                String facebookUserId = profile.getUid();
                                user.highResURI = "https://graph.facebook.com/" + facebookUserId
                                        + "/picture?height=500";
                                dao.updateUser(user, new ICallback() {
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

            }
        }
    }

    private static final int RC_SIGN_IN = 8888;

    public void loadGoogleUserDetails() {
        try {
            // Configure sign-in to request the user's ID, email address, and basic profile. ID and
            // basic profile are included in DEFAULT_SIGN_IN.
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.
                    DEFAULT_SIGN_IN).requestEmail().build();

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
                        if (acct.getPhotoUrl() != null) {
                            param.highResURI = acct.getPhotoUrl().toString();
                            dao.updateUser(param, new ICallback() {
                                @Override
                                public void execute(Object param) {

                                }
                            });
                        }
                    }
                });
            }
        }

    }


}