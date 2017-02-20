package projetoum.equipe.iteach.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.ClassObject;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.DAO;

public class CadastroAulaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    private DAO dao;
    private EditText tituloEd;
    private EditText numVagasEd;
    private EditText valorEd;
    private EditText dataEd;
    private EditText horarioInicioEd;
    private EditText horarioFimEd;
    private EditText assuntoEd;
    private EditText tagsEd;
    private EditText localEd;
    private ImageView imagePropaganda;
    private StorageReference mReference;
    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_aula);

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

        mReference = FirebaseStorage.getInstance().getReference();

        ((TextView)header.findViewById(R.id.label_name)).setText(dao.getFireBaseUser().getDisplayName());
        ((TextView)header.findViewById(R.id.label_email)).setText(dao.getFireBaseUser().getEmail());
        Picasso.with(getBaseContext()).load(dao.getFireBaseUser().getPhotoUrl()).into(((ImageView)header.findViewById(R.id.card_aula_img)));

        tituloEd = (EditText) findViewById(R.id.edt_titulo);
        numVagasEd = (EditText) findViewById(R.id.edt_num_vagas);
        valorEd = (EditText) findViewById(R.id.edt_valor);
        dataEd = (EditText) findViewById(R.id.edt_date);
        horarioInicioEd = (EditText) findViewById(R.id.edt_horario_inicio);
        horarioFimEd = (EditText) findViewById(R.id.edt_horario_fim);
        assuntoEd = (EditText) findViewById(R.id.edt_assunto);
        tagsEd = (EditText) findViewById(R.id.edt_tags);
        localEd = (EditText) findViewById(R.id.edt_local_aula);
        imagePropaganda = (ImageView) findViewById(R.id.img_propaganda);

        imagePropaganda.setOnClickListener(this);
        findViewById(R.id.bt_salvar_aula).setOnClickListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_feed) {
            startActivity(new Intent(this,MainActivity.class));
        } else if (id == R.id.nav_profile) {
            //startActivity(new Intent(this,CadastroActivity.class));

        } else if (id == R.id.nav_my_class) {
            //startActivity(new Intent(this,CourseActivity.class));

        } else if (id == R.id.nav_options) {
            // startActivity(new Intent(this,OptionsActivity.class));

        } else if (id == R.id.nav_class) {
            startActivity(new Intent(this,SearchActivity.class));

        } else if (id == R.id.nav_teacher) {
            startActivity(new Intent(this,SearchActivity.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        case R.id.bt_salvar_aula:
            enviarAula();
            break;
        case R.id.img_propaganda:
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                imagePropaganda.setImageBitmap(bitmap);
                findViewById(R.id.myImageViewText).setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void enviarAula(){
        String nome = String.valueOf(new Date().getTime()) + ".png";
        StorageReference filepath = mReference.child("imagens").child(nome);
        imagePropaganda.setDrawingCacheEnabled(true);

        imagePropaganda.buildDrawingCache();

        Bitmap bm = imagePropaganda.getDrawingCache();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        filepath.putBytes(byteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                enviarAulaComFoto(taskSnapshot.getDownloadUrl().toString());
            }
        });
    }


    private void enviarAulaComFoto(String imagem){
        final ClassObject classe = new ClassObject();
        classe.setImagem(imagem);
        if(tituloEd.getText() != null){
            classe.setName(tituloEd.getText().toString());
        }
        if(numVagasEd.getText() != null){
            classe.setSlots(Double.parseDouble(numVagasEd.getText().toString()));
        }
        if(dataEd.getText() != null){
            classe.setData(dataEd.getText().toString());
        }
        if(horarioInicioEd.getText() != null){}
        if(horarioFimEd.getText() != null){}
        if(assuntoEd.getText() != null){
            classe.setSubject(assuntoEd.getText().toString());
        }
        if(tagsEd.getText() != null){
            List<String> lista = Arrays.asList(tagsEd.getText().toString().split(","));
            classe.setTags(lista);
        }
        if(localEd.getText() != null){
            classe.setAddress(localEd.getText().toString());
        }

        dao.getCurrentUser(new ICallback<User>() {
            @Override
            public void execute(User param) {
                classe.setTeacherId(param.getUserId());
                criarClasse(classe);
            }
        });





    }

    private void criarClasse(ClassObject classe){
        dao.createClass(classe, new ICallback() {
            @Override
            public void execute(Object param) {
                Toast.makeText(CadastroAulaActivity.this, param.toString(), Toast.LENGTH_LONG).show();
                startActivity(new Intent(CadastroAulaActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
