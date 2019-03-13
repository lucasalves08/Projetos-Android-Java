package com.example.lsantos.sistemacadastro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lsantos.sistemacadastro.BD.UsuarioDAO;
import com.example.lsantos.sistemacadastro.modelo.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText nome, cpf, telefone;
    private UsuarioDAO dao;
    private Usuario usuario = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        setTitle("Cadastro de Usuários");

        nome = findViewById(R.id.txtNome);
        cpf = findViewById(R.id.txtCPF);
        telefone = findViewById(R.id.txtTelefone);

        dao = new UsuarioDAO(this);

        Intent intent = getIntent();
        if(intent.hasExtra("usuario")){
            usuario = (Usuario) intent.getSerializableExtra("usuario");
            nome.setText(usuario.getNome());
            cpf.setText(usuario.getCpf());
            telefone.setText(usuario.getTelefone());

        }
    }

    public void salvar(View v){

        if(usuario == null) {

            usuario = new Usuario();
            usuario.setNome(nome.getText().toString());
            usuario.setCpf(cpf.getText().toString());
            usuario.setTelefone(telefone.getText().toString());

            long id = dao.inserir(usuario);


            alert("Usuario inserido com id: " + id);
            finish();
        }
        else {
            usuario.setNome(nome.getText().toString());
            usuario.setCpf(cpf.getText().toString());
            usuario.setTelefone(telefone.getText().toString());
            dao.atualizar(usuario);
            alert("Usuario foi atualizado!");
            finish();

        }

    }

    public void alert(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
