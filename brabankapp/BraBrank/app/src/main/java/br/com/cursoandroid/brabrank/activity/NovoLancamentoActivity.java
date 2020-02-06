package br.com.cursoandroid.brabrank.activity;

import android.app.DatePickerDialog;
import android.graphics.MaskFilter;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import br.com.cursoandroid.brabrank.R;
import br.com.cursoandroid.brabrank.model.CategoriaDAO;
import br.com.cursoandroid.brabrank.model.LancamentoDAO;
import br.com.cursoandroid.brabrank.model.entity.Categoria;
import br.com.cursoandroid.brabrank.model.entity.Lancamento;

public class NovoLancamentoActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnFocusChangeListener {

    Toolbar toolbar;
    RadioGroup rdGroup;
    EditText descricao, valor, data;
    Spinner spinner;

    DatePickerDialog dialogoData;

    List<Categoria> categorias = new ArrayList<>();
    private ArrayAdapter<Categoria> adpSpinner;

    LancamentoDAO lancamentoDAO = new LancamentoDAO(this);
    CategoriaDAO categoriaDAO = new CategoriaDAO(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_lancamento);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Lançamento");
        setSupportActionBar(toolbar);

        rdGroup = findViewById(R.id.rdGroupNovo);
        descricao = findViewById(R.id.txtDescricao);
        valor = findViewById(R.id.txtValor);
        data = findViewById(R.id.txtDate);
        spinner = findViewById(R.id.spinner);

        rdGroup.setOnCheckedChangeListener(this);

        data.setOnFocusChangeListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

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
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void salvar() {

        if(validar()){

            Lancamento lancamento = new Lancamento();
            lancamento.codigo = null;
            lancamento.descricao = descricao.getText().toString();
            lancamento.valor = Float.parseFloat(valor.getText().toString());

            Integer idCategoria = spinner.getSelectedItemPosition();
            Categoria categoria = categorias.get(idCategoria);

            Categoria cat = categoriaDAO.buscarPorId(categoria.id);

            lancamento.categoria = cat;

            if (rdGroup.getCheckedRadioButtonId() == R.id.rdDespesaNovo){
                lancamento.tipo = "despesa";
            } else if (rdGroup.getCheckedRadioButtonId() == R.id.rdReceitaNovo){
                lancamento.tipo = "receita";
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dataMili;

            try {

                dataMili = sdf.parse(data.getText().toString());
                lancamento.dataLancamento = dataMili;

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(lancamentoDAO.inserir(lancamento)){
                Snackbar.make( findViewById(R.id.novoLancamentoLaouyt), "Lançamento cadastrado com sucesso!", Snackbar.LENGTH_LONG).show();

                descricao.setText("");
                data.setText("");
                valor.setText("");
            }

        }

    }

    public boolean validar(){

        boolean validator = true;

        if(spinner.getSelectedItem() == null){
            new AlertDialog.Builder(this)
                    .setTitle("Erro")
                    .setMessage("Por favor selecione um item do spinner!")
                    .setPositiveButton("OK", null)
                    .show();
            validator = false;
        }

        if(valor.getText().toString().equals("")){
            valor.setError("Campo obrigatório!");
            validator = false;
        }

        if(data.getText().toString().equals("")){
            data.setError("Campo obrigatório!");
            validator = false;
        }

        if(descricao.getText().toString().equals("")){
            descricao.setError("Campo obrigatório!");
            validator = false;
        }

        if(descricao.getText().toString().length()  < 3){
            descricao.setError("No minimo 3 caractéres!");
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
            case R.id.rdDespesaNovo:
                categorias = categoriaDAO.listarPorTipo("despesa");
                break;

            case R.id.rdReceitaNovo:
                categorias = categoriaDAO.listarPorTipo("receita");
                break;
        }

        adpSpinner = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, categorias);
        spinner.setAdapter(adpSpinner);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if(hasFocus){
            escolherData();
        } else {
            validarData();
        }

    }

    private void escolherData() {

        Calendar calendario = Calendar.getInstance();
        Integer intDia = calendario.get(Calendar.DAY_OF_MONTH);
        Integer intMes = calendario.get(Calendar.MONTH);
        Integer intAno = calendario.get(Calendar.YEAR);

        dialogoData = new DatePickerDialog(NovoLancamentoActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int ano, int mes, int dia) {

                data.setText(dia + "/" + (mes + 1) + "/" + ano);

            }

        }, intAno, intMes, intDia);

        dialogoData.getDatePicker().setMaxDate(calendario.getTimeInMillis());

        dialogoData.setTitle("Selecione a data");
        dialogoData.show();
        data.requestFocus();

    }

    private void validarData() {

        Date momentoAtual;
        Date dataUsuario;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);

        try {
            momentoAtual = sdf.parse(sdf.format(new Date()));
            dataUsuario = sdf.parse(data.getText().toString());


            // Se o primeiro elemento for maior ele retornará positivo;
            // Igual será retornado 0;
            // Menor será negativo;

            if(dataUsuario.compareTo(momentoAtual) > 0){

                data.setError("A data não pode ser maior que a atual!");
                data.setText("");

            }

        } catch (ParseException e) {
            e.printStackTrace();

            data.setError("A data está inválida!");
            data.setText("");
        }


    }
}
