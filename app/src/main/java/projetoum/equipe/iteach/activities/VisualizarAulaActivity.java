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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import projetoum.equipe.iteach.R;

import static projetoum.equipe.iteach.R.id.map;

public class VisualizarAulaActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_aula);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
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

//        Intent intent = getIntent();
//        String nomeAula = intent.getStringExtra("aula");
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
}
