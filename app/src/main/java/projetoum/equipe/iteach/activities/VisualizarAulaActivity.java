package projetoum.equipe.iteach.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URL;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.ClassObject;
import projetoum.equipe.iteach.models.User;
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
    private TextView aula_vagas;
    private TextView card_aula_valor;
    private TextView aula_data;
    private TextView aula_horario;
    private TextView aula_conteudo_body;
    private TextView aula_endereco;
    private TextView aula_mapa_dist;
    private ImageView class_image;
    private ImageView teacher_image;

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
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user-class");
                DatabaseReference newUserClass = ref.child(dao.getFireBaseUser().getUid());
                newUserClass.child(getIntent().getExtras().getString("aula_id")).setValue(true);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        final AlertDialog alert = builder.create();
        Button participar = (Button) findViewById(R.id.aula_botao_participar);
        participar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.show();
            }
        });


        teacher_image = (ImageView) findViewById(R.id.profile_image);
        class_image = (ImageView) findViewById(R.id.class_image_detail);
        aula_nome_professor = (TextView) findViewById(R.id.aula_nome_professor);
        aula_rating = (RatingBar) findViewById(R.id.aula_rating);
        aula_vagas = (TextView) findViewById(R.id.aula_vagas);
        card_aula_valor = (TextView) findViewById(R.id.card_aula_valor);
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
        getImage(class_image,mClass.getImagem());
        dao.findUserById(mClass.getTeacherId(), new ICallback() {
            @Override
            public void execute(Object param) {
                User user = (User) param;
                getImage(teacher_image, user.getHighResURI());
            }
        });
        aula_nome_professor.setText(mClass.getName());
//        aula_rating.setRating(aulaSelecionada.getRating());
        aula_vagas.setText("Vagas ocupadas: " + String.valueOf(mClass.getSlots()));
        card_aula_valor.setText("R$ " + String.valueOf(mClass.getValue()) + ",00");
        aula_data.setText("Data: " + "Terças e Quintas");
        aula_horario.setText("Horario: " + String.valueOf(mClass.getTime()));
        aula_conteudo_body.setText(mClass.getData());
        aula_endereco.setText(mClass.getAddress());
        aula_mapa_dist.setText(String.valueOf(10) + " Km");
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

    private void getImage(final ImageView img, final String imgUrl){
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
                if (bitmap !=  null){
                    img.setImageBitmap(bitmap);
                }
            }
        }.execute();
    }
}
