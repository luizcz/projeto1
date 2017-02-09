package projetoum.equipe.iteach.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
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
import projetoum.equipe.iteach.adapter.ClassAdapter;
import projetoum.equipe.iteach.adapter.UserAdapter;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.DAO;

public class SearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private MenuItem menuSearch;
    private SearchView searchView;
    private ClassAdapter searchAdapter;
    private String search_input;
    private RecyclerView mRecyclerView;
    private UserAdapter adapter;
    private DAO dao;
    private List<User> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter = new UserAdapter(this);
        mRecyclerView.setAdapter(adapter);

        search_input = "";

        dao = DAO.getInstace(this);


        ((TextView)header.findViewById(R.id.label_name)).setText(dao.getFireBaseUser().getDisplayName());
        ((TextView)header.findViewById(R.id.label_email)).setText(dao.getFireBaseUser().getEmail());
        Picasso.with(getBaseContext()).load(dao.getFireBaseUser().getPhotoUrl()).into(((ImageView)header.findViewById(R.id.img)));


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
                search_input = input;

//                new TimeOut().execute("1000");
                Log.i("dao", dao.getUsuarios().toString());
                updateList(dao.getUsuarios(), input);

                searchView.clearFocus();
                return true;
            }


            @Override
            public boolean onQueryTextChange(String input) {
                search_input = input;
                if (!input.equals("")) {
                    updateList(dao.getUsuarios(), input);
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

    private List<User> getUsers(List<User> usuarios, String input){
        List<User> users_filtered = new ArrayList<>();

        List<User> copia = new ArrayList<>();
        copia.addAll(dao.getUsuarios());
//        Log.i("copia",copia.toString());



        for (int i=0; i<copia.size();i++) {
            if (copia.get(i).getName().contains(input)){
                users_filtered.add(copia.get(i));
                adapter.add(copia.get(i));
            } else {
                adapter.remove(copia.get(i));
            }
        }
        return users_filtered;
    }

    public void updateList(List<User> result, String string) {
        //((TextView) findViewById(R.id.sample_output)).setText("");
        List<User> copia = new ArrayList<User>();
        copia.addAll(dao.getUsuarios());

//        Log.i("add", onlyAdd.toString());
        adapter.removeAll();
        Log.i("adapter",adapter.getUsuarios().toString());

        for (int i=0; i < copia.size(); i++){
            Log.i("i",String.valueOf(i));
            if (copia.get(i).getName().toLowerCase().contains(string)){
                adapter.add(copia.get(i));
            }
        }
//        for (User item : onlyRemove) {
//            if (onlyAdd.contains(item)) onlyAdd.remove(item);
//            else
//                adapter.remove(item);
//        }
//        for (User item : onlyAdd) {
//            adapter.add(adapter.getItemCount(), item);
//        }
    }

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
//            list = getUsers(dao.getUsuarios(), result);
//
////                handleQuery(search_input);
////                progressBar.setVisibility(View.INVISIBLE);
////            }
////            else
////                new TimeOut().execute("1000");
////        }
//        }
//
//
//    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_feed) {
            startActivity(new Intent(this,MainActivity.class));
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this,CadastroActivity.class));

        } else if (id == R.id.nav_my_class) {
            //startActivity(new Intent(this,CourseActivity.class));

        } else if (id == R.id.nav_options) {
            // startActivity(new Intent(this,OptionsActivity.class));

        } else if (id == R.id.nav_class) {
           // startActivity(new Intent(this,SearchActivity.class));

        } else if (id == R.id.nav_teacher) {
           // startActivity(new Intent(this,SearchActivity.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
