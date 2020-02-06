package br.com.cursoandroid.brabrank.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

import java.util.List;
import java.util.Random;

import br.com.cursoandroid.brabrank.R;
import br.com.cursoandroid.brabrank.model.CategoriaDAO;
import br.com.cursoandroid.brabrank.model.LancamentoDAO;
import br.com.cursoandroid.brabrank.model.entity.Categoria;
import br.com.cursoandroid.brabrank.model.entity.Lancamento;

public class CategoriasActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, AdapterView.OnItemLongClickListener, SearchView.OnQueryTextListener {

    Toolbar toolbar;

    EditText edtCategoria;
    RadioGroup rdGroup;
    ListView listCategoria;

    boolean inserindo = true;
    Integer idCategoriaEditar = null;

    ArrayAdapter<Categoria> adapterCategorias;
    List<Categoria> categorias;

    SearchView searchView;

    CategoriaDAO cDAO = new CategoriaDAO(this);
    LancamentoDAO lDAO = new LancamentoDAO(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Minhas Categorias");

        setSupportActionBar(toolbar);

        edtCategoria = findViewById(R.id.txtCategoria);
        rdGroup = findViewById(R.id.rdGroupCategoria);

        listCategoria = findViewById(R.id.listCategoria);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rdGroup.setOnCheckedChangeListener(this);

        carregarCategorias();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_save, menu);

        MenuItem searchItem = menu.findItem(R.id.search);

        searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.btnMenuSalvar:
                if(inserindo){
                    salvar();
                } else {
                    editar();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void carregarCategorias() {

        categorias = cDAO.listarTodos();

        adapterCategorias = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                categorias
        );

        // liga a lista ao adapter
        listCategoria.setAdapter(adapterCategorias);

        listCategoria.setOnItemLongClickListener(this);

    }

    public void carregarCategoriasPorTipo(String tipo) {

        categorias = cDAO.listarPorTipo(tipo);

        adapterCategorias = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                categorias
        );

        // liga a lista ao adapter
        listCategoria.setAdapter(adapterCategorias);

        listCategoria.setOnItemLongClickListener(this);

    }

    private void salvar() {

        if(validar()){


            Categoria categoria = new Categoria();
            categoria.id = null;
            categoria.nome = edtCategoria.getText().toString();

            if (rdGroup.getCheckedRadioButtonId() == R.id.rdDespesaCategoria){
                categoria.tipo = "despesa";
            } else if (rdGroup.getCheckedRadioButtonId() == R.id.rdReceitaCategoria){
                categoria.tipo = "receita";
            }


            if(cDAO.inserir(categoria)){
                Snackbar.make( findViewById(R.id.categoriaLaouyt), "Categoria cadastrada com sucesso!", Snackbar.LENGTH_LONG).show();

                edtCategoria.setText("");


            }

            carregarCategoriasPorTipo(categoria.tipo);

        }

    }

    private void editar() {

        if(validar()){

            Categoria categoria = new Categoria();

            categoria.nome = edtCategoria.getText().toString();
            categoria.id = idCategoriaEditar;

            if (rdGroup.getCheckedRadioButtonId() == R.id.rdDespesaCategoria){
                categoria.tipo = "despesa";
            } else if (rdGroup.getCheckedRadioButtonId() == R.id.rdReceitaCategoria){
                categoria.tipo = "receita";
            }

            if(cDAO.editar(categoria)){
                Snackbar.make( findViewById(R.id.categoriaLaouyt), "Categoria editada com sucesso!", Snackbar.LENGTH_LONG).show();

                edtCategoria.setText("");

                inserindo = true;
            }


            carregarCategoriasPorTipo(categoria.tipo);

        }

    }

    public boolean validar(){

        boolean validator = true;

        if(edtCategoria.getText().toString().equals("")){
            edtCategoria.setError("Campo obrigatório!");
            validator = false;
        }

        if(edtCategoria.getText().toString().length() < 3){
            edtCategoria.setError("No minimo 3 caractéres!");
            validator = false;
        }

        if(rdGroup.getCheckedRadioButtonId() == -1){
            new AlertDialog.Builder(this)
                    .setTitle("Erro")
                    .setMessage("Por favor indentifique se é dispesa ou receita!")
                    .setPositiveButton("OK", null)
                    .show();
            validator = false;
        }

        return validator;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId){
            case R.id.rdDespesaCategoria:
                carregarCategoriasPorTipo("despesa");
                break;

            case R.id.rdReceitaCategoria:
                carregarCategoriasPorTipo("receita");
                break;
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        new AlertDialog.Builder(this)
                .setTitle("Selecione um opção")
                .setItems(R.array.listaDialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Categoria categoriaSelecionada = categorias.get(position);

                        if(which == 0){

                            inserindo = false;

                            idCategoriaEditar = categoriaSelecionada.id;
                            edtCategoria.setText(categoriaSelecionada.nome);
                            edtCategoria.requestFocus();
                            edtCategoria.setSelection(0, categoriaSelecionada.nome.length());

                            if(categoriaSelecionada.tipo.equals("despesa")){
                                rdGroup.check(R.id.rdDespesaCategoria);
                            } else if(categoriaSelecionada.tipo.equals("receita")){
                                rdGroup.check(R.id.rdReceitaCategoria);
                            }


                        } else if(which == 1) {

                             excluir(categoriaSelecionada);

                        }

                    }
                })
                .show();


        return false;
    }

    private void excluir(final Categoria categoria) {

        new AlertDialog.Builder(this)
                .setTitle("Contatos")
                .setMessage("Deseja excluir a categoria " + categoria.nome + "?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                List<Lancamento> lancaCate = lDAO.buscarPorCategoria(categoria.id);

                                if(lancaCate.isEmpty()){

                                    if(cDAO.excluir(categoria)){
                                        Snackbar.make( findViewById(R.id.categoriaLaouyt), "Categoria excluída com sucesso!", Snackbar.LENGTH_LONG).show();

                                        carregarCategoriasPorTipo(categoria.tipo);

                                    } else {
                                        Snackbar.make( findViewById(R.id.categoriaLaouyt), "Ops, erro ao excluir a categoria!", Snackbar.LENGTH_LONG).show();
                                    }

                                } else {
                                    new AlertDialog.Builder(getContext())
                                            .setTitle("Ops!")
                                            .setMessage("Categoria está sendo usada em um Lançamento.")
                                            .setPositiveButton("OK", null)
                                            .show();
                                }

                            }
                        }
                )
                .setNegativeButton("Não", null)
                .show();


    }

    private Context getContext(){
        return this;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        adapterCategorias.getFilter().filter(s);

        return false;
    }
}
