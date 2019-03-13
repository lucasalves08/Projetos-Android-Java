package com.example.lsantos.sistemacadastro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.lsantos.sistemacadastro.BD.UsuarioDAO;
import com.example.lsantos.sistemacadastro.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ListaActivity extends AppCompatActivity {


    private ListView listView;
    private UsuarioDAO dao;
    private List<Usuario> usuarios;
    private List<Usuario> usuariosFiltrados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        setTitle("Usuários");

        listView = findViewById(R.id.listUsuario);
        dao = new UsuarioDAO(this);
        usuarios = dao.obterTodos();
        usuariosFiltrados.addAll(usuarios);

        ArrayAdapter<Usuario> adapter = new ArrayAdapter<Usuario>(this, android.R.layout.simple_list_item_1, usuariosFiltrados);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_principal, menu);
        SearchView sv = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                procuraUsuario(newText);
                //System.out.println("Digitou "+ newText);
                return false;
            }

            public void procuraUsuario(String nome){

                usuariosFiltrados.clear();
                for(Usuario u : usuarios){
                    if(u.getNome().toLowerCase().contains(nome.toLowerCase())){
                        usuariosFiltrados.add(u);
                    }
                }
                listView.invalidateViews();
            }
        });
        return true;
    }

    // Menu para quando clicar no nome.
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v,menuInfo);
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_contexto, menu);
    }


    @Override
    public void onResume(){
        super.onResume();
        usuarios = dao.obterTodos();
        usuariosFiltrados.clear();
        usuariosFiltrados.addAll(usuarios);
        listView.invalidateViews();

    }

    // MÉTODOS DA CLASSE
    public void cadastrar(MenuItem item){
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);

    }

    public void excluir(MenuItem item){
        //Passsando para um adaptador
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final Usuario usuarioExluir = usuariosFiltrados.get(menuInfo.position);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Atenção")
                .setMessage("Realmente deseja excluir o usuário?")
                .setNegativeButton("NÃO",null)
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        usuariosFiltrados.remove(usuarioExluir);
                        usuarios.remove(usuarioExluir);
                        dao.excluir(usuarioExluir);
                        listView.invalidateViews();
                    }
                }).create();
        dialog.show();
    }

    public void atualizar(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final Usuario usuarioAtualizar = usuariosFiltrados.get(menuInfo.position);

        Intent intent = new Intent(this, CadastroActivity.class);
        intent.putExtra("usuario", usuarioAtualizar);
        startActivity(intent);

    }

    public void dadosCompletos( MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final Usuario usuario = usuariosFiltrados.get(menuInfo.position);

        Intent intent = new Intent(this, DadosCompletosActivity.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);

    }
}
