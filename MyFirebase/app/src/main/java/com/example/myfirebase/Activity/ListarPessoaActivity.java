package com.example.myfirebase.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.myfirebase.Modelo.Pessoa;
import com.example.myfirebase.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListarPessoaActivity extends AppCompatActivity {

    //Lista
    ListView listPessoa;
    private List<Pessoa> pessoas = new ArrayList<>();
    private List<Pessoa> pessoasFiltradas = new ArrayList<>();
    private ArrayAdapter<Pessoa> arrayAdapterPessoa;

    //Conexão com o banco
    FirebaseDatabase mDatabase;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_pessoa);

        listPessoa = findViewById(R.id.listPessoa);

        inicializarFirebase();
        listarTodos();
        registerForContextMenu(listPessoa);


    }

    private void listarTodos() {
        ref.child("pessoas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pessoas.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Pessoa p = snapshot.getValue(Pessoa.class);
                    pessoas.add(p);
                }
                pessoasFiltradas.addAll(pessoas);
                arrayAdapterPessoa = new ArrayAdapter<>(ListarPessoaActivity.this, android.R.layout.simple_list_item_1,pessoasFiltradas);
                listPessoa.setAdapter(arrayAdapterPessoa);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_main, menu);
        SearchView sv = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                procuraPessoa(newText);
                return false;
            }

            private void procuraPessoa(String nome) {

                pessoasFiltradas.clear();
                for(Pessoa p : pessoas){
                    if(p.getNome().toLowerCase().contains(nome.toLowerCase())){
                        pessoasFiltradas.add(p);
                    }
                }
                listPessoa.invalidateViews();
            }

        });

        return true;
    }


    // Menus
    // cria o menu de contexto
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_contexto, menu);
    }

    //  menu principal
    public void cadastrar(MenuItem item){
        Intent i = new Intent(ListarPessoaActivity.this, CadastroPessoaActivity.class);
        startActivity(i);
    }

    public void excluir(MenuItem item){
       AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Pessoa pessoaExcluir = pessoasFiltradas.get(menuInfo.position);

        AlertDialog dialog = new AlertDialog.Builder(ListarPessoaActivity.this)
                .setTitle("Atenção")
                .setMessage("Realmente deseja excluir?")
                .setNegativeButton("NÃO", null)
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pessoasFiltradas.remove(pessoaExcluir);
                        pessoas.remove(pessoaExcluir);
                        ref.child("pessoas").child(pessoaExcluir.getId()).removeValue();
                        listPessoa.invalidateViews();
                        finish();
                        startActivity(getIntent());
                    }
                }).create();
        dialog.show();

    }

    public void atualizar(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final Pessoa pessoaAtualizar = pessoasFiltradas.get(menuInfo.position);
        Intent i = new Intent(this, CadastroPessoaActivity.class);
        i.putExtra("pessoa", pessoaAtualizar);
        startActivity(i);

    }

    public void detalhes(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final Pessoa pessoa = pessoasFiltradas.get(menuInfo.position);

        Intent intent = new Intent(this, DetalhesActivity.class);
        intent.putExtra("pessoa", pessoa);
        startActivity(intent);

    }




    @Override
    protected void onResume() {
        super.onResume();
        //pessoas.clear();
        //listarTodos();
        pessoasFiltradas.clear();
        pessoasFiltradas.addAll(pessoas);
        listPessoa.invalidateViews();
    }


    // Inicializar banco
    private void inicializarFirebase() {
        mDatabase = FirebaseDatabase.getInstance();
        ref = mDatabase.getReference();

    }
}
