package com.example.myfirebase.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail, editSenha;
    private Button btnLogin, btnNovo;
    private TextView textEsqueceu, textEmailV;

    private FirebaseAuth mAuth;
    private FirebaseUser user;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        // Buttons
        btnLogin = findViewById(R.id.btnLogin);
        btnNovo = findViewById(R.id.btnNovoUser);

        //Views
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        textEsqueceu = findViewById(R.id.textEsqueceu);
        textEmailV = findViewById(R.id.textVerificar);

        mAuth = FirebaseAuth.getInstance();
        user  = mAuth.getCurrentUser();

        if(user != null) {
            Intent i = new Intent(LoginActivity.this, PerfilActivity.class);
            startActivity(i);
        }

        eventoClicks();



    }

    private void eventoClicks() {
        btnNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CadastroUserActivity.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString().trim();
                String senha = editSenha.getText().toString().trim();
                login(email,senha);


            }
        });

        textEsqueceu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ResetSenhaActivity.class);
                startActivity(i);
            }
        });
    }

    private void login(String email, String senha) {
        if(email.isEmpty()){
            editEmail.setError("Insira o e-mail");
            editEmail.requestFocus();
            return;
        }
        if(senha.isEmpty()){
            editSenha.setError("Insira a senha");
            editSenha.requestFocus();
            return;
        }
        final ProgressDialog login = new ProgressDialog(this);
        login.setMessage("Logando...");
        login.show();
        mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    login.dismiss();
                    if(user.isEmailVerified()) {
                        Intent intent = new Intent(LoginActivity.this, PerfilActivity.class);
                        startActivity(intent);
                    }
                    else {
                        textEmailV.setText("E-mail não verificado");
                    }
                }
                else {
                    login.dismiss();
                    alert("E-mail ou senha incorretos!");
                }
            }
        });
    }



    private void alert(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();

    }
}
