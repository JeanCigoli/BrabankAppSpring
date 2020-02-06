package br.com.cursoandroid.brabrank.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String NOME_BANCO = "db_brabank";
    public static final String TABELA_CATEGORIA = "tbl_categoria";
    public static final String TABELA_LANCAMENTO = "tbl_lancamento";

    public static final int VERSAO = 1;


    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlCategoria = "CREATE TABLE " + TABELA_CATEGORIA + "(" +
                "codigo INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "tipo TEXT NOT NULL" +
                ")";

        String sqlLancamento = "CREATE TABLE " + TABELA_LANCAMENTO + "(" +
                "codigo INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tipo TEXT NOT NULL," +
                "descricao TEXT NOT NULL," +
                "valor REAL NOT NULL," +
                "data_lancamento TEXT NOT NULL," +
                "categoria INTEGER NOT NULL" +
                ")";

        db.execSQL(sqlCategoria);
        db.execSQL(sqlLancamento);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public DBHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }
}
