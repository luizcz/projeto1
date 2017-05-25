package projetoum.equipe.iteach.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.User;

public class CadastroActivity extends DrawerActivity implements View.OnClickListener {
    User usuarioAtual;
    private EditText edtNome;
    private EditText edtTelefone;
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


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_salvar_perfil) {
            carregarDadosUsuario();
            usuarioAtual.setFirstTime(false);
            dao.updateUser(usuarioAtual, new ICallback() {
                @Override
                public void execute(Object param) {
                    startActivity(new Intent(CadastroActivity.this, PreferenciasActivity.class));
                    finish();
                }
            });
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



}
