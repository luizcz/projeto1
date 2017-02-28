package projetoum.equipe.iteach.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
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

import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.DAO;

public class PreferenciasActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_options);

        View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);

        dao = DAO.getInstace(this);


        ((TextView) header.findViewById(R.id.label_name)).setText(dao.getFireBaseUser().getDisplayName());
        ((TextView) header.findViewById(R.id.label_email)).setText(dao.getFireBaseUser().getEmail());
        Picasso.with(getBaseContext()).load(dao.getFireBaseUser().getPhotoUrl()).into(((ImageView) header.findViewById(R.id.card_aula_img)));


    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_feed) {
            finish();
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, PerfilActivity.class));

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
}
