package com.example.myfirebase.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myfirebase.Modelo.Pessoa;
import com.example.myfirebase.R;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CadastroPessoaActivity extends AppCompatActivity {

    private EditText editEmail, editNome, editCpf, editTelefone;
    private Button btnSalvar;
    private Pessoa pessoa = null;

    // Conexão com o banco

    FirebaseDatabase mDatabase;
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pessoa);

        //Views
        editEmail = findViewById(R.id.editEmailPes);
        editNome = findViewById(R.id.editNomePes);
        editCpf = findViewById(R.id.editCPFPes);
        editTelefone = findViewById(R.id.editTelefonePes);

        //Button
        btnSalvar = findViewById(R.id.btnSalvarPes);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inserirPessoa();
                alert("Inserido com sucesso!");
                finish();

            }
        });

        Intent i = getIntent();
        if(i.hasExtra("pessoa")){
            pessoa = (Pessoa) i.getSerializableExtra("pessoa");
            editNome.setText(pessoa.getNome());
            editEmail.setText(pessoa.getEmail());
            editTelefone.setText(pessoa.getTelefone());
            editCpf.setText(pessoa.getCpf());
        }

        iniciarFirebase();



    }

    public void inserirPessoa() {

        if(pessoa == null) {

            pessoa = new Pessoa();
            pessoa.setId(UUID.randomUUID().toString());
            pessoa.setNome(editNome.getText().toString());
            pessoa.setEmail(editEmail.getText().toString());
            pessoa.setCpf(editCpf.getText().toString());
            pessoa.setTelefone(editTelefone.getText().toString());

            ref.child("pessoas").child(pessoa.getId()).setValue(pessoa);

            alert("Inserido com sucesso!");
            finish();
        }
        else {
            Pessoa p = new Pessoa();
            p.setId(pessoa.getId());
            p.setNome(editNome.getText().toString());
            p.setCpf(editCpf.getText().toString());
            p.setEmail(editEmail.getText().toString());
            p.setTelefone(editTelefone.getText().toString());

            ref.child("pessoas").child(p.getId()).setValue(p);

        }


    }

    private void iniciarFirebase() {
        mDatabase = FirebaseDatabase.getInstance();
        //Alterar no aplicativo
//        mDatabase.setPersistenceEnabled(true);
        ref = mDatabase.getReference();
    }

    private void alert(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
