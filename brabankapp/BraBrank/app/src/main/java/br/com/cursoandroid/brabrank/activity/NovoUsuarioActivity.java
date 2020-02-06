package br.com.cursoandroid.brabrank.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Random;

import br.com.cursoandroid.brabrank.R;
import br.com.cursoandroid.brabrank.model.UsuarioDAO;
import br.com.cursoandroid.brabrank.model.entity.Usuario;

public class NovoUsuarioActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText nome, cpf, email, senha, confSenha;
    RadioGroup rdGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Usuário");
        setSupportActionBar(toolbar);

        nome = findViewById(R.id.txtNome);
        cpf = findViewById(R.id.txtCpf);
        email = findViewById(R.id.txtEmail);
        senha = findViewById(R.id.txtSenha);
        confSenha = findViewById(R.id.txtConfiSenha);
        rdGroup = findViewById(R.id.rdGroup);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Método responsável por inflar o menu na ToolBar


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_save, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.btnMenuSalvar:
                salvar();
        }

        return super.onOptionsItemSelected(item);
    }

    public void salvar(){
        if(validar()){

            Random random = new Random();
            Integer id = random.nextInt();

            Usuario user = new Usuario();
            user.id = id;
            user.nome = nome.getText().toString();
            user.email = email.getText().toString();
            user.cpf = cpf.getText().toString();
            user.senha = senha.getText().toString();

            if(rdGroup.getCheckedRadioButtonId() == R.id.rdMasculino){
                user.sexo = "Masculino";
            } else if(rdGroup.getCheckedRadioButtonId() == R.id.rdFeminino){
                user.sexo = "Feminino";
            }

            UsuarioDAO dao = new UsuarioDAO();

            if(dao.inserir(user)){
                new AlertDialog.Builder(this)
                        .setTitle("Sucesso")
                        .setMessage("Cadastro efetuado com sucesso!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }

                        ).show();
            }
        }
    }

    public boolean validar(){

        boolean validator = true;

        if(nome.getText().toString().equals("")){
            nome.setError("Campo obrigatório!");
            validator = false;
        }
        if(email.getText().toString().equals("")){
            email.setError("Campo obrigatório!");
            validator = false;
        }

        if(cpf.getText().toString().equals("")){
            cpf.setError("Campo obrigatório!");
            validator = false;
        }

        if(rdGroup.getCheckedRadioButtonId() == -1){
            new AlertDialog.Builder(this)
                    .setTitle("Erro")
                    .setMessage("Selecione um Sexo!")
                    .setPositiveButton("OK", null)
                    .show();
            validator = false;
        }

        if(validator){

            if(!senha.getText().toString().equals(confSenha.getText().toString())){
                new AlertDialog.Builder(this)
                        .setTitle("Erro")
                        .setMessage("Senhas digitadas não conferem!")
                        .setPositiveButton("OK", null)
                        .show();
                validator = false;
            }

        }

        return validator;
    }



}
