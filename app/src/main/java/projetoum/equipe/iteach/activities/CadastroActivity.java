package projetoum.equipe.iteach.activities;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.lang.ref.WeakReference;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.BrazilNumberFormatter;

public class CadastroActivity extends DrawerActivity implements View.OnClickListener {
    User usuarioAtual;
    private EditText edtNome;
    private EditText edtTelefone;
    private EditText edtTelefoneFormatted;
    private EditText edtLocal;
    private EditText edtBio;
    private Button btProximo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //supportRequestWindowFeature(AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_cadastro);

        init(R.id.nav_feed);

        edtNome = (EditText) findViewById(R.id.edt_nome);
        edtTelefone = (EditText) findViewById(R.id.edt_telefone);
        BrazilNumberFormatter formatter = new BrazilNumberFormatter(new WeakReference<EditText>(edtTelefone));
        edtTelefone.addTextChangedListener(formatter);
        edtLocal = (EditText) findViewById(R.id.edt_local);
        edtBio = (EditText) findViewById(R.id.edt_bio);


        dao.getCurrentUser(new ICallback<User>() {
            @Override
            public void execute(User param) {
                usuarioAtual = param;
                if (usuarioAtual.getName() != null) {
                    edtNome.setText(usuarioAtual.getName());
                }
                if (usuarioAtual.getTelefone() != null) {
                    edtTelefone.setText(usuarioAtual.getTelefone());
                }
                if (usuarioAtual.getLocal() != null) {
                    edtLocal.setText(usuarioAtual.getLocal());
                }
                if (usuarioAtual.getBio() != null) {
                    edtBio.setText(usuarioAtual.getBio());
                }
            }
        });

        btProximo = (Button) findViewById(R.id.bt_salvar_perfil);
        btProximo.setOnClickListener(this);
        setTextListeners();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_salvar_perfil) {
            findViewById(R.id.bt_salvar_perfil).setEnabled(false);
            if(checkDataForm()){
                carregarDadosUsuario();
                usuarioAtual.setFirstTime(false);
                dao.updateUserOnce(usuarioAtual, new ICallback() {
                    @Override
                    public void execute(Object param) {
                        startActivity(new Intent(CadastroActivity.this, PreferenciasActivity.class));
                        finish();
                    }
                });
            }
            findViewById(R.id.bt_salvar_perfil).setEnabled(true);
        }
    }

    private void carregarDadosUsuario() {
        if (edtNome.getText() != null) {
            usuarioAtual.setName(edtNome.getText().toString());
        }
        if (edtTelefone.getText() != null) {
            usuarioAtual.setTelefone(edtTelefone.getText().toString());
        }
        if (edtLocal.getText() != null) {
            usuarioAtual.setLocal(edtLocal.getText().toString());
            dao.getLocationFromAddress(usuarioAtual);
        }
        if (edtBio.getText() != null) {
            usuarioAtual.setBio(edtBio.getText().toString());
        }
    }

    private void setTextListeners() {
        edtNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtNome.getText().toString().trim().length() <= 0) {
                    edtNome.setError(getString(R.string.campo_nao_pode_ser_vazio));
                } else {
                    edtNome.setError(null);
                }
            }
        });
        edtTelefone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtTelefone.getText().toString().trim().length() <= 0) {
                    edtTelefone.setError(getString(R.string.campo_nao_pode_ser_vazio));
                } else {
                    edtTelefone.setError(null);
                }
            }
        });

        edtLocal.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtLocal.getText().toString().trim().length() <= 0) {
                    edtLocal.setError(getString(R.string.campo_nao_pode_ser_vazio));
                } else {
                    edtLocal.setError(null);
                }
            }
        });

    }

    private boolean checkDataForm() {

        if (edtNome.getText() == null || edtNome.getText().toString().trim().equals("")) {
            edtNome.setError(getString(R.string.campo_nao_pode_ser_vazio));
            edtNome.requestFocus();
            return false;
        } else {
            edtNome.setError(null);
        }

        if (edtTelefone.getText() == null || edtTelefone.getText().toString().trim().equals("")) {
            edtTelefone.setError(getString(R.string.campo_nao_pode_ser_vazio));
            edtTelefone.requestFocus();
            return false;
        } else {
            edtTelefone.setError(null);
        }

        if (edtLocal.getText() == null || edtLocal.getText().toString().trim().equals("")) {
            edtLocal.setError(getString(R.string.campo_nao_pode_ser_vazio));
            edtLocal.requestFocus();
            return false;
        } else {
            edtLocal.setError(null);
        }
        return true;
    }



}
