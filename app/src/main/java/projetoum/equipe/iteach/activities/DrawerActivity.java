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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.utils.DAO;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    protected DAO dao;
    private int navId = 0;
    NavigationView navigationView;
    protected Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_feed && id != navId) {
            startActivity(new Intent(this, MainActivity.class));
            finish();

        } else if (id == R.id.nav_profile && id != navId) {
            startActivity(new Intent(this, PerfilActivity.class));
            finish();

        } else if (id == R.id.nav_my_class && id != navId) {
            startActivity(new Intent(this,MinhasAulasActivity.class));
            finish();

        } else if (id == R.id.nav_options && id != navId) {
            startActivity(new Intent(this,PreferenciasActivity.class));
            finish();

        } else if (id == R.id.nav_class && id != navId) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("busca", "aula");
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_teacher && id != navId) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("busca", "user");
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_about && id != navId) {
            Intent intent = new Intent(this, SobreActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_logout && id != navId) {
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void init(int navId){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(navId);
        this.navId = navId;

        View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);

        dao = DAO.getInstace(this);

        ((TextView) header.findViewById(R.id.label_name)).setText(dao.getFireBaseUser().getDisplayName());
        ((TextView) header.findViewById(R.id.label_email)).setText(dao.getFireBaseUser().getEmail());
        Picasso.with(getBaseContext()).load(dao.getFireBaseUser().getPhotoUrl()).into(((ImageView) header.findViewById(R.id.card_aula_img)));
    }

    @Override
    public void onBackPressed() {
        navigationView.setCheckedItem(R.id.nav_feed);
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(navId);
    }

    protected void uncheckAll(){
        navigationView.setCheckedItem(R.id.menu_none);
    }
}
