package projetoum.equipe.iteach.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.adapter.TagAdapter;
import projetoum.equipe.iteach.fragments.CadastroAulaFragment;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.ClassObject;
import projetoum.equipe.iteach.models.FeedItem;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.Constants;
import projetoum.equipe.iteach.utils.LocationHelper;

import static projetoum.equipe.iteach.R.id.mapview;

public class CadastroDeAulaActivity extends DrawerActivity implements View.OnClickListener, OnMapReadyCallback, CadastroAulaFragment.OnFragmentInteractionListener {
    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnBack, btnNext;


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
    private GoogleMap mMap;
    private View mapView;
    private LatLng local;
    private View[] telas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_de_aula);
        init(R.id.nav_class);
        uncheckAll();


        diasSemana = new ArrayList<>();
        mReference = FirebaseStorage.getInstance().getReference();

        local = Constants.DEFAULT_LOCATION;

        calDataInicio = Calendar.getInstance();
        calDataFim = Calendar.getInstance();
        calHorarioInicio = Calendar.getInstance();
        calHorarioFim = Calendar.getInstance();

        inflateScreen(R.layout.cadastro_aula_slide1);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnBack = (Button) findViewById(R.id.btn_back);
        btnNext = (Button) findViewById(R.id.btn_next);


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.cadastro_aula_slide1,
                R.layout.cadastro_aula_slide2,
                R.layout.cadastro_aula_slide3,
                R.layout.cadastro_aula_slide4,
                R.layout.cadastro_aula_slide5,
                R.layout.cadastro_aula_slide6};


        telas = new View[layouts.length];
        // adding bottom dots
        addBottomDots(0);

        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (current > 1) {
                    // move to next screen
                    viewPager.setCurrentItem(current - 2);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    /*TODO: 08/06/17 mostrar aula criada */
                    //launchHomeScreen();
                }
            }
        });
    }

    private void inflateScreen(int layout) {
        switch (layout) {
            case R.layout.cadastro_aula_slide1:

                break;
            case R.layout.cadastro_aula_slide2:
                numVagasEd = (EditText) findViewById(R.id.edt_num_vagas);
                valorEd = (EditText) findViewById(R.id.edt_valor);
                horarioInicioEd = (TextView) findViewById(R.id.start_time_edt);
                horarioFimEd = (TextView) findViewById(R.id.end_time_edt);
                horarioInicioEd.setOnClickListener(this);
                horarioFimEd.setOnClickListener(this);
                break;
            case R.layout.cadastro_aula_slide3:

                dataInicioEd = (TextView) findViewById(R.id.edt_date_inicio);
                dataFimEd = (TextView) findViewById(R.id.edt_date_fim);
                title_dias_semana = (TextView) findViewById(R.id.st_week_day);

                dataInicioEd.setOnClickListener(this);
                dataFimEd.setOnClickListener(this);
                break;
            case R.layout.cadastro_aula_slide4:
                localEd = (EditText) findViewById(R.id.edt_local_aula);
                localEd.setOnKeyListener(new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_DOWN) {
                            switch (keyCode) {
                                case KeyEvent.KEYCODE_DPAD_CENTER:
                                case KeyEvent.KEYCODE_ENTER:
                                    if (!localEd.getText().toString().trim().isEmpty()) {
                                        local = LocationHelper.getLatLng(LocationHelper.getLocationFromGoogle(localEd.getText().toString().trim()));
                                        markLocalOnMap();
                                    }
                                    return true;
                                default:
                                    break;
                            }
                        }
                        return false;
                    }
                });
                localEd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            if (!localEd.getText().toString().trim().isEmpty()) {
                                local = LocationHelper.getLatLng(LocationHelper.getLocationFromGoogle(localEd.getText().toString().trim()));
                                markLocalOnMap();
                            }
                            return true;
                        }
                        return false;
                    }
                });
                localEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus && !localEd.getText().toString().trim().isEmpty()) {
                            local = LocationHelper.getLatLng(LocationHelper.getLocationFromGoogle(localEd.getText().toString().trim()));
                            markLocalOnMap();
                        }
                    }
                });
                mapView = findViewById(R.id.mapview);
                //mapView.setVisibility(View.GONE);

                SupportMapFragment mapFragment =
                        (SupportMapFragment) getSupportFragmentManager().findFragmentById(mapview);
                mapFragment.getMapAsync(this);

                break;
            case R.layout.cadastro_aula_slide5:

                imagePropaganda = (ImageView) findViewById(R.id.img_propaganda);
                selecioneUmaImagem = (TextView) findViewById(R.id.selecione_uma_imagem);
                fotoSelecionada = false;
                imagePropaganda.setOnClickListener(this);

                break;
            case R.layout.cadastro_aula_slide6:

                recycler = (RecyclerView) findViewById(R.id.recycler);

                List<String> rowListItem = new ArrayList<String>();
                GridLayoutManager layoutManager = new GridLayoutManager(CadastroDeAulaActivity.this, 3);

                recycler.setHasFixedSize(false);
                recycler.setLayoutManager(layoutManager);
                recycler.setAdapter(new TagAdapter(this, rowListItem, true));
                findViewById(R.id.bt_salvar_aula).setOnClickListener(this);
                findViewById(R.id.bt_salvar_aula).setEnabled(true);

                break;


        }

        // setTextListeners();

    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.dot_dark));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.dot_light));
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText("Criar aula");
                //btnBack.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText("Próximo");
                btnBack.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                local = latLng;
                markLocalOnMap();
                localEd.setText(LocationHelper.getLocation(LocationHelper.getLocationFromGoogleLatLng(latLng)));
            }
        });

        markLocalOnMap();

    }

    private void markLocalOnMap() {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(local).title("Local da aula"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(local));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(local, 13f));
    }

    /**
     * View pager adapter
     */
    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    return CadastroAulaFragment.newInstance(R.layout.cadastro_aula_slide1);
                case 1:
                    return CadastroAulaFragment.newInstance(R.layout.cadastro_aula_slide2);
                case 2:
                    return CadastroAulaFragment.newInstance(R.layout.cadastro_aula_slide3);
                case 3:
                    return CadastroAulaFragment.newInstance(R.layout.cadastro_aula_slide4);
                case 4:
                    return CadastroAulaFragment.newInstance(R.layout.cadastro_aula_slide5);
                case 5:
                    return CadastroAulaFragment.newInstance(R.layout.cadastro_aula_slide6);
                default:
                    return CadastroAulaFragment.newInstance(R.layout.cadastro_aula_slide1);
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
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
        System.out.println("aaaaaaaaaaaa");
        System.out.println(v.getId());
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
            case R.id.start_time_edt:
                inicio = true;
                showTimePickerDialog();
                break;
            case R.id.end_time_edt:
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


                    Toast.makeText(CadastroDeAulaActivity.this, "Aula cadastrada com sucesso", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CadastroDeAulaActivity.this, VisualizarAulaActivity.class);
                    intent.putExtra("aula_id", param.toString());
                    startActivity(intent);
                }
                finish();
            }
        });
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}


