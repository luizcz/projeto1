package projetoum.equipe.iteach.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.adapter.TagAdapter;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.ClassObject;
import projetoum.equipe.iteach.models.FeedItem;
import projetoum.equipe.iteach.models.User;

public class CadastroAulaActivity extends DrawerActivity implements View.OnClickListener {

    private EditText tituloEd;
    private EditText numVagasEd;
    private EditText valorEd;
    private TextView dataInicioEd;
    private TextView dataFimEd;
    private Calendar calDataInicio;
    private Calendar calDataFim;
    private Calendar calHorarioInicio;
    private Calendar calHorarioFim;
    private TextView horarioInicioEd;
    private TextView horarioFimEd;
    private TextView title_dias_semana;
    private TextView selecioneUmaImagem;
    private EditText assuntoEd;
    private EditText localEd;
    private ImageView imagePropaganda;
    private Boolean fotoSelecionada;
    private StorageReference mReference;
    private int mDay, mYear, mMonth, mHour, mMinute;
    private boolean inicio;
    private boolean inicioDate;
    private List<String> diasSemana;
    private User professor;
    private int PICK_IMAGE_REQUEST = 1;
    private ProgressBar progressBar;
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_aula);
        getWindow().setBackgroundDrawableResource(R.drawable.background);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar_cd_aula);
        init(R.id.nav_feed);
        diasSemana = new ArrayList<>();
        mReference = FirebaseStorage.getInstance().getReference();


        tituloEd = (EditText) findViewById(R.id.edt_titulo);
        numVagasEd = (EditText) findViewById(R.id.edt_num_vagas);
        valorEd = (EditText) findViewById(R.id.edt_valor);
        dataInicioEd = (TextView) findViewById(R.id.edt_date_inicio);
        dataFimEd = (TextView) findViewById(R.id.edt_date_fim);
        horarioInicioEd = (TextView) findViewById(R.id.edt_horario_inicio);
        horarioFimEd = (TextView) findViewById(R.id.edt_horario_fim);
        assuntoEd = (EditText) findViewById(R.id.edt_assunto);
        localEd = (EditText) findViewById(R.id.edt_local_aula);
        imagePropaganda = (ImageView) findViewById(R.id.img_propaganda);
        title_dias_semana = (TextView) findViewById(R.id.st_week_day);

        selecioneUmaImagem = (TextView) findViewById(R.id.selecione_uma_imagem);
        fotoSelecionada = false;

        calDataInicio = Calendar.getInstance();
        calDataFim = Calendar.getInstance();
        calHorarioInicio = Calendar.getInstance();
        calHorarioFim = Calendar.getInstance();

        horarioInicioEd.setOnClickListener(this);
        horarioFimEd.setOnClickListener(this);
        dataInicioEd.setOnClickListener(this);
        dataFimEd.setOnClickListener(this);

        setTextListeners();
        imagePropaganda.setOnClickListener(this);
        findViewById(R.id.bt_salvar_aula).setOnClickListener(this);
        findViewById(R.id.bt_salvar_aula).setEnabled(true);


        recycler = (RecyclerView) findViewById(R.id.recycler);

        List<String> rowListItem = new ArrayList<String>();
        GridLayoutManager layoutManager = new GridLayoutManager(CadastroAulaActivity.this, 3);

        recycler.setHasFixedSize(false);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(new TagAdapter(this, rowListItem, true));


    }

    private void setTextListeners() {

        tituloEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (tituloEd.getText().toString().trim().length() <= 0) {
                    tituloEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
                } else {
                    tituloEd.setError(null);
                }
            }
        });
        localEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (localEd.getText().toString().trim().length() <= 0) {
                    localEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
                } else {
                    localEd.setError(null);
                }
            }
        });
        numVagasEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (numVagasEd.getText().toString().trim().length() <= 0) {
                    numVagasEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
                } else {
                    numVagasEd.setError(null);
                }
            }
        });
        valorEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (valorEd.getText().toString().trim().length() <= 0) {
                    valorEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
                } else {
                    valorEd.setError(null);
                }
            }
        });
        dataInicioEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (dataInicioEd.getText().toString().trim().length() <= 0) {
                    dataInicioEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
                } else {
                    dataInicioEd.setError(null);
                }
            }
        });
        dataFimEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (dataFimEd.getText().toString().trim().length() <= 0) {
                    dataFimEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
                } else {
                    dataFimEd.setError(null);
                }
            }
        });
        horarioInicioEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (horarioInicioEd.getText().toString().trim().length() <= 0) {
                    horarioInicioEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
                } else {
                    horarioInicioEd.setError(null);
                }
            }
        });
        horarioFimEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (horarioFimEd.getText().toString().trim().length() <= 0) {
                    horarioFimEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
                } else {
                    horarioFimEd.setError(null);
                }
            }
        });
        assuntoEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (assuntoEd.getText().toString().trim().length() <= 0) {
                    assuntoEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
                } else {
                    assuntoEd.setError(null);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_salvar_aula:
                findViewById(R.id.bt_salvar_aula).setEnabled(false);
                if (checkDataForm()) {
                    progressBar.setVisibility(View.VISIBLE);
                    enviarAula();
                } else {
                    findViewById(R.id.bt_salvar_aula).setEnabled(true);
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
            case R.id.edt_date_inicio:
                inicioDate = true;
                showDateDialog();
                break;
            case R.id.edt_date_fim:
                inicioDate = false;
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

        if (imagePropaganda.getDrawable() == null) {
            selecioneUmaImagem.setError(getString(R.string.escolha_uma_imagem));
            selecioneUmaImagem.requestFocus();
            return false;
        } else {
            selecioneUmaImagem.setError(null);
        }

        if (tituloEd.getText() == null || tituloEd.getText().toString().trim().equals("")) {
            tituloEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
            tituloEd.requestFocus();
            return false;
        } else {
            tituloEd.setError(null);
        }

        if (localEd.getText() == null || localEd.getText().toString().trim().equals("")) {
            localEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
            localEd.requestFocus();
            return false;
        } else {
            localEd.setError(null);
        }

        if (numVagasEd.getText() == null || numVagasEd.getText().toString().trim().equals("")) {
            numVagasEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
            numVagasEd.requestFocus();
            return false;
        } else {
            numVagasEd.setError(null);
        }

        if (valorEd.getText() == null || valorEd.getText().toString().trim().equals("")) {
            valorEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
            valorEd.requestFocus();
            return false;
        } else {
            valorEd.setError(null);
        }

        if (diasSemana.isEmpty()) {
            title_dias_semana.setError(getString(R.string.escolha_um_dia));
            title_dias_semana.requestFocus();
            return false;
        } else {
            title_dias_semana.setError(null);
        }

        if (dataInicioEd.getText() == null || dataInicioEd.getText().toString().trim().equals("")) {
            dataInicioEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
            dataInicioEd.requestFocus();
            return false;
        } else {
            dataInicioEd.setError(null);
        }

        if (dataFimEd.getText() == null || dataFimEd.getText().toString().trim().equals("")) {
            dataFimEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
            dataFimEd.requestFocus();
            return false;
        } else {
            dataFimEd.setError(null);
        }

        if (Calendar.getInstance().compareTo(calDataInicio) > 0) {
            dataInicioEd.setError(getString(R.string.data_inicio_futuro));
            dataInicioEd.requestFocus();
            return false;
        } else {
            dataInicioEd.setError(null);
        }

        if (calDataInicio.compareTo(calDataFim) > 0) {
            dataFimEd.setError(getString(R.string.data_final_apos_inicio));
            dataFimEd.requestFocus();
            return false;
        } else {
            dataFimEd.setError(null);
        }

        if (horarioInicioEd.getText() == null || horarioInicioEd.getText().toString().trim().equals("")) {
            horarioInicioEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
            horarioInicioEd.requestFocus();
            return false;
        } else {
            horarioInicioEd.setError(null);
        }

        if (horarioFimEd.getText() == null || horarioFimEd.getText().toString().trim().equals("")) {
            horarioFimEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
            horarioFimEd.requestFocus();
            return false;
        } else {
            horarioFimEd.setError(null);
        }

        if (calHorarioFim.getTime().getTime() - calHorarioInicio.getTime().getTime() <= 0) {
            horarioFimEd.setError(getString(R.string.horario_final_apos_inicio));
            horarioFimEd.requestFocus();
            return false;
        } else {
            horarioFimEd.setError(null);
        }

        if (assuntoEd.getText() == null || assuntoEd.getText().toString().trim().equals("")) {
            assuntoEd.setError(getString(R.string.campo_nao_pode_ser_vazio));
            assuntoEd.requestFocus();
            return false;
        } else {
            assuntoEd.setError(null);
        }
        return true;
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

    private void enviarAula() {
        String nome = String.valueOf(new Date().getTime()) + ".png";
        StorageReference filepath = mReference.child("imagens").child(nome);
        imagePropaganda.setDrawingCacheEnabled(true);

        imagePropaganda.buildDrawingCache();

        Bitmap bm = imagePropaganda.getDrawingCache();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        filepath.putBytes(byteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fotoSelecionada = true;
                enviarAulaComFoto(taskSnapshot.getDownloadUrl().toString());
            }
        });
    }


    private void enviarAulaComFoto(String imagem) {

        final ClassObject classe = new ClassObject();
        classe.setImagem(imagem);
        classe.setValue(Double.parseDouble(valorEd.getText().toString()));
        classe.setName(tituloEd.getText().toString());
        classe.setSlots(Integer.parseInt(numVagasEd.getText().toString()));
        classe.setData(dataInicioEd.getText().toString());
        classe.setDataFim(dataFimEd.getText().toString());
        classe.setHoraInicio(horarioInicioEd.getText().toString());
        classe.setHoraFim(horarioFimEd.getText().toString());
        classe.setSubject(assuntoEd.getText().toString());
        classe.setAddress(localEd.getText().toString());
        classe.setDiasSemana(diasSemana);
        classe.setTags(((TagAdapter) recycler.getAdapter()).getDataset());

        dao.getCurrentUser(new ICallback<User>() {
            @Override
            public void execute(User param) {
                professor = param;
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.TimePickerTheme,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        if (inicio) {
                            horarioInicioEd.setText(hourOfDay + ":" + minute);
                            calHorarioInicio.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calHorarioInicio.set(Calendar.MINUTE, minute);
                        } else {
                            horarioFimEd.setText(hourOfDay + ":" + minute);
                            calHorarioFim.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calHorarioFim.set(Calendar.MINUTE, minute);
                        }
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void showDateDialog() {
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

                        if (inicioDate) {
                            calDataInicio.set(year, monthOfYear, dayOfMonth);
                            dataInicioEd.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        } else {
                            calDataFim.set(year, monthOfYear, dayOfMonth);
                            dataFimEd.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void criarClasse(final ClassObject classe) {
        dao.createClass(classe, new ICallback() {
            @Override
            public void execute(final Object param) {

                if (professor.getMyClasses() == null)
                    professor.setMyClasses(new ArrayList<String>());
                if (!professor.getMyClasses().contains(param.toString())) {
                    professor.getMyClasses().add(param.toString());
                    professor.addOnFeed(new FeedItem(classe, FeedItem.TYPE_CLASS_SUBTYPE_SUBSCRIBE, FeedItem.STATUS_SHOWING));
                    dao.updateUser(professor, new ICallback() {
                        @Override
                        public void execute(Object param) {
                            // faz nada
                        }
                    });
                }
                progressBar.setVisibility(View.GONE);
                if (param != null) {

                    dao.getCurrentUser(new ICallback<User>() {
                        @Override
                        public void execute(final User current) {

                            dao.findUserByTag(classe.getTags(), new ICallback<User>() {
                                @Override
                                public void execute(User user) {
                                    if (user.getUserId() != current.getUserId()) {

                                        user.addOnFeed(new FeedItem(classe, FeedItem.TYPE_CLASS_SUBTYPE_TAG, FeedItem.STATUS_SHOWING));
                                        dao.updateUser(user, new ICallback() {
                                            @Override
                                            public void execute(Object param) {

                                            }
                                        });
                                    }
                                }
                            });

                            dao.findUserWithinDistance(classe.getLat(), classe.getLon(), new ICallback<User>() {
                                @Override
                                public void execute(User user) {
                                    if (!user.getUserId().equals(current.getUserId())) {

                                        user.addOnFeed(new FeedItem(classe, FeedItem.TYPE_CLASS_SUBTYPE_LOCATION, FeedItem.STATUS_SHOWING));
                                        dao.updateUser(user, new ICallback() {
                                            @Override
                                            public void execute(Object param) {

                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });


                    Toast.makeText(CadastroAulaActivity.this, "Aula cadastrada com sucesso", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CadastroAulaActivity.this, VisualizarAulaActivity.class);
                    intent.putExtra("aula_id", param.toString());
                    startActivity(intent);
                }
                finish();
            }
        });
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        if (checked)
            diasSemana.add(((CheckBox) view).getText().toString());
        else
            diasSemana.remove(((CheckBox) view).getText().toString());

    }

}
