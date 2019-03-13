package com.example.lsantos.sistemacadastro.BD;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lsantos.sistemacadastro.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private Conexao conexao;
    private SQLiteDatabase banco;

    public UsuarioDAO(Context context){
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();

    }

    // INSERT
    public long inserir(Usuario usuario){
        ContentValues values = new ContentValues();
        values.put("nome",usuario.getNome());
        values.put("cpf",usuario.getCpf());
        values.put("telefone",usuario.getTelefone());

        return banco.insert("usuario", null, values);
    }
    // SELECT * FROM
    public List<Usuario> obterTodos(){
        List<Usuario> usuarios = new ArrayList<>();
        Cursor cursor = banco.query("usuario",new String[]{"id","nome","cpf","telefone"},
                null,null,null,null,null);
        while(cursor.moveToNext()){
            Usuario u = new Usuario();
            u.setId(cursor.getInt(0));
            u.setNome(cursor.getString(1));
            u.setCpf(cursor.getString(2));
            u.setTelefone(cursor.getString(3));

            usuarios.add(u);
        }
        return usuarios;
    }

    // DELETE

    public void excluir(Usuario u){
        banco.delete("usuario", "id = ?", new String[]{u.getId().toString()});
    }

    // UPDATE

    public void atualizar(Usuario usuario){

        ContentValues values = new ContentValues();
        values.put("nome",usuario.getNome());
        values.put("cpf",usuario.getCpf());
        values.put("telefone",usuario.getTelefone());
        banco.update("usuario", values, "id = ?", new String[]{usuario.getId().toString()});


    }



}
