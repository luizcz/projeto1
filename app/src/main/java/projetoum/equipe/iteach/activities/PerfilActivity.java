package projetoum.equipe.iteach.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.User;

public class PerfilActivity extends DrawerActivity {

    private GoogleApiClient mGoogleApiClient;
    private TextView name, local, bio;
    private ImageView img;
    private ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        init(R.id.nav_profile);

        spinner = (ProgressBar) findViewById(R.id.progressBar3);

        name = (TextView) findViewById(R.id.label_name);
        local = ((TextView) findViewById(R.id.label_local));
        bio = ((TextView) findViewById(R.id.label_info));
        img = (ImageView) findViewById(R.id.card_aula_img);


        Typeface giz = Typeface.createFromAsset(getAssets(), "font/giz.ttf");

        name.setTypeface(giz);

        if (getIntent().hasExtra("id")) {


            dao.findUserById(getIntent().getStringExtra("id"), new ICallback<User>() {
                @Override
                public void execute(User param) {
                    name.setText(pattern(param.name));
                    local.setText("Endereço: " + param.getLocal());
                    bio.setText("Bio: " + param.getBio());
                    if (param.highResURI != null && !param.highResURI.isEmpty())
                        Picasso.with(getBaseContext()).load(param.highResURI).fit().centerCrop().into((ImageView) findViewById(R.id.card_aula_img));
                    else
                        Picasso.with(getBaseContext()).load(param.getLowResURI()).fit().centerCrop().into((ImageView) findViewById(R.id.card_aula_img));
                    spinner.setVisibility(View.GONE);
                }
            });

        } else {
            dao.getCurrentUser(new ICallback<User>() {
                @Override
                public void execute(User param) {
                    name.setText(pattern(param.name));
                    local.setText("Endereço: " + param.getLocal());
                    bio.setText("Bio: " + param.getBio());
                    if (param.highResURI != null && !param.highResURI.isEmpty())
                        Picasso.with(getBaseContext()).load(param.highResURI).fit().centerCrop().into((ImageView) findViewById(R.id.card_aula_img));
                    else
                        Picasso.with(getBaseContext()).load(param.getLowResURI()).fit().centerCrop().into((ImageView) findViewById(R.id.card_aula_img));
                    spinner.setVisibility(View.GONE);
                }
            });
        }
    }

    private String pattern(String displayName) {
        String pattern = "";
        String[] split = displayName.split(" ");
        pattern += " " + split[0];
        if (split.length > 1 && pattern.length() < 16) {
            if (pattern.length() + split[1].length() <= 16) {
                pattern += " " + split[split.length - 1];
            } else {
                pattern += " " + split[split.length - 1].substring(0, 1) + ".";
            }
        }
        return pattern;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil, menu);

        if (getIntent().getStringExtra("id") != null && !getIntent().getStringExtra("id").equals(dao.getFireBaseUser().getUid())) {
            menu.findItem(R.id.edit).setVisible(false);
        }


        return true;
    }


    public void action(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                startActivity(new Intent(PerfilActivity.this, CadastroActivity.class));
                break;
        }
    }

}
