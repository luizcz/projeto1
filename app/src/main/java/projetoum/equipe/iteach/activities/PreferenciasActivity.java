package projetoum.equipe.iteach.activities;

import android.os.Bundle;

import projetoum.equipe.iteach.R;

public class PreferenciasActivity extends DrawerActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);

        init(R.id.nav_options);

    }

}
