package br.com.cursoandroid.brabrank.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.cursoandroid.brabrank.model.entity.Categoria;
import br.com.cursoandroid.brabrank.model.entity.Lancamento;

public class LancamentoDAO {

    DBHelper dbHelper;
    SQLiteDatabase db;
    Context context;

    public LancamentoDAO(Context context) {

        this.context = context;
        dbHelper = new DBHelper(context);

    }

    private List<Lancamento> toList (Cursor cursor){

        List<Lancamento> lancamentos = new ArrayList<>();

        while(cursor.moveToNext()){

            Long dataLong = cursor.getLong(cursor.getColumnIndex("data_lancamento"));
            Date data = new Date();
            data.setTime(dataLong);

            CategoriaDAO cDAO = new CategoriaDAO(context);
            Integer cod_categoria = cursor.getInt(cursor.getColumnIndex("categoria"));
            Categoria cat = cDAO.buscarPorId(cod_categoria);

            Lancamento l = new Lancamento(
                    cursor.getInt(cursor.getColumnIndex("codigo")),
                    cursor.getString(cursor.getColumnIndex("tipo")),
                    cursor.getString(cursor.getColumnIndex("descricao")),
                    cursor.getFloat(cursor.getColumnIndex("valor")),
                    data,
                    cat
            );

            lancamentos.add(l);
        }

        return lancamentos;
    }

    public List<Lancamento> listarTodos(){

        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(dbHelper.TABELA_LANCAMENTO, null, null,null,null,null,null);

        return toList(cursor);
    }

    public Lancamento buscarPorId(Integer id) {

        String[] args = {id.toString()};

        Cursor cursor = db.query(dbHelper.TABELA_CATEGORIA, null, "codigo = ?", args, null, null ,null);

        if(cursor.moveToNext()){

            Long dataLong = cursor.getLong(cursor.getColumnIndex("data_lancamento"));
            Date data = new Date();
            data.setTime(dataLong);

            CategoriaDAO cDAO = new CategoriaDAO(context);
            Integer cod_categoria = cursor.getInt(cursor.getColumnIndex("categoria"));
            Categoria cat = cDAO.buscarPorId(cod_categoria);

            Lancamento l = new Lancamento(
                    cursor.getInt(cursor.getColumnIndex("codigo")),
                    cursor.getString(cursor.getColumnIndex("tipo")),
                    cursor.getString(cursor.getColumnIndex("descricao")),
                    cursor.getFloat(cursor.getColumnIndex("valor")),
                    data,
                    cat
            );

            return l;

        } else {

            return null;
        }
    }

    public List<Lancamento> buscarPorCategoria(Integer codigo) {

        db = dbHelper.getReadableDatabase();

        String[] args = {codigo.toString()};

        Cursor cursor = db.query(dbHelper.TABELA_LANCAMENTO, null, "categoria = ?", args, null, null ,null);

        return toList(cursor);
    }

    public boolean inserir (Lancamento lancamento){

        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("codigo", lancamento.codigo);
        values.put("tipo", lancamento.tipo);
        values.put("descricao", lancamento.descricao);
        values.put("valor", lancamento.valor);
        values.put("data_lancamento", lancamento.dataLancamento.getTime());
        values.put("categoria", lancamento.categoria.id);

        return db.insert(dbHelper.TABELA_LANCAMENTO, null, values) > 0;
    }

    public boolean excluir (Lancamento lancamento){

        db = dbHelper.getWritableDatabase();

        String[] args = {lancamento.codigo.toString()};

        return db.delete(dbHelper.TABELA_LANCAMENTO, "codigo = ?", args) > 0;
    }

    public boolean editar(Lancamento lancamento){

        db = dbHelper.getWritableDatabase();

        String[] args = {lancamento.codigo.toString()};

        ContentValues values = new ContentValues();
        values.put("codigo", lancamento.codigo);
        values.put("tipo", lancamento.tipo);
        values.put("descricao", lancamento.descricao);
        values.put("valor", lancamento.valor);
        values.put("data_lancamento", lancamento.dataLancamento.getTime());
        values.put("categoria", lancamento.categoria.id);

        return db.update(dbHelper.TABELA_LANCAMENTO, values, "codigo = ?", args) > 0;

    }

}
