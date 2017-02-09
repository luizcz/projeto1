package projetoum.equipe.iteach.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.DAO;

public class PerfilActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    private DAO dao;
    User usuarioAtual;
    private PerfilActivity mContext;
    private EditText edtNome;
    private EditText edtTelefone;
    private EditText edtLocal;
    private EditText edtBio;
    private Button btProximo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_perfil);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.transparent));
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);



        mContext = this;

        edtNome = (EditText) findViewById(R.id.edt_nome);
        edtTelefone = (EditText) findViewById(R.id.edt_telefone);
        edtLocal = (EditText) findViewById(R.id.edt_local);
        edtBio = (EditText) findViewById(R.id.edt_bio);


        dao = DAO.getInstace(mContext);



        ((TextView)header.findViewById(R.id.label_name)).setText(dao.getFireBaseUser().getDisplayName());
        ((TextView)header.findViewById(R.id.label_email)).setText(dao.getFireBaseUser().getEmail());
        Picasso.with(getBaseContext()).load(dao.getFireBaseUser().getPhotoUrl()).into(((ImageView)header.findViewById(R.id.img)));

        dao.getCurrentUser(new ICallback<User>() {
            @Override
            public void execute(User param) {
                usuarioAtual = param;
                if(usuarioAtual.getName() != null){
                    edtNome.setText(usuarioAtual.getName());
                }
                if(usuarioAtual.getTelefone() != null){
                    edtTelefone.setText(usuarioAtual.getTelefone());
                }
                if(usuarioAtual.getLocal() != null){
                    edtLocal.setText(usuarioAtual.getLocal());
                }
                if(usuarioAtual.getBio() != null){
                    edtBio.setText(usuarioAtual.getBio());
                }
            }
        });

        btProximo = (Button) findViewById(R.id.bt_salvar_perfil);
        btProximo.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bt_salvar_perfil){
            carregarDadosUsuario();
            usuarioAtual.setFirstTime(false);
            dao.updateUser(usuarioAtual, new ICallback() {
                @Override
                public void execute(Object param) {
                    startActivity(new Intent(PerfilActivity.this , MainActivity.class));
                    finish();
                }
            });
        }
    }

    private void carregarDadosUsuario(){
        if(edtNome.getText() != null){
            usuarioAtual.setName(edtNome.getText().toString());
        }
        if(edtTelefone.getText() != null){
            usuarioAtual.setTelefone(edtTelefone.getText().toString());
        }
        if(edtLocal.getText() != null){
            usuarioAtual.setLocal(edtLocal.getText().toString());
        }
        if(edtBio.getText() != null){
            usuarioAtual.setBio(edtBio.getText().toString());
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_feed) {
            startActivity(new Intent(this,MainActivity.class));
        } else if (id == R.id.nav_profile) {
            //startActivity(new Intent(this,PerfilActivity.class));

        } else if (id == R.id.nav_my_class) {
            //startActivity(new Intent(this,CourseActivity.class));

        } else if (id == R.id.nav_options) {
            // startActivity(new Intent(this,OptionsActivity.class));

        } else if (id == R.id.nav_class) {
            startActivity(new Intent(this,SearchActivity.class));

        } else if (id == R.id.nav_teacher) {
            startActivity(new Intent(this,SearchActivity.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
