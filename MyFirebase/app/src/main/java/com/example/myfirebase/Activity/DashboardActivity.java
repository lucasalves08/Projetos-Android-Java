package com.example.myfirebase.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.myfirebase.R;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

/*
    private void eventoClicks() {
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PerfilActivity.this, CadastroPessoaActivity.class);
                startActivity(i);
            }
        });
        btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PerfilActivity.this, ListarPessoaActivity.class);
                startActivity(i);
            }
        });

        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);

            }
        });
    }

    protected void onStart(){
        super.onStart();
        mAuth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();
        verificaUser();
    }


    private void verificaUser() {
        if(user == null){
            finish();
        }
        else{
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(nome).build();
            user.updateProfile(profile);
            nome = user.getDisplayName();
            Toast.makeText(this, nome, Toast.LENGTH_SHORT).show();
            //txtEmail.setText("E-mail: "+nome);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_provisorio, menu);
        return true;
    }

    public void logout(MenuItem item){
        mAuth.signOut();
    } */
}
