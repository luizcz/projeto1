package projetoum.equipe.iteach.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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

public class MinhasAulasActivity extends DrawerActivity implements View.OnClickListener{

    private RecyclerView mainListView;
    private ClassAdapter listAdapter;
    private DAO dao;
    private List<ClassObject> classes;
    private TextView nao_tem_aulas;
    private TextView tem_aulas;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_aulas);

        init(R.id.nav_my_class);
        getWindow().setBackgroundDrawableResource(R.drawable.background);

        mainListView = (RecyclerView) findViewById( R.id.minhas_aulas_list );
        nao_tem_aulas = (TextView) findViewById(R.id.nenhuma_aula);
        tem_aulas = (TextView) findViewById(R.id.tem_aulas);

        listAdapter = new ClassAdapter(this);
        mainListView.setHasFixedSize(true);
        mainListView.setNestedScrollingEnabled(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mainListView.setLayoutManager(mLayoutManager);

        dao = DAO.getInstace(this);

        carregarClasses();

        mainListView.setAdapter(listAdapter);
    }

    private void carregarClasses() {
        classes = new ArrayList<>();

        dao.getCurrentUser(new ICallback<User>() {
            @Override
            public void execute(User param) {
                dao.findClassesByUser(param.getUserId(), new ICallback<List<String>>() {
                    @Override
                    public void execute(List<String> param) {
                        for (String classId: param){
                            dao.findClassById(classId, new ICallback<ClassObject>() {
                                @Override
                                public void execute(ClassObject param) {
                                    classes.add(param);
                                    listAdapter.setClasses(classes);
                                    listAdapter.notifyItemInserted(classes.size()-1);

                                    tem_aulas.setVisibility(View.VISIBLE);
                                    nao_tem_aulas.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatingActionButton:
                startActivity(new Intent(this, CadastroAulaActivity.class));
                break;
        }
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
