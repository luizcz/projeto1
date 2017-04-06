package projetoum.equipe.iteach.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.google.maps.android.SphericalUtil;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.ClassObject;
import projetoum.equipe.iteach.models.FeedItem;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.Constants;
import projetoum.equipe.iteach.utils.DAO;

import static projetoum.equipe.iteach.R.id.aula_mapa;

public class VisualizarAulaActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Toolbar toolbar;
    private DAO dao;
    private String user;
    private String class_object;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_aula);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dao = DAO.getInstace(this);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(aula_mapa);
        mapFragment.getMapAsync(this);


        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);

        builder.setTitle("Confirmação");
        builder.setMessage("Deseja se increver nessa aula?");
        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
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
                            Toast.makeText(getApplicationContext(), "Você já está matriculado nessa classe.", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            param.getAlunos().add(dao.getFireBaseUser().getUid());
                            param.setId(getIntent().getExtras().getString("aula_id"));
                            final ClassObject aula = param;
                            dao.updateClass(param, new ICallback<Integer>() {
                                @Override
                                public void execute(Integer result) {
                                    if (result == Constants.REQUEST_OK) {
                                        Toast.makeText(getApplicationContext(), "Matriculado com sucesso nessa classe.", Toast.LENGTH_LONG).show();
                                        participar.setVisibility(View.GONE);
                                        deixar.setVisibility(View.VISIBLE);
                                        dao.getCurrentUser(new ICallback<User>() {
                                            @Override
                                            public void execute(User user) {
                                                if (user != null) {
                                                    FeedItem feed = user.feed.get(user.feed.size() - 1);
                                                    if (!(feed.aulaID == aula.getId() && feed.subtype == FeedItem.TYPE_CLASS_SUBTYPE_SUBSCRIBE)) {
                                                        user.feed.add(new FeedItem(aula, FeedItem.TYPE_CLASS_SUBTYPE_SUBSCRIBE, FeedItem.STATUS_SHOWING));
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
                                    } else {

                                    }
                                }
                            });
                        }
                    }
                });
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });


        AlertDialog.Builder builderCancelar = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);

        builderCancelar.setTitle("Confirmação");
        builderCancelar.setMessage("Deseja se cancelar inscrição nessa aula?");
        builderCancelar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
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
                        dao.updateClass(param, new ICallback<Integer>() {
                            @Override
                            public void execute(Integer result) {
                                if (result == Constants.REQUEST_OK) {
                                    Log.d("Sair","Saiu");
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

        builderCancelar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        dao.findUserClass(dao.getFireBaseUser().getUid(), getIntent().getExtras().getString("aula_id"), new ICallback<Boolean>() {
            @Override
            public void execute(Boolean param) {
                if(param != null && param){
                    participar.setVisibility(View.GONE);
                    deixar.setVisibility(View.VISIBLE);
                }
            }
        });

        final AlertDialog alert = builder.create();
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
                }

                if (param.getLat() != null && param.getLon() != null && mClass.getLat() != null && mClass.getLon() != null) {

                    Location startPoint = new Location("user");
                    startPoint.setLatitude(param.getLat());
                    startPoint.setLongitude(param.getLon());

                    Location endPoint = new Location("class");
                    endPoint.setLatitude(mClass.getLat());
                    endPoint.setLongitude(mClass.getLon());

                    double distance = startPoint.distanceTo(endPoint) / 1000;

                    DecimalFormat df = new DecimalFormat("#0.0");
                    aula_mapa_dist.setText("Distancia " + String.valueOf(df.format(distance / 1000)) + " Km");
                } else {
                    aula_mapa_dist.setText("Distancia desconhecida");
                }
            }
        });


        aula_nome_professor.setText(mClass.getName());
//        aula_rating.setRating(aulaSelecionada.getRating());
        aula_vagas.setText("Vagas ocupadas: " + String.valueOf(mClass.getSlots()));

        dao.countVagaOcupadasClass(getIntent().getExtras().getString("aula_id"),
                new ICallback<Long>() {
                    @Override
                    public void execute(Long param) {
                        String total = "";
                        total = param.toString() + "/" + String.valueOf(mClass.getSlots());
                        aula_vagas.setText("Vagas ocupadas: " + total);
                    }
                }

        );

        String valor = mClass.getValorFormatado();
        if (valor.equals("0"))

        {
            aula_valor.setText("Valor: " + getResources().getString(R.string.free));
        } else

        {
            aula_valor.setText("Valor: " + valor);
        }

        if (mClass.getDiasSemana() != null)

        {
            aula_data.setText("Data: " + mClass.getDiasSemana().toString());
        } else

        {
            aula_data.setText("Data: Não informado");
        }


        aula_horario.setText("Horario: " + String.valueOf(mClass.getHoraInicio()) + " - " + String.valueOf(mClass.getHoraFim()));
        aula_conteudo_body.setText(mClass.getSubject());
        aula_endereco.setText(mClass.getAddress());

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
            dao.findClassById(getIntent().getStringExtra("aula_id"), new ICallback<ClassObject>() {
                @Override
                public void execute(ClassObject param) {
                    mClass = param;

                    LatLng sydney = new LatLng(-34, 151);

                    if (mClass != null && mClass.getLat() != null && mClass.getLon() != null) {
                        sydney = new LatLng(mClass.getLat(), mClass.getLon());
                    }

                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13f));

                    preencherDados();
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
                    Bitmap classImg = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    return classImg;
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
