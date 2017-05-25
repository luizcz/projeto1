package projetoum.equipe.iteach.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.vision.text.Text;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.adapter.ClassAdapter;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.ClassObject;
import projetoum.equipe.iteach.models.User;

public class PerfilActivity extends DrawerActivity {

    private GoogleApiClient mGoogleApiClient;
    private TextView name, local, bio;
    private ImageView img;
    private ProgressBar spinner;
    private List<ClassObject> classes;
    private List<ClassObject> classesMinistro;
    private RecyclerView participoListView;
    private RecyclerView ministroListView;
    private ClassAdapter listParticipoAdapter;
    private ClassAdapter listMinistroAdapter;
    private RatingBar ratingBarSmall;
    private RatingBar ratingBar;
    private Double atual;


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
        ratingBarSmall = (RatingBar) findViewById(R.id.ratingBarSmal);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);


        participoListView = (RecyclerView) findViewById(R.id.recycler_participo);
//        nenhuma_aula = (TextView) findViewById(R.id.nenhuma_aula);
//        tem_aulas = (TextView) findViewById(R.id.tem_aulas);

        listParticipoAdapter = new ClassAdapter(this);
        participoListView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        participoListView.setLayoutManager(mLayoutManager);

        participoListView.setAdapter(listParticipoAdapter);

        ministroListView = (RecyclerView) findViewById(R.id.recycler_ministro);
