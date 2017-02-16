package projetoum.equipe.iteach.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.DAO;

public class PerfilActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DAO dao;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_profile);

        View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);

        dao = DAO.getInstace(this);


        ((TextView) header.findViewById(R.id.label_name)).setText(dao.getFireBaseUser().getDisplayName());
        ((TextView) header.findViewById(R.id.label_email)).setText(dao.getFireBaseUser().getEmail());
        Picasso.with(getBaseContext()).load(dao.getFireBaseUser().getPhotoUrl()).into(((ImageView) header.findViewById(R.id.card_aula_img)));


        TextView name = (TextView) findViewById(R.id.label_name);

        Typeface giz = Typeface.createFromAsset(getAssets(), "font/giz.ttf");

        name.setTypeface(giz);
        name.setText(pattern(dao.getFireBaseUser().getDisplayName()));

        dao.getCurrentUser(new ICallback<User>() {
            @Override
            public void execute(User param) {
                User usuarioAtual = param;
                /*if(usuarioAtual.getRating() != null){
                    ((RatingBar)findViewById(R.id.ratingBar)).setRating(usuarioAtual.getRating().getGrade());
                    usuarioAtual.getRating()
                    edtNome.setText(usuarioAtual.getName());
                }*/
                if (usuarioAtual.getLocal() != null) {
                    ((TextView) findViewById(R.id.label_local)).setText(usuarioAtual.getLocal());
                }
                if (usuarioAtual.getBio() != null) {
                    ((TextView) findViewById(R.id.label_info)).setText(usuarioAtual.getBio());
                }
            }
        });


        for (UserInfo profile : dao.getFireBaseUser().getProviderData()) {
            String firstProvider = profile.getProviderId();


            if (firstProvider.equals("facebook.com")) {

                String facebookUserId = profile.getUid();

                String photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=500";
                System.out.println(photoUrl);
                Picasso.with(getBaseContext()).load(photoUrl).fit().centerCrop().into((ImageView) findViewById(R.id.card_aula_img));


            } else if (firstProvider.equals("google.com")) {


                loadGoogleUserDetails();
            }
        }

    }

    private String pattern(String displayName) {
        String pattern = "";
        String[] split = displayName.split(" ");
        pattern += " "+ split[0];
        if (split.length > 1 && pattern.length() < 16) {
            if (pattern.length() + split[1].length() <= 16) {
                pattern += " "+ split[split.length - 1];
            } else {
                pattern += " "+ split[split.length - 1].substring(0, 1) + ".";
            }
        }
        return pattern;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil, menu);

        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_feed) {
            finish();
        } else if (id == R.id.nav_profile) {
            //startActivity(new Intent(this, PerfilActivity.class));

        } else if (id == R.id.nav_my_class) {
            //startActivity(new Intent(this,CourseActivity.class));

        } else if (id == R.id.nav_options) {
            // startActivity(new Intent(this,OptionsActivity.class));

        } else if (id == R.id.nav_class) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("busca", "aula");
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_teacher) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("busca", "user");
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_logout) {
            dao.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                GoogleSignInAccount acct = result.getSignInAccount();
                // Get account information
                String photoUrl = acct.getPhotoUrl().toString();

                Picasso.with(getBaseContext()).load(photoUrl).fit().centerCrop().into((ImageView) findViewById(R.id.card_aula_img));


            }
        }
    }

    public void action(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                startActivity(new Intent(PerfilActivity.this, CadastroActivity.class));
                break;
        }
    }

}
