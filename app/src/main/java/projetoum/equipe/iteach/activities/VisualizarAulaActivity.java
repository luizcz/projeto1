package projetoum.equipe.iteach.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.models.ClassObject;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.DAO;

import static projetoum.equipe.iteach.R.id.aula_mapa;

public class VisualizarAulaActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Toolbar toolbar;
    private DAO dao;

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
                // Do nothing but close the dialog
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


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        preencherDados();
    }

    private void preencherDados() {
        toolbar.setTitle("Reforço de Português");
        setSupportActionBar(toolbar);
        Intent intent = getIntent();

//        Intent intent = getIntent();
//        String nomeAula = intent.getStringExtra("aula");

        TextView aula_nome_professor = (TextView) findViewById(R.id.aula_nome_professor);
        RatingBar aula_rating = (RatingBar) findViewById(R.id.aula_rating);
        TextView aula_vagas = (TextView) findViewById(R.id.aula_vagas);
        TextView card_aula_valor = (TextView) findViewById(R.id.card_aula_valor);
        TextView aula_data = (TextView) findViewById(R.id.aula_data);
        TextView aula_horario = (TextView) findViewById(R.id.aula_horario);
        TextView aula_conteudo_body = (TextView) findViewById(R.id.aula_conteudo_body);
        TextView aula_endereco = (TextView) findViewById(R.id.aula_endereco);
        TextView aula_mapa_dist = (TextView) findViewById(R.id.aula_mapa_dist);

//        if (intent.getStringExtra("aulaId").equals("10")){
//            ClassObject aulaSelecionada = new ClassObject();
//
//            for (ClassObject aula: dao.getClasses()){
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
//            aula_nome_professor.setText(aulaSelecionada.getName());
////        aula_rating.setRating(aulaSelecionada.getRating());
//            aula_vagas.setText("Vagas ocupadas: " + String.valueOf(aulaSelecionada.getSlots()));
//            card_aula_valor.setText("R$ " + String.valueOf(aulaSelecionada.getValue()) + ",00");
//            aula_data.setText("Data: " + "Terças e Quintas");
//            aula_horario.setText("Horario: " + String.valueOf(aulaSelecionada.getTime()));
//            aula_conteudo_body.setText("Preposições\nUso de Crase\nVerbos Irregulares");
//            aula_endereco.setText(aulaSelecionada.getAddress());
//            aula_mapa_dist.setText(String.valueOf(10) + " Km");
//        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13f));

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
