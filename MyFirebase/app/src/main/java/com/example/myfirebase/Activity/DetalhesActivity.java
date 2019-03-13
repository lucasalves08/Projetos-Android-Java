package com.example.myfirebase.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myfirebase.Modelo.Pessoa;
import com.example.myfirebase.R;

public class DetalhesActivity extends AppCompatActivity {

    private TextView nome, cpf, email, telefone;
    private Pessoa pessoa = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        //View
        email = findViewById(R.id.textEmailL);
        nome = findViewById(R.id.textNomeL);
        telefone = findViewById(R.id.textTelefoneL);
        cpf = findViewById(R.id.textCPFL);

        Intent i = getIntent();
        if(i.hasExtra("pessoa")){
            pessoa = (Pessoa) i.getSerializableExtra("pessoa");
            nome.setText("Nome: "+ pessoa.getNome());
            cpf.setText("CPF: "+ pessoa.getCpf());
            telefone.setText("Telefone: "+ pessoa.getTelefone());
            email.setText("E-mail: "+ pessoa.getEmail());
        }


    }
}
