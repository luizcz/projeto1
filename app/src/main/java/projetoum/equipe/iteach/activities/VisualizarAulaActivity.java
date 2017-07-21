package projetoum.equipe.iteach.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.ClassObject;
import projetoum.equipe.iteach.models.FeedItem;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.Constants;
import projetoum.equipe.iteach.utils.LocationHelper;

import static projetoum.equipe.iteach.R.id.aula_mapa;

public class VisualizarAulaActivity extends DrawerActivity implements OnMapReadyCallback {
    private ClassObject mClass;
    private GoogleMap mMap;
    private TextView aula_nome_professor;
    private RatingBar aula_rating;
    private ImageView aula_img;
    private TextView aula_vagas;
    private TextView aula_valor;
    private TextView aula_data;
    private TextView aula_horario;
    private TextView aula_conteudo_body;
    private TextView aula_endereco;
    private TextView aula_mapa_dist;
    private ImageView class_image;
    private ImageView teacher_image;
    private Button participar;
    private Button deixar;
    private Button remover;
    private Date dataHoje;
    private Date dataFim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_aula);

        init(R.id.nav_class);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(aula_mapa);
        mapFragment.getMapAsync(this);


        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);

        builder.setTitle(R.string.confirmacao);
        builder.setMessage(R.string.deseja_se_inscrever);
        builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user-class");
                DatabaseReference newUserClass = ref.child(dao.getFireBaseUser().getUid());
                newUserClass.child(getIntent().getExtras().getString("aula_id")).setValue(true);
                DatabaseReference refClass = FirebaseDatabase.getInstance().getReference("class-user");
                final DatabaseReference newClassUser = refClass.child(getIntent().getExtras().getString("aula_id"));
                newClassUser.child(dao.getFireBaseUser().getUid()).setValue(true);


                dao.findClassByIdOnce(getIntent().getExtras().getString("aula_id"), new ICallback<ClassObject>() {
                    @Override
                    public void execute(ClassObject param) {
                        if (param.getAlunos() == null)
                            param.setAlunos(new ArrayList<String>());
                        if (param.getAlunos().contains(dao.getFireBaseUser().getUid())) {
                            Toast.makeText(getApplicationContext(), R.string.ja_matriculado, Toast.LENGTH_LONG).show();
                        } else {
                            param.getAlunos().add(dao.getFireBaseUser().getUid());
                            param.setId(getIntent().getExtras().getString("aula_id"));
                            final ClassObject aula = param;
                            dao.updateClassOnce(param, new ICallback<Integer>() {
                                @Override
                                public void execute(Integer result) {
                                    if (result == Constants.REQUEST_OK) {
                                        Toast.makeText(getApplicationContext(), R.string.matriculado_com_sucesso, Toast.LENGTH_LONG).show();
                                        dao.getCurrentUser(new ICallback<User>() {
                                            @Override
                                            public void execute(User user) {
                                                if (user != null) {
                                                    int count = user.feed.size();
                                                    user.addOnFeed(new FeedItem(aula, FeedItem.TYPE_CLASS_SUBTYPE_SUBSCRIBE, FeedItem.STATUS_SHOWING));
                                                    if (count != user.feed.size()) {
                                                        dao.updateUser(user, new ICallback<Integer>() {
                                                            @Override
                                                            public void execute(Integer result) {
                                                                if (result == Constants.REQUEST_OK) {
                                                                    Log.d("Visualizar Aula", "User atualizado");
                                                                }

                                                            }
                                                        });

                                                    }
                                                }
                                            }
                                        });
                                        participar.setVisibility(View.GONE);
                                        deixar.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }
                    }
                });

                dialog.dismiss();
            }
        });


        builder.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });


        AlertDialog.Builder builderCancelar = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);

        builderCancelar.setTitle(R.string.confirmacao);
        builderCancelar.setMessage(R.string.cancelar_inscricao);
        builderCancelar.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user-class");
                DatabaseReference newUserClass = ref.child(dao.getFireBaseUser().getUid());
                newUserClass.child(getIntent().getExtras().getString("aula_id")).removeValue();
                DatabaseReference refClass = FirebaseDatabase.getInstance().getReference("class-user");
                final DatabaseReference newClassUser = refClass.child(getIntent().getExtras().getString("aula_id"));
                newClassUser.child(dao.getFireBaseUser().getUid()).removeValue();

                dao.findClassByIdOnce(getIntent().getExtras().getString("aula_id"), new ICallback<ClassObject>() {
                    @Override
                    public void execute(ClassObject param) {
                        param.getAlunos().remove(dao.getFireBaseUser().getUid());
                        param.setId(getIntent().getExtras().getString("aula_id"));
                        final ClassObject aula = param;
                        dao.updateClassOnce(param, new ICallback<Integer>() {
                            @Override
                            public void execute(Integer result) {
                                if (result == Constants.REQUEST_OK) {
                                    Toast.makeText(getApplicationContext(), R.string.desvinculado, Toast.LENGTH_LONG).show();
                                    dao.getCurrentUser(new ICallback<User>() {
                                        @Override
                                        public void execute(User user) {
                                            if (user != null) {
                                                int count = user.feed.size();
                                                user.addOnFeed(new FeedItem(aula, FeedItem.TYPE_CLASS_SUBTYPE_OUT, FeedItem.STATUS_SHOWING));
                                                if (count != user.feed.size()) {
                                                    dao.updateUser(user, new ICallback<Integer>() {
                                                        @Override
                                                        public void execute(Integer result) {
                                                            if (result == Constants.REQUEST_OK) {
                                                                Log.d("Visualizar Aula", "User atualizado");
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    });
                                    participar.setVisibility(View.VISIBLE);
                                    deixar.setVisibility(View.GONE);
                                } else {

                                }
                            }
                        });

                    }
                });
                dialog.dismiss();
            }
        });

        builderCancelar.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });


        AlertDialog.Builder builderRemover = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);

        builderRemover.setTitle(R.string.confirmacao);
        builderRemover.setMessage(R.string.deseja_remover);
        builderRemover.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dao.removerAula(mClass.getId());
                dialog.dismiss();
                finish();
            }
        });

        builderRemover.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        dao.findUserClass(dao.getFireBaseUser().getUid(), getIntent().getExtras().getString("aula_id"), new ICallback<Boolean>() {
            @Override
            public void execute(Boolean param) {
                if (param != null && param) {
                    participar.setVisibility(View.GONE);
                    deixar.setVisibility(View.VISIBLE);
                }
            }
        });

        final AlertDialog alert = builder.create();
        participar = (Button) findViewById(R.id.aula_botao_participar);
        participar.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                alert.show();
            }
        });
        participar = (Button) findViewById(R.id.aula_botao_participar);
        participar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.show();
            }
        });

        final AlertDialog alertCancel = builderCancelar.create();
        deixar = (Button) findViewById(R.id.aula_botao_deixar);
        deixar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertCancel.show();
            }
        });

        final AlertDialog alertRemover = builderRemover.create();
        remover = (Button) findViewById(R.id.aula_botao_remover);
        remover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertRemover.show();
            }
        });

        teacher_image = (ImageView) findViewById(R.id.profile_image);
        class_image = (ImageView) findViewById(R.id.class_image_detail);
        aula_nome_professor = (TextView) findViewById(R.id.aula_nome_professor);
        aula_rating = (RatingBar) findViewById(R.id.aula_rating);
        aula_vagas = (TextView) findViewById(R.id.aula_vagas);
        aula_valor = (TextView) findViewById(R.id.aula_valor);
        aula_data = (TextView) findViewById(R.id.aula_data);
        aula_horario = (TextView) findViewById(R.id.aula_horario);
        aula_conteudo_body = (TextView) findViewById(R.id.aula_conteudo_body);
        aula_endereco = (TextView) findViewById(R.id.aula_endereco);
        aula_mapa_dist = (TextView) findViewById(R.id.aula_mapa_dist);

        teacher_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VisualizarAulaActivity.this, PerfilActivity.class).putExtra("id",mClass.getTeacherId()));
                finish();
            }
        });

    }

    private void preencherDados() {
        toolbar.setTitle(mClass.getName());
        setSupportActionBar(toolbar);
        Intent intent = getIntent();

//        Intent intent = getIntent();
//        String nomeAula = intent.getStringExtra("aula");


//        if (intent.getStringExtra("aulaId").equals("10")){
//            ClassObject aulaSelecionada = new ClassObject();
//
//            for (ClassObject aula: dao.getmClasses()){
//                if (aula.getId().equals(10)){
//                    aulaSelecionada = aula;
//                    break;
//                }
//            }
//            for (User user: dao.getUsuarios()){
//                if (user.getUserId().equals(aulaSelecionada.getTeacherId())){
//                    User professor = user;
//                }
//            }
//
//
        getImage(class_image, mClass.getImagem());
        dao.findUserById(mClass.getTeacherId(), new ICallback() {
            @Override
            public void execute(Object param) {
                User user = (User) param;
                getImage(teacher_image, user.getHighResURI());
                aula_nome_professor.setText(user.getName());
            }
        });
        dao.getCurrentUser(new ICallback<User>() {
            @Override
            public void execute(User param) {
                if (param.getUserId().equals(mClass.getTeacherId())) {
                    participar.setVisibility(View.GONE);
                    remover.setVisibility(View.VISIBLE);
                }

                if (param.getLat() != null && param.getLon() != null && mClass.getLat() != null && mClass.getLon() != null) {

                    Location startPoint = new Location("user");
                    startPoint.setLatitude(param.getLat());
                    startPoint.setLongitude(param.getLon());

                    Location endPoint = new Location("class");
                    endPoint.setLatitude(mClass.getLat());
                    endPoint.setLongitude(mClass.getLon());

                    double distanceInMeters = startPoint.distanceTo(endPoint) / 1000;

                    aula_mapa_dist.setText(getString(R.string.distancia, LocationHelper.getFormatedDistance(distanceInMeters)));
                } else {
                    aula_mapa_dist.setText(R.string.dist_desconhecida);
                }
            }
        });


        aula_nome_professor.setText(mClass.getName());
//        aula_rating.setRating(aulaSelecionada.getRating());
        aula_vagas.setText(getString(R.string.vagas_ocupadas, String.valueOf(mClass.getSlots())));

        dao.countVagaOcupadasClass(getIntent().getExtras().getString("aula_id"),
                new ICallback<Long>() {
                    @Override
                    public void execute(Long param) {
                        String total = param.toString() + "/" + String.valueOf(mClass.getSlots());
                        aula_vagas.setText(getString(R.string.vagas_ocupadas, total));
                    }
                }
        );

        String valor = mClass.getValorFormatado();
        if (valor.equals("R$ 0")) {
            aula_valor.setText(getString(R.string.valor_free));
        } else {
            aula_valor.setText(getString(R.string.valor, valor));
        }

        if (mClass.getDiasSemana() != null) {
            aula_data.setText(getString(R.string.data, mClass.getDiasSemana().toString()));
        } else {
            aula_data.setText(R.string.data_n_informada);
        }

        aula_horario.setText(getString(R.string.horario_formatado,
                String.valueOf(mClass.getHoraInicio()), String.valueOf(mClass.getHoraFim())));
        aula_conteudo_body.setText(mClass.getSubject());
        aula_endereco.setText(mClass.getAddress());

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        dataFim = null;
        try {
            dataFim = format.parse(mClass.getDataFim());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        dao.getCurrentTime(new ICallback<Long>() {
            @Override
            public void execute(Long param) {
                dataHoje = new Date(param);
                if(dataFim !=null && dataFim.before(dataHoje)){
                    participar.setVisibility(View.GONE);
                    deixar.setVisibility(View.GONE);
                    aula_data.setText(R.string.aula_indisponível);
                    dao.removerAula(mClass.getId());
                }
            }
        });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }
        });


        if (getIntent().hasExtra("aula_id"))
            dao.findClassByIdOnce(getIntent().getStringExtra("aula_id"), new ICallback<ClassObject>() {
                @Override
                public void execute(ClassObject param) {
                    if(param != null){
                        param.setId(getIntent().getStringExtra("aula_id"));
                        mClass = param;

                        LatLng sydney = new LatLng(-34, 151);

                        if (mClass != null && mClass.getLat() != null && mClass.getLon() != null) {
                            sydney = new LatLng(mClass.getLat(), mClass.getLon());
                        }

                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(sydney).title(getString(R.string.local_aula)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13f));


                        preencherDados();
                    }
                }
            });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void getImage(final ImageView img, final String imgUrl) {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                try {
                    URL url = new URL(imgUrl);
                    return BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    img.setImageBitmap(bitmap);
                }
            }
        }.execute();
    }
}