//        nenhuma_aula = (TextView) findViewById(R.id.nenhuma_aula);
//        tem_aulas = (TextView) findViewById(R.id.tem_aulas);

        listMinistroAdapter = new ClassAdapter(this);
        ministroListView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManagerMinistro = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ministroListView.setLayoutManager(mLayoutManagerMinistro);

        ministroListView.setAdapter(listMinistroAdapter);

        classes = new ArrayList<>();
        classesMinistro = new ArrayList<>();


        Typeface giz = Typeface.createFromAsset(getAssets(), "font/giz.ttf");
        name.setTypeface(giz);


        if (getIntent().hasExtra("id")) {
            findViewById(R.id.frame_avaliacao).setVisibility(View.GONE);
            dao.findUserById(getIntent().getStringExtra("id"), new ICallback<User>() {
                @Override
                public void execute(User param) {
                    if (param.getUserId() == null) {
                        param.userId = getIntent().getStringExtra("id");
                    } else {
                        uncheckAll();
                    }
                    dao.carregarNota(getIntent().getStringExtra("id"), dao.getFireBaseUser().getUid(), new ICallback<Double>() {
                        @Override
                        public void execute(Double param) {
                            if (param != null) {
                                mudarParaEditar(param);
                            }

                            if (getIntent().getStringExtra("id") != null && !getIntent().getStringExtra("id").
                                    equals(dao.getFireBaseUser().getUid())) {
                                findViewById(R.id.frame_avaliacao).setVisibility(View.VISIBLE);
                            }


                        }
                    });
                    preencherAvalie(param);
                    carregarImagemAvalie();
                    carregarClassesMinistro(param.userId);
                    name.setText(pattern(param.name));
                    local.setText("Endereço: " + param.getLocal());
                    bio.setText("Bio: " + param.getBio());
                    if (param.getNotas() != null && param.getNotas().size() > 0) {
                        ratingBar.setRating(calcularMedia(param.getNotas()).floatValue());
                    } else {
                        ratingBar.setVisibility(View.GONE);
                    }
                    if (param.highResURI != null && !param.highResURI.isEmpty())
                        Picasso.with(getBaseContext()).load(param.highResURI).fit().
                                centerCrop().into((ImageView) findViewById(R.id.card_aula_img));
                    else
                        Picasso.with(getBaseContext()).load(param.getLowResURI()).fit().
                                centerCrop().into((ImageView) findViewById(R.id.card_aula_img));
                    spinner.setVisibility(View.GONE);
                }
            });

        } else {
            findViewById(R.id.frame_avaliacao).setVisibility(View.GONE);
            dao.getCurrentUser(new ICallback<User>() {
                @Override
                public void execute(User param) {
                    carregarClassesParticipo(param.userId);
                    carregarClassesMinistro(param.userId);
                    name.setText(pattern(param.name));
                    local.setText("Endereço: " + param.getLocal());
                    bio.setText("Bio: " + param.getBio());
                    if (param.getNotas() != null && param.getNotas().size() > 0) {
                        ratingBar.setRating(calcularMedia(param.getNotas()).floatValue());
                    } else {
                        ratingBar.setVisibility(View.GONE);
                    }
                    if (param.highResURI != null && !param.highResURI.isEmpty())
                        Picasso.with(getBaseContext()).load(param.highResURI).fit().
                                centerCrop().into((ImageView) findViewById(R.id.card_aula_img));
                    else
                        Picasso.with(getBaseContext()).load(param.getLowResURI()).fit().
                                centerCrop().into((ImageView) findViewById(R.id.card_aula_img));
                    spinner.setVisibility(View.GONE);
                }
            });
        }
    }

    private void carregarImagemAvalie() {
        dao.getCurrentUser(new ICallback<User>() {
            @Override
            public void execute(User param) {
                if (param.highResURI != null && !param.highResURI.isEmpty())
                    Picasso.with(getBaseContext()).load(param.highResURI).fit().
                            centerCrop().into((ImageView) findViewById(R.id.imagem_usuario_avaliacao));
                else
                    Picasso.with(getBaseContext()).load(param.getLowResURI()).fit().
                            centerCrop().into((ImageView) findViewById(R.id.imagem_usuario_avaliacao));
            }
        });
    }

    private Double calcularMedia(List<Double> l) {
        Double soma = 0d;
        for (Double a : l) {
            if (a != null) {
                soma += a;
            }
        }
        return soma / l.size();
    }

    private void preencherAvalie(final User professor) {
        findViewById(R.id.st_avalie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((TextView) view).getText().toString().equals("Editar")) {
                    ratingBarSmall.setIsIndicator(false);
                    atual = (double) ratingBarSmall.getRating();
                    ((TextView) view).setText("Avalie");
                    ((TextView) view).setTextColor(Color.parseColor("#000000"));
                    ((RelativeLayout) findViewById(R.id.fundo_avaliar)).setBackgroundColor(Color.parseColor("#FFFFFF"));
                } else {
                    dao.avaliarProfessor(getIntent().getStringExtra("id"), dao.getFireBaseUser().getUid(), (double) ratingBarSmall.getRating(), professor, atual, new ICallback<Double>() {
                        @Override
                        public void execute(Double param) {
                            //Toast.makeText(getApplicationContext(), "Deu certo", Toast.LENGTH_LONG).show();
                            mudarParaEditar(param);
                        }
                    });
                }
            }
        });
    }

    private void mudarParaEditar(Double nota) {
        ratingBarSmall.setRating(nota.floatValue());
        ratingBarSmall.setIsIndicator(true);
        TextView avalie = (TextView) findViewById(R.id.st_avalie);
        ((RelativeLayout) findViewById(R.id.fundo_avaliar)).setBackgroundColor(Color.parseColor("#000000"));
        avalie.setText("Editar");
        avalie.setTextColor(Color.parseColor("#FFFFFF"));
        ratingBar.setVisibility(View.VISIBLE);
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

        if (getIntent().getStringExtra("id") != null && !getIntent().getStringExtra("id").
                equals(dao.getFireBaseUser().getUid())) {
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

    private void carregarClassesParticipo(String id) {
        dao.findClassesByUser(id, new ICallback<List<String>>() {
            @Override
            public void execute(List<String> param) {
                for (String classId : param) {
                    dao.findClassById(classId, new ICallback<ClassObject>() {
                        @Override
                        public void execute(ClassObject param) {
                            classes.add(param);
                            listParticipoAdapter.setClasses(classes);
                            listParticipoAdapter.notifyItemInserted(classes.size() - 1);

                            if (classes.size() > 0) {
                                findViewById(R.id.ll_aluno).setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }

    private void carregarClassesMinistro(String id) {
        dao.findUserById(id, new ICallback() {
            @Override
            public void execute(Object param) {
                if (((User) param).getMyClasses() != null) {
                    for (String classId : ((User) param).getMyClasses()) {
                        dao.findClassById(classId, new ICallback<ClassObject>() {
                            @Override
                            public void execute(ClassObject param) {
                                if (param != null) {
                                    classesMinistro.add(param);
                                    listMinistroAdapter.setClasses(classesMinistro);
                                    listMinistroAdapter.notifyItemInserted(classesMinistro.size() - 1);

                                    if (classesMinistro.size() > 0) {
                                        findViewById(R.id.ll_ministro).setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });


    }
}
