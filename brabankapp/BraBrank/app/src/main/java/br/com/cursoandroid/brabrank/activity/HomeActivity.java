package br.com.cursoandroid.brabrank.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import br.com.cursoandroid.brabrank.R;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home");

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.navView);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case R.id.novoLancamento:
                Intent intent_novoLancamento = new Intent(this, NovoLancamentoActivity.class);
                startActivity(intent_novoLancamento);
                break;

            case R.id.lancamentos:

                break;

            case R.id.categorias:
                Intent intent_categoria = new Intent(this, CategoriasActivity.class);
                startActivity(intent_categoria);
                break;

            case R.id.informacao:
                Intent intent_informacao = new Intent(this, InformacoesActivity.class);
                startActivity(intent_informacao);
                break;

            case R.id.ajuda:
                new AlertDialog.Builder(this)
                        .setTitle("Ajuda!")
                        .setMessage("Por favor entre em contato com este e-mail: ajuda@brabank.com ou nos ligue neste número: (11) 4598-6520")
                        .setPositiveButton("OK", null)
                        .show();
                break;

            case R.id.perfil:

                break;

            case R.id.sair:
                Intent intent_sair = new Intent(this, LoginActivity.class);
                startActivity(intent_sair);
                this.finish();
                break;
        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}
