package projetoum.equipe.iteach.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.adapter.ClassAdapter;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.ClassObject;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.DAO;

public class MinhasAulasActivity extends AppCompatActivity {

    private RecyclerView mainListView;
    private ClassAdapter listAdapter;
    private DAO dao;
    private List<ClassObject> classes;
    private TextView nenhuma_aula;
    private TextView tem_aulas;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_aulas);
        getWindow().setBackgroundDrawableResource(R.drawable.background);

        mainListView = (RecyclerView) findViewById( R.id.minhas_aulas_list );
        nenhuma_aula = (TextView) findViewById(R.id.nenhuma_aula);
        tem_aulas = (TextView) findViewById(R.id.tem_aulas);

        listAdapter = new ClassAdapter(this);
        mainListView.setHasFixedSize(true);
        mainListView.setNestedScrollingEnabled(false);

        dao = DAO.getInstace(this);

        carregarClasses();

        mainListView.setAdapter(listAdapter);
    }

    private void carregarClasses() {

        dao.getCurrentUser(new ICallback<User>() {
            @Override
            public void execute(User param) {
                dao.findClassByTeacher(param.getUserId(), new ICallback<List<ClassObject>>() {
                    @Override
                    public void execute(List<ClassObject> param) {
                        classes = param;
                    }
                });
            }
        });

        if (classes == null) {
            classes = new ArrayList<>();
            nenhuma_aula.setVisibility(View.VISIBLE);
            tem_aulas.setVisibility(View.INVISIBLE);

        }
        listAdapter.setClasses(classes);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
