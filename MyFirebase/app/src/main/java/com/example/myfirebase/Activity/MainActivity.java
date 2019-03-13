package com.example.myfirebase.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.myfirebase.Modelo.Pessoa;
import com.example.myfirebase.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    EditText editNome, editEmail;
    ListView listDado;
    private List<Pessoa> listPessoa = new ArrayList<>();
    private ArrayAdapter<Pessoa> arrayAdapterPessoa;
    private Pessoa pessoaSelecionada;

    // Conexão com o banco

    FirebaseDatabase mDatabase;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Views
        editEmail = findViewById(R.id.editEmail);
        editNome = findViewById(R.id.editNome);
        listDado = findViewById(R.id.listDados);


        inicializarFirebase();

        eventoDAO();

        listDado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pessoaSelecionada = (Pessoa) parent.getItemAtPosition(position);
                editEmail.setText(pessoaSelecionada.getEmail());
                editNome.setText(pessoaSelecionada.getNome());

            }
        });
    }

    private void eventoDAO() {
        ref.child("Pessoa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPessoa.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Pessoa p = snapshot.getValue(Pessoa.class);
                    listPessoa.add(p);
                }
                arrayAdapterPessoa = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,listPessoa);
                listDado.setAdapter(arrayAdapterPessoa);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    // Inicializar banco
    private void inicializarFirebase() {
        mDatabase = FirebaseDatabase.getInstance();
        //Alterar no aplicativo
        mDatabase.setPersistenceEnabled(true);
        ref = mDatabase.getReference();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*int id = item.getItemId();
        if(id == R.id.menu_novo){
            Pessoa p = new Pessoa();
            p.setId(UUID.randomUUID().toString());
            p.setNome(editNome.getText().toString());
            p.setEmail(editEmail.getText().toString());

            ref.child("Pessoa").child(p.getId()).setValue(p);

            limparCampos();

        }*/
        /*else if(id == R.id.menu_atualizar) {
            Pessoa p = new Pessoa();
            p.setId(pessoaSelecionada.getId());
            p.setNome(editNome.getText().toString().trim());
            p.setEmail(editEmail.getText().toString().trim());

            ref.child("Pessoa").child(p.getId()).setValue(p);
            limparCampos();

        }
        else if(id == R.id.menu_deletar){
            Pessoa p = new Pessoa();
            p.setId(pessoaSelecionada.getId());

            ref.child("Pessoa").child(p.getId()).removeValue();
        }*/
        return true;
    }

    private void limparCampos() {
        editEmail.setText("");
        editNome.setText("");
    }



}
