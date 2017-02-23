package projetoum.equipe.iteach.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

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
    private ImageView aula_img;
    private TextView aula_vagas;
    private TextView aula_valor;
    private TextView aula_data;
    private TextView aula_horario;
    private TextView aula_conteudo_body;
    private TextView aula_endereco;
    private TextView aula_mapa_dist;

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

        aula_img = (ImageView) findViewById(R.id.aula_img);
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

        aula_nome_professor.setText(mClass.getName());
//        aula_rating.setRating(aulaSelecionada.getRating());
        aula_vagas.setText("Vagas ocupadas: " + String.valueOf(mClass.getSlots()));

        String valor = mClass.getValorFormatado();
        if (valor.equals("0")){
            aula_valor.setText("Valor: " + R.string.free);
        } else {
            aula_valor.setText("Valor: " + valor);
        }

        if (mClass.getDiasSemana() != null){
            aula_data.setText("Data: " + mClass.getDiasSemana().toString());
        } else {
            aula_data.setText("Data: Não informado");
        }



        aula_horario.setText("Horario: " + String.valueOf(mClass.getHoraInicio()));
        aula_conteudo_body.setText(mClass.getDescription());
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
}
