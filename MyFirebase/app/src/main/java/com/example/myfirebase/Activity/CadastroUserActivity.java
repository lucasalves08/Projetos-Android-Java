package com.example.myfirebase.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myfirebase.Modelo.Usuario;
import com.example.myfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroUserActivity extends AppCompatActivity {

    private EditText editEmail, editSenha, editConfir, editNome;
    private Button btnRegistrar;

    //Usuario
    private Usuario usuario = null;

    //Firebase
    private FirebaseAuth mAuth;

    FirebaseDatabase mDatabase;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_user);

        //Views
        editEmail = findViewById(R.id.editEmailc);
        editSenha = findViewById(R.id.editSenhac);
        editConfir = findViewById(R.id.editSenhaConfir);
        editNome = findViewById(R.id.editNomec);

        //Buttons
        btnRegistrar = findViewById(R.id.btnRegistrar);
        eventoClicks();

        //Firebase
        mDatabase = FirebaseDatabase.getInstance();
        reference = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    private void eventoClicks() {
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String senha = editSenha.getText().toString();
                String nome = editNome.getText().toString();
                String senhaConfir = editConfir.getText().toString();
                if(senha.equals(senhaConfir)){
                    criarUser(email, senha, nome);
                }
                else {
                    alert("Senha divergentes!");
                    alert(nome);
                }


            }
        });

    }



    //Criar Usuario Firebase
    private void criarUser(final String email, String senha, final String nome){
        mAuth.createUserWithEmailAndPassword(email,senha).addOnCompleteListener(CadastroUserActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser user = mAuth.getCurrentUser();
                    usuario = new Usuario();
                    usuario.setId(user.getUid());
                    usuario.setNome(nome);
                    usuario.setEmail(email);
                    usuario.setCpf("");
                    usuario.setTelefone("");
                    usuario.setNivel(0);
                    usuario.setSexo("");
                    usuario.setFuncao("");
                    //Salva no banco
                    reference.child("Usuarios").child(usuario.getEmail()).setValue(usuario);

                    //Envia email de verificação
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                alert("Foi enviado um e-mail de verificação");
                            }
                        }
                    });
                    finish();
                }
                else {
                    alert("Erro de cadastro");
                }


            }
        });
    }

    public void alert(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

}
