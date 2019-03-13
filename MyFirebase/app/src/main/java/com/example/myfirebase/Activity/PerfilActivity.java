package com.example.myfirebase.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myfirebase.Modelo.Usuario;
import com.example.myfirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PerfilActivity extends AppCompatActivity {

    private TextView txtNome, txtFuncao;
    private Button btnAlterar, btnVisualizar;


    private ImageView imagemPerfil;

    private FirebaseDatabase mDatabase;
    private DatabaseReference ref;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        setTitle("Perfil");

        //View
        txtNome = findViewById(R.id.textNome);
        txtFuncao = findViewById(R.id.textFuncao);
        imagemPerfil = findViewById(R.id.imagePerfilUsu);

        //Button
        btnAlterar = findViewById(R.id.btnAlterarDados);
        btnVisualizar = findViewById(R.id.btnVisualizarDados);

        //Instanciar Firabse Database
        mDatabase = FirebaseDatabase.getInstance();
        ref = mDatabase.getReference();


        eventoClicks();


    }

    private void eventoClicks() {

        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PerfilActivity.this, DetalhesUsuariosActivity.class);
                //finish();
                startActivity(i);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        user = mAuth.getCurrentUser();
        if(user != null) {
            Log.d("Url Nova", String.valueOf(user.getPhotoUrl()));
            ref.child("Usuarios").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    txtNome.setText(usuario.getNome());
                    txtFuncao.setText(usuario.getFuncao());
                    Glide.with(PerfilActivity.this).load(user.getPhotoUrl()).into(imagemPerfil);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.err.println("Erro de leitura: " + databaseError.getCode());

                }
            });
        }
        else {
            finish();
        }
    }

    protected void onStart(){
        super.onStart();
        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_provisorio, menu);
        return true;
    }

    public void logout(MenuItem item){
        mAuth.signOut();
        finish();
    }

    private void showProgress() {

    }

    private void hideProgress() {

    }

}
