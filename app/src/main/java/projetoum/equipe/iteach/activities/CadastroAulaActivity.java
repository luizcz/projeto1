package projetoum.equipe.iteach.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
    private TextView dataEd;
    private TextView dataFimEd;
    private TextView horarioInicioEd;
    private TextView horarioFimEd;
    private EditText assuntoEd;
    private EditText tagsEd;
    private EditText localEd;
    private ImageView imagePropaganda;
    private StorageReference mReference;
    private int mDay, mYear, mMonth, mHour, mMinute;
    private boolean inicio;
    private boolean inicioDate;
    private List<String> diasSemana;
    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_aula);
        getWindow().setBackgroundDrawableResource(R.drawable.background);

        diasSemana = new ArrayList<String>();

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
        dataEd = (TextView) findViewById(R.id.edt_date);
        dataFimEd = (TextView) findViewById(R.id.edt_date_fim);
        horarioInicioEd = (TextView) findViewById(R.id.edt_horario_inicio);
        horarioFimEd = (TextView) findViewById(R.id.edt_horario_fim);
        assuntoEd = (EditText) findViewById(R.id.edt_assunto);
        tagsEd = (EditText) findViewById(R.id.edt_tags);
        localEd = (EditText) findViewById(R.id.edt_local_aula);
        imagePropaganda = (ImageView) findViewById(R.id.img_propaganda);

        horarioInicioEd.setOnClickListener(this);
        horarioFimEd.setOnClickListener(this);
        dataEd.setOnClickListener(this);
        dataFimEd.setOnClickListener(this);

        setTextListeners();
        imagePropaganda.setOnClickListener(this);
        findViewById(R.id.bt_salvar_aula).setOnClickListener(this);

    }

    private void setTextListeners() {

        tituloEd.addTextChangedListener(new TextWatcher()  {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s)  {
                if (tituloEd.getText().toString().trim().length() <= 0) {
                    tituloEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
                } else {
                    tituloEd.setError(null);
                }
            }
        });
        numVagasEd.addTextChangedListener(new TextWatcher()  {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s)  {
                if (numVagasEd.getText().toString().trim().length() <= 0) {
                    numVagasEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
                } else {
                    numVagasEd.setError(null);
                }
            }
        });
        valorEd.addTextChangedListener(new TextWatcher()  {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s)  {
                if (valorEd.getText().toString().trim().length() <= 0) {
                    valorEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
                } else {
                    valorEd.setError(null);
                }
            }
        });
        dataEd.addTextChangedListener(new TextWatcher()  {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s)  {
                if (dataEd.getText().toString().trim().length() <= 0) {
                    dataEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
                } else {
                    dataEd.setError(null);
                }
            }
        });
        dataFimEd.addTextChangedListener(new TextWatcher()  {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s)  {
                if (dataFimEd.getText().toString().trim().length() <= 0) {
                    dataFimEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
                } else {
                    dataFimEd.setError(null);
                }
            }
        });
        horarioInicioEd.addTextChangedListener(new TextWatcher()  {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s)  {
                if (horarioInicioEd.getText().toString().trim().length() <= 0) {
                    horarioInicioEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
                } else {
                    horarioInicioEd.setError(null);
                }
            }
        });
        horarioFimEd.addTextChangedListener(new TextWatcher()  {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s)  {
                if (horarioFimEd.getText().toString().trim().length() <= 0) {
                    horarioFimEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
                } else {
                    horarioFimEd.setError(null);
                }
            }
        });
        assuntoEd.addTextChangedListener(new TextWatcher()  {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s)  {
                if (assuntoEd.getText().toString().trim().length() <= 0) {
                    assuntoEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
                } else {
                    assuntoEd.setError(null);
                }
            }
        });
        tagsEd.addTextChangedListener(new TextWatcher()  {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s)  {
                if (tagsEd.getText().toString().trim().length() <= 0) {
                    tagsEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
                } else {
                    tagsEd.setError(null);
                }
            }
        });
        localEd.addTextChangedListener(new TextWatcher()  {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s)  {
                if (localEd.getText().toString().trim().length() <= 0) {
                    localEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
                } else {
                    localEd.setError(null);
                }
            }
        });

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
            
            if (checkDataForm()){
                enviarAula();
            }
            break;
        case R.id.edt_horario_inicio:
            inicio = true;
            showTimePickerDialog();
            break;
        case R.id.edt_horario_fim:
            inicio = false;
            showTimePickerDialog();
            break;
        case R.id.edt_date_fim:
            inicioDate = false;
            showDateDialog();
            break;
        case R.id.edt_date:
            inicioDate = true;
            showDateDialog();
            break;

        case R.id.img_propaganda:
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            break;
        }
    }

    private boolean checkDataForm() {
//        if(tituloEd.getText() != null && tituloEd.getText().toString().trim().equalsIgnoreCase("")){
//            if(numVagasEd.getText() != null && numVagasEd.getText().toString().trim().equalsIgnoreCase("")){
//                if(dataEd.getText() != null && dataEd.getText().toString().trim().equalsIgnoreCase("")){
//                    if(dataFimEd.getText() != null && dataFimEd.getText().toString().trim().equalsIgnoreCase("")){
//                        if(horarioInicioEd.getText() != null && horarioInicioEd.getText().toString().trim().equalsIgnoreCase("")){
//                            if(horarioFimEd.getText() != null && horarioFimEd.getText().toString().trim().equalsIgnoreCase("")){
//                                if(assuntoEd.getText() != null && assuntoEd.getText().toString().trim().equalsIgnoreCase("")){
//                                    if(tagsEd.getText() != null && tagsEd.getText().toString().trim().equalsIgnoreCase("")){
//                                        if(localEd.getText() != null && localEd.getText().toString().trim().equalsIgnoreCase("")){
//                                            return true;
//                                        } else {
//                                            localEd.setError("This field can not be blank");
//                                        }
//                                    } else {
//                                        tagsEd.setError("This field can not be blank");
//                                    }
//                                } else {
//                                    assuntoEd.setError("This field can not be blank");
//                                }
//                            } else {
//                                horarioFimEd.setError("This field can not be blank");
//                            }
//                        } else {
//                            horarioInicioEd.setError("This field can not be blank");
//                        }
//                    } else {
//                        dataFimEd.setError("This field can not be blank");
//                    }
//                } else {
//                    numVagasEd.setError("This field can not be blank");
//                }
//            } else {
//                tituloEd.setError("This field can not be blank");
//            }
//        }

        return false;
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
                findViewById(R.id.selecione_uma_imagem).setVisibility(View.GONE);
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
        classe.setName(tituloEd.getText().toString());
        classe.setSlots(Integer.parseInt(numVagasEd.getText().toString()));
        classe.setData(dataEd.getText().toString());
        classe.setDataFim(dataFimEd.getText().toString());
        classe.setHoraInicio(horarioInicioEd.getText().toString());
        classe.setHoraFim(horarioFimEd.getText().toString());
        classe.setSubject(assuntoEd.getText().toString());
        List<String> lista = Arrays.asList(tagsEd.getText().toString().split(","));
        classe.setTags(lista);
        classe.setAddress(localEd.getText().toString());
        classe.setDiasSemana(diasSemana);

        dao.getCurrentUser(new ICallback<User>() {
            @Override
            public void execute(User param) {
                classe.setTeacherId(param.getUserId());
                criarClasse(classe);
            }
        });
    }

    public void showTimePickerDialog() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,R.style.TimePickerTheme,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        if(inicio)
                            horarioInicioEd.setText(hourOfDay + ":" + minute);
                        else
                            horarioFimEd.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void showDateDialog(){
    // Get Current Date
    final Calendar c = Calendar.getInstance();
    mYear = c.get(Calendar.YEAR);
    mMonth = c.get(Calendar.MONTH);
    mDay = c.get(Calendar.DAY_OF_MONTH);


    DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.TimePickerTheme,
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {

                    if(inicioDate)
                        dataEd.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    else
                        dataFimEd.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                }
            }, mYear, mMonth, mDay);
    datePickerDialog.show();

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

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        if(checked)
            diasSemana.add(((CheckBox) view).getText().toString());
        else
            diasSemana.remove(((CheckBox) view).getText().toString());

    }

    private void validarClasse(ClassObject classe){
        if(classe.getName() == null || classe.getName().isEmpty()){
            //TODO decidir erro
        }else if(classe.getAddress() == null || classe.getAddress().isEmpty()){

        }else if(classe.getSlots() <= 0){

        }

    }
}
