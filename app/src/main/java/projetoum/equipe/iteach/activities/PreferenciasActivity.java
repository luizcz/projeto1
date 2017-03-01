package projetoum.equipe.iteach.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.adapter.TagAdapter;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.Constants;

public class PreferenciasActivity extends DrawerActivity implements GoogleApiClient.OnConnectionFailedListener {

    private TextView labelEmailG, labelEmailF, labelDistance, labelMajorMethod;
    private Switch switchg, switchf;
    private SeekBar seekBar;
    private RecyclerView recycler;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);

        init(R.id.nav_options);


        labelEmailG = (TextView) findViewById(R.id.label_email_google);
        labelEmailF = (TextView) findViewById(R.id.label_email_facebook);
        labelDistance = (TextView) findViewById(R.id.label_distance);

        labelMajorMethod = (TextView) findViewById(R.id.label_major_method);
        labelMajorMethod.setOnClickListener(onClick);
        labelMajorMethod.setText("Pagamento indisponível no momento");

        recycler = (RecyclerView) findViewById(R.id.recycler);

        List<String> rowListItem = new ArrayList<String>(Arrays.asList("Ingles","Portugues","Espanhol","StarCraftII","Voley"));
        GridLayoutManager layoutManager = new GridLayoutManager(PreferenciasActivity.this, 3);

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(new TagAdapter(this,rowListItem));


        findViewById(R.id.bt_add_payment).setOnClickListener(onClick);

        //seekbar
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(Constants.MAX_DISTANCE_METERS);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >= Constants.KM_IN_METERS)
                    labelDistance.setText((progress / Constants.KM_IN_METERS) + "Km");
                else
                    labelDistance.setText(progress + "m");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //switch
        switchg = (Switch) findViewById(R.id.switchg);
        switchf = (Switch) findViewById(R.id.switchf);
        switchg.setOnCheckedChangeListener(checkListner);
        switchf.setOnCheckedChangeListener(checkListner);


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

        mCallbackManager = CallbackManager.Factory.create();


        for (UserInfo profile : dao.getFireBaseUser().getProviderData()) {
            String firstProvider = profile.getProviderId();


            if (firstProvider.equals("facebook.com")) {
                switchf.setActivated(true);
                labelEmailF.setText(profile.getEmail());
                System.out.println("facebook: " + profile.getEmail());

            } else if (firstProvider.equals("google.com")) {
                switchg.setActivated(true);
                labelEmailG.setText(profile.getEmail());
                System.out.println("google: " + profile.getEmail());
            }
        }


        if (!switchg.isActivated()) {
            switchf.setClickable(false);
            labelEmailG.setText("");
        }
        if (!switchf.isActivated()) {
            switchg.setClickable(false);
            labelEmailF.setText("");
        }


    }


    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_add_payment:
                    break;
                case R.id.label_major_method:
                    break;
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener checkListner = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.switchg:
                    if (isChecked) {
                        signIn();

                    } else {
                        dao.getAuth().getCurrentUser().unlink(dao.getFireBaseUser().getProviderId())
                                .addOnCompleteListener(PreferenciasActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            // Auth provider unlinked from account
                                        } else {

                                            switchf.setClickable(false);
                                        }
                                    }
                                });
                    }
                    break;
                case R.id.switchf:
                   /* if (isChecked) {
                        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
                    }*/
                    break;

            }
        }
    };


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, LoginActivity.RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == LoginActivity.RC_SIGN_IN) {
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

            final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            dao.getAuth().getCurrentUser().linkWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("Preferencias", "linkWithCredential:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(PreferenciasActivity.this, "Usuário já existe, as duas contas serão unidas.",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser prevUser = dao.getFireBaseUser();
                                dao.getAuth().signInWithCredential(credential);
                                // Merge prevUser and currentUser accounts and data
                                // ...
                                switchf.setClickable(true);

                            }

                            // ...
                        }
                    });

        } else {
            // Toast.makeText(this, "login fail", Toast.LENGTH_SHORT).show();
            Log.i("Login", "login fail");

        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
