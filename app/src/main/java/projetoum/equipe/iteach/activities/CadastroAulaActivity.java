package projetoum.equipe.iteach.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.utils.DAO;

public class CadastroAulaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    private DAO dao;
    private EditText tituloEd;
    private EditText numVagasEd;
    private EditText valorEd;
    private EditText dataEd;
    private EditText horarioInicioEd;
    private EditText horarioFimEd;
    private EditText assuntoEd;
    private EditText tagsEd;
    private ImageView imagePropaganda;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_aula);

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

        dao = DAO.getInstace(this);



        ((TextView)header.findViewById(R.id.label_name)).setText(dao.getFireBaseUser().getDisplayName());
        ((TextView)header.findViewById(R.id.label_email)).setText(dao.getFireBaseUser().getEmail());
        Picasso.with(getBaseContext()).load(dao.getFireBaseUser().getPhotoUrl()).into(((ImageView)header.findViewById(R.id.img)));

        tituloEd = (EditText) findViewById(R.id.edt_titulo);
        numVagasEd = (EditText) findViewById(R.id.edt_num_vagas);
        valorEd = (EditText) findViewById(R.id.edt_valor);
        dataEd = (EditText) findViewById(R.id.edt_date);
        horarioInicioEd = (EditText) findViewById(R.id.edt_horario_inicio);
        horarioFimEd = (EditText) findViewById(R.id.edt_horario_fim);
        assuntoEd = (EditText) findViewById(R.id.edt_assunto);
        tagsEd = (EditText) findViewById(R.id.edt_tags);
        imagePropaganda = (ImageView) findViewById(R.id.img_propaganda);

        imagePropaganda.setOnClickListener(this);
        findViewById(R.id.bt_salvar_aula).setOnClickListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_feed) {
            startActivity(new Intent(this,MainActivity.class));
        } else if (id == R.id.nav_profile) {
            //startActivity(new Intent(this,CadastroActivity.class));

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        case R.id.bt_salvar_aula:
            break;
        }
    }
}
