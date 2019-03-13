package com.example.lsantos.sistemacadastro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.lsantos.sistemacadastro.modelo.Usuario;

public class DadosCompletosActivity extends AppCompatActivity {

    private TextView nome, cpf, telefone;
    private Usuario usuario = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_completos);
        //usuario = new Usuario();
        nome = findViewById(R.id.nome);
        cpf = findViewById(R.id.cpf);
        telefone = findViewById(R.id.telefone);

        Intent intent = getIntent();
        if(intent.hasExtra("usuario")) {
            usuario = (Usuario) intent.getSerializableExtra("usuario");
            nome.setText(usuario.getNome());
            cpf.setText(usuario.getCpf());
            telefone.setText(usuario.getTelefone());
        }

    }
}
