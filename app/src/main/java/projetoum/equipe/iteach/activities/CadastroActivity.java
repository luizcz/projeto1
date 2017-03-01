package projetoum.equipe.iteach.activities;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import projetoum.equipe.iteach.R;
import projetoum.equipe.iteach.interfaces.ICallback;
import projetoum.equipe.iteach.models.User;
import projetoum.equipe.iteach.utils.DAO;

public class CadastroActivity extends DrawerActivity implements View.OnClickListener {

    private DAO dao;
    User usuarioAtual;
    private CadastroActivity mContext;
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
        }
        if (edtBio.getText() != null) {
            usuarioAtual.setBio(edtBio.getText().toString());
        }
    }



}
