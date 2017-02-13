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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.fragments.SearchAulasFragment;
import projetoum.equipe.iteach.fragments.SearchProfsFragment;
import projetoum.equipe.iteach.adapter.ClassAdapter;
import projetoum.equipe.iteach.adapter.UserAdapter;
import projetoum.equipe.iteach.models.ClassObject;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.DAO;

public class SearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private MenuItem menuSearch;
    private SearchView searchView;
    private ClassAdapter searchAdapter;
    private RecyclerView mRecyclerView;
    private UserAdapter userAdapter;
    private ClassAdapter classAdapter;
    private DAO dao;
    private List<User> usuarios;
    private List<ClassObject> classes;
    private FragmentManager fragmentManager;
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
        // toolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.transparent));
        setSupportActionBar(toolbar);
        getWindow().setBackgroundDrawableResource(R.drawable.background);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);

        dao = DAO.getInstace(this);
        usuarios = dao.getUsuarios();
        classes = dao.getClasses();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        userAdapter = new UserAdapter(getBaseContext());
        classAdapter = new ClassAdapter(getBaseContext());
        mRecyclerView.setAdapter(classAdapter);

        ((TextView)header.findViewById(R.id.label_name)).setText(dao.getFireBaseUser().getDisplayName());
        ((TextView)header.findViewById(R.id.label_email)).setText(dao.getFireBaseUser().getEmail());
        Picasso.with(getBaseContext()).load(dao.getFireBaseUser().getPhotoUrl()).into(((ImageView)header.findViewById(R.id.img)));

        setUpFragments();
    }

    private void setUpFragments(){
        searchAulasFragment = new SearchAulasFragment();
        searchProfsFragment = new SearchProfsFragment();

        currentFragment = searchAulasFragment;

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, searchAulasFragment, SEARCH_AULAS_TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        menuSearch = menu.findItem(R.id.search);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

//            @Override
//            public boolean onQueryTextSubmit(String string) {
//                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("class");
//                ref.orderByChild("name_lower").startAt(string.toLowerCase()).addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        Log.e("Qualquercoisa", "qualquercoisa");
//                        ClassObject classObject = dataSnapshot.getValue(ClassObject.class);
//                        searchAdapter.addClass(classObject);
//                    }
//
//                    //                new TimeOut().execute("1000");
//                    updateList(dao.getUsuarios(), input);
//
//                    searchView.clearFocus();
//                    return true;
//                }

            @Override
            public boolean onQueryTextSubmit(String input) {
                if (currentFragment == searchProfsFragment){
                    updateProfs(dao.getUsuarios(), input);
                } else if (currentFragment == searchAulasFragment){
                    updateAulas(dao.getClasses(), input);
                }
                searchView.clearFocus();
                return true;
            }


            @Override
            public boolean onQueryTextChange(String input) {
                if (currentFragment == searchProfsFragment){
                    updateProfs(dao.getUsuarios(), input);
                } else if (currentFragment == searchAulasFragment){
                    updateAulas(dao.getClasses(), input);
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

    private void updateAulas(List<ClassObject> classes, String input) {
        List<ClassObject> copia = new ArrayList<>();
        copia.addAll(classes);

        classAdapter.removeAll();

        if (!input.equals("")) {
            for (int i=0; i < copia.size(); i++){
                Log.i("i",String.valueOf(i));
                if (copia.get(i).getName().toLowerCase().contains(input)){
                    classAdapter.add(copia.get(i));
                }
            }
        } else {
            classAdapter.setClasses(dao.getClasses());
        }
    }

    private void updateProfs(List<User> usuarios, String input) {
        List<User> copia = new ArrayList<User>();
        copia.addAll(usuarios);

        userAdapter.removeAll();

        if (!input.equals("")) {
            for (int i = 0; i < copia.size(); i++) {
                Log.i("i", String.valueOf(i));
                if (copia.get(i).getName().toLowerCase().contains(input)) {
                    userAdapter.add(copia.get(i));
                }
            }
        } else {
            userAdapter.setUsuarios(dao.getUsuarios());
        }
    }


//    public void updateList(List<User> result, String string) {
//        //((TextView) findViewById(R.id.sample_output)).setText("");
//        List<User> copia = new ArrayList<User>();
//        copia.addAll(dao.getUsuarios());
//
////        Log.i("add", onlyAdd.toString());
//        userAdapter.removeAll();
//        Log.i("userAdapter", userAdapter.getUsuarios().toString());
//
//        for (int i=0; i < copia.size(); i++){
//            Log.i("i",String.valueOf(i));
//            if (copia.get(i).getName().toLowerCase().contains(string)){
//                userAdapter.add(copia.get(i));
//            }
//        }

//        List<User> copiaClasses = new ArrayList<User>();
//        copiaClasses.addAll(dao.getClasses());
//
////        Log.i("add", onlyAdd.toString());
//        userAdapter.removeAll();
//        Log.i("userAdapter", userAdapter.getUsuarios().toString());
//
//        for (int i=0; i < copiaClasses.size(); i++){
//            Log.i("i",String.valueOf(i));
//            if (copiaClasses.get(i).getName().toLowerCase().contains(string)){
//                userAdapter.add(copiaClasses.get(i));
//            }
//        }
//        for (User item : onlyRemove) {
//            if (onlyAdd.contains(item)) onlyAdd.remove(item);
//            else
//                userAdapter.remove(item);
//        }
//        for (User item : onlyAdd) {
//            userAdapter.add(userAdapter.getItemCount(), item);
//        }
//    }

//    private class TimeOut extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... time) {
//            int t = Integer.parseInt(time[0]);
//            while (t > 0) t--;
//            return "";
//        }
//
//        // onPostExecute displays the results of the AsyncTask.
//        @Override
//        protected void onPostExecute(String result) {
////            Log.i("httpReady", String.valueOf(httpHandler.isReady()));
////            if (httpHandler.isReady()) {
//            usuarios = getUsers(dao.getUsuarios(), result);
//
////                handleQuery(search_input);
////                progressBar.setVisibility(View.INVISIBLE);
////            }
////            else
////                new TimeOut().execute("1000");
////        }
//        }
//    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (id == 0) {
            id = lastFragment;
        }

        switch (id){
            case R.id.nav_feed:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.nav_profile:
                startActivity(new Intent(this,PerfilActivity.class));
            case R.id.nav_my_class:
                //startActivity(new Intent(this,CourseActivity.class));
                break;
            case R.id.nav_options:
                // startActivity(new Intent(this,OptionsActivity.class));
                break;
            case R.id.nav_class:
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
                break;
                // startActivity(new Intent(this,SearchActivity.class));
            case R.id.nav_teacher:
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
                // startActivity(new Intent(this,SearchActivity.class));
                break;
            default:
                break;
        }

//        if (id == R.id.nav_feed) {
//            startActivity(new Intent(this,MainActivity.class));
//        } else if (id == R.id.nav_profile) {
//            startActivity(new Intent(this,PerfilActivity.class));
//
//        } else if (id == R.id.nav_my_class) {
//            //startActivity(new Intent(this,CourseActivity.class));
//
//        } else if (id == R.id.nav_options) {
//            // startActivity(new Intent(this,OptionsActivity.class));
//
//        } else if (id == R.id.nav_class) {
//            getSupportActionBar().setTitle("Busca Aulas");
//            if (fragmentManager.findFragmentByTag(SEARCH_AULAS_TAG) == null) {
//                fragmentTransaction.hide(currentFragment);
//                fragmentTransaction.add(R.id.fragment_container, searchAulasFragment, SEARCH_AULAS_TAG);
//                fragmentTransaction.show(searchAulasFragment).commit();
//            } else if (!fragmentManager.findFragmentByTag(SEARCH_AULAS_TAG).isVisible()) {
//                fragmentTransaction.hide(currentFragment).show(searchAulasFragment).commit();
//            }
//            currentFragment = searchAulasFragment;
//            lastFragment = R.id.nav_class;
//
//        } else if (id == R.id.nav_teacher) {
//           // startActivity(new Intent(this,SearchActivity.class));
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
