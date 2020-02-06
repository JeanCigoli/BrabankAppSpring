package br.com.cursoandroid.brabrank.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import br.com.cursoandroid.brabrank.model.entity.Categoria;

public class CategoriaDAO {

    DBHelper dbHelper;
    SQLiteDatabase db;

    public CategoriaDAO(Context context) {

        dbHelper = new DBHelper(context);

    }

    private List<Categoria> toList (Cursor cursor){

        List<Categoria> categorias = new ArrayList<>();

        while(cursor.moveToNext()){

            Categoria c = new Categoria(
                    cursor.getInt(cursor.getColumnIndex("codigo")),
                    cursor.getString(cursor.getColumnIndex("nome")),
                    cursor.getString(cursor.getColumnIndex("tipo"))
            );

            categorias.add(c);
        }

        return categorias;
    }

    public List<Categoria> listarTodos() {

        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(dbHelper.TABELA_CATEGORIA, null, null,null, null, null, null);

        return toList(cursor);

    }

    public List<Categoria> listarPorTipo (String tipo){

        String[] args = {tipo};

        db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(dbHelper.TABELA_CATEGORIA, null, "tipo = ?", args, null, null,null);

        return toList(cursor);
    }

    public Categoria buscarPorId(Integer id){

        db = dbHelper.getReadableDatabase();

        String[] args = {id.toString()};

        // Dando erro bem aqui;
        Cursor cursor = db.query(dbHelper.TABELA_CATEGORIA, null, "codigo = ?", args, null, null ,null, null);

        if(cursor.moveToNext()){

            Categoria c = new Categoria(
                    cursor.getInt(cursor.getColumnIndex("codigo")),
                    cursor.getString(cursor.getColumnIndex("nome")),
                    cursor.getString(cursor.getColumnIndex("tipo"))
            );

            return c;

        } else {

            return null;
        }
    }

    public boolean inserir(Categoria categoria){

        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nome", categoria.nome);
        values.put("tipo", categoria.tipo);

        return db.insert(dbHelper.TABELA_CATEGORIA, null, values) > 0;

    }

    public boolean editar(Categoria categoria){

        db = dbHelper.getWritableDatabase();

        String[] args = {categoria.id.toString()};

        ContentValues values = new ContentValues();
        values.put("nome", categoria.nome);
        values.put("tipo", categoria.tipo);

        return db.update(dbHelper.TABELA_CATEGORIA, values, "codigo = ?", args) > 0;
    }

    public boolean excluir(Categoria categoria){

        db = dbHelper.getWritableDatabase();

        String[] args = {categoria.id.toString()};

        return db.delete(dbHelper.TABELA_CATEGORIA, "codigo = ?", args) > 0;
    }
}
