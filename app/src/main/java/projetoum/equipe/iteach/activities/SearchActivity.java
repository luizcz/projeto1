package projetoum.equipe.iteach.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.fragments.SearchAulasFragment;
import projetoum.equipe.iteach.fragments.SearchProfsFragment;
import projetoum.equipe.iteach.adapter.ClassAdapter;
import projetoum.equipe.iteach.adapter.UserAdapter;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.ClassObject;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.DAO;
import projetoum.equipe.iteach.utils.Sort;

public class SearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ICallback {

    private MenuItem menuSearch;
    private SearchView searchView;
    private RecyclerView mRecyclerView;
    private UserAdapter userAdapter;
    private ClassAdapter classAdapter;
    private DAO dao;
    NavigationView navigationView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment currentFragment;
    private int lastFragment;
    private SearchAulasFragment searchAulasFragment;
    private SearchProfsFragment searchProfsFragment;

    public static final String SEARCH_AULAS_TAG = "SEARCH_AULAS_TAG";
    public static final String SEARCH_PROFS_TAG = "SEARCH_PROFS_TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setBackgroundDrawableResource(R.drawable.background);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);

        dao = DAO.getInstace(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        userAdapter = new UserAdapter(this);
        classAdapter = new ClassAdapter(this);

        if (getIntent().getStringExtra("busca").equals("aula")) {
            mRecyclerView.setAdapter(classAdapter);
        } else {
            mRecyclerView.setAdapter(userAdapter);
        }

        ((TextView) header.findViewById(R.id.label_name)).setText(dao.getFireBaseUser().getDisplayName());
        ((TextView) header.findViewById(R.id.label_email)).setText(dao.getFireBaseUser().getEmail());
        Picasso.with(getBaseContext()).load(dao.getFireBaseUser().getPhotoUrl()).into(((ImageView) header.findViewById(R.id.card_aula_img)));

        setUpFragments();
    }

    private void setUpFragments() {
        searchAulasFragment = new SearchAulasFragment();
        searchProfsFragment = new SearchProfsFragment();

        dao.loadFirstClasses(classAdapter);
        dao.loadFirstTeachers(userAdapter);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        if (getIntent().getStringExtra("busca").equals("aula")) {
            fragmentTransaction.replace(R.id.fragment_container, searchAulasFragment, SEARCH_AULAS_TAG);
            currentFragment = searchAulasFragment;
        } else {
            fragmentTransaction.replace(R.id.fragment_container, searchProfsFragment, SEARCH_PROFS_TAG);
            currentFragment = searchProfsFragment;
        }

        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
       // firstLoad();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String input) {
                if (currentFragment == searchProfsFragment) {
                    dao.searchUser(input, userAdapter);
                } else if (currentFragment == searchAulasFragment) {
                    dao.searchClass(input, classAdapter);
                }
                searchView.clearFocus();
                return true;
            }


            @Override
            public boolean onQueryTextChange(String input) {
                if (currentFragment == searchProfsFragment) {
                    dao.searchUser(input, userAdapter);
                } else if (currentFragment == searchAulasFragment) {
                    dao.searchClass(input, classAdapter);
                }
                return true;
            }
        });

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.search), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }
        });
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == 0) {
            id = lastFragment;
        }

        switch (id) {
            case R.id.nav_feed:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.nav_profile:
                startActivity(new Intent(this, PerfilActivity.class).putExtra("id",dao.getFireBaseUser().getUid()));
                finish();
            case R.id.nav_my_class:
                startActivity(new Intent(this,MinhasAulasActivity.class));
                finish();
                break;
            case R.id.nav_options:
                startActivity(new Intent(this,PreferenciasActivity.class));
                finish();
                break;
            case R.id.nav_about:
                startActivity(new Intent(this,SobreActivity.class));
                finish();
                break;
            case R.id.nav_logout:
                dao.signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.nav_class:
                setUpClassFragment();
                break;
            case R.id.nav_teacher:
                setUpProfFragment();
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == 0) {
            id = lastFragment;
        }

        switch (id) {
            case R.id.alfa:
                sortByAlpha();
                break;
//            case R.id.rating:
//                sortByRating();
//                break;
            case R.id.price:
                sortByPrice();
                break;
            case R.id.dist:
                sortByDistance();
                break;
        }

        return true;
    }

    private void sortByAlpha() {
        if (currentFragment == searchAulasFragment){
            classAdapter.sort(Sort.ALPHA);
        } else {
            userAdapter.sort(Sort.ALPHA);
        }
    }

    private void sortByRating() {
        if (currentFragment == searchAulasFragment){
            classAdapter.sort(Sort.RATING);
        } else {

        }
    }

    private void sortByPrice() {
        if (currentFragment == searchAulasFragment){
            classAdapter.sort(Sort.PRICE);
        } else {

        }
    }

    private void sortByDistance() {
        if (currentFragment == searchAulasFragment){
            classAdapter.sort(Sort.DISTANCE);
        } else {

        }
    }

    private void setUpClassFragment(){
        fragmentTransaction = fragmentManager.beginTransaction();
        getSupportActionBar().setTitle("Busca Aulas");
        if (fragmentManager.findFragmentByTag(SEARCH_AULAS_TAG) == null) {
            fragmentTransaction.hide(currentFragment);
            fragmentTransaction.add(R.id.fragment_container, searchAulasFragment, SEARCH_AULAS_TAG);
            fragmentTransaction.show(searchAulasFragment).commit();
        } else if (!fragmentManager.findFragmentByTag(SEARCH_AULAS_TAG).isVisible()) {
            fragmentTransaction.hide(currentFragment).show(searchAulasFragment).commit();
        }
        currentFragment = searchAulasFragment;
        lastFragment = R.id.nav_class;
        mRecyclerView.setAdapter(classAdapter);
    }

    private void setUpProfFragment(){
        fragmentTransaction = fragmentManager.beginTransaction();
        getSupportActionBar().setTitle("Busca Professores");
        if (fragmentManager.findFragmentByTag(SEARCH_PROFS_TAG) == null) {
            fragmentTransaction.hide(currentFragment);
            fragmentTransaction.add(R.id.fragment_container, searchProfsFragment, SEARCH_PROFS_TAG);
            fragmentTransaction.show(searchProfsFragment).commit();
        } else if (!fragmentManager.findFragmentByTag(SEARCH_PROFS_TAG).isVisible()) {
            fragmentTransaction.hide(currentFragment).show(searchProfsFragment).commit();
        }
        currentFragment = searchProfsFragment;
        lastFragment = R.id.nav_teacher;
        mRecyclerView.setAdapter(userAdapter);
    }

    @Override
    public void execute(Object param) {

    }
}
