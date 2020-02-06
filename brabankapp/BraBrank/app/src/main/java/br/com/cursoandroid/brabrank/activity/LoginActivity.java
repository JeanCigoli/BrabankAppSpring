package br.com.cursoandroid.brabrank.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import br.com.cursoandroid.brabrank.R;
import br.com.cursoandroid.brabrank.model.RetrofitConfig;
import br.com.cursoandroid.brabrank.model.UsuarioDAO;
import br.com.cursoandroid.brabrank.model.api.UsuarioAPI;
import br.com.cursoandroid.brabrank.model.entity.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnEntrar, btnPrimeiroAcesso;
    EditText usuario, txtsenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnEntrar = findViewById(R.id.btnEntrar);
        btnPrimeiroAcesso = findViewById(R.id.btnCadastrar);

        usuario = findViewById(R.id.txtUsuario);
        txtsenha = findViewById(R.id.txtLoginSenha);

        btnPrimeiroAcesso.setOnClickListener(this);
        btnEntrar.setOnClickListener(this);



    }

    @Override
    protected void onResume() {
        super.onResume();

        UsuarioDAO userDao = new UsuarioDAO();
        List<Usuario> usuarios = userDao.listarTodos();

        for(Usuario u : usuarios){

        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnCadastrar:
                Intent intentNovoUsuario = new Intent(getBaseContext(), NovoUsuarioActivity.class);
                startActivity(intentNovoUsuario);
                break;

            case R.id.btnEntrar:
                autenticaApi();
                break;
        }

    }

    private void autenticaApi(){

        String email = usuario.getText().toString().trim();
        final String senha = txtsenha.getText().toString();

        UsuarioAPI usuarioAPI = RetrofitConfig.getRetrofitInstance()
                .create(UsuarioAPI.class);


        Call<Usuario> call = usuarioAPI.buscarPorEmail(email);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                if(response.code() == 404){
                    Toast.makeText(getBaseContext(), "Usuário e/ou Senha não encontrado", Toast.LENGTH_LONG);
                } else if (response.code() == 200){
                    Usuario user = response.body();

                    if(!user.senha.equals(senha)){

                        new AlertDialog.Builder(getBaseContext())
                                .setTitle("Usuário não encontrado")
                                .setMessage("Por favor verifique seu e-mail ou sua senha!")
                                .setPositiveButton("OK", null)
                                .show();

                    } else{

                        Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });


    }



    private void autenticar(){

        String email = usuario.getText().toString().trim();
        String senha = txtsenha.getText().toString();

        UsuarioDAO dao = new UsuarioDAO();

        Usuario user = dao.buscarPorEmail(email) ;

        if(user == null || !user.senha.equals(senha)){

            new AlertDialog.Builder(this)
                    .setTitle("Usuário não encontrado")
                    .setMessage("Por favor verifique seu e-mail ou sua senha!")
                    .setPositiveButton("OK", null)
                .show();

        } else{

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }

    }

}
