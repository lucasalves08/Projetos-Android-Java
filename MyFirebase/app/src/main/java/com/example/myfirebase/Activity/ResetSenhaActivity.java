package com.example.myfirebase.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetSenhaActivity extends AppCompatActivity {

    private EditText editEmail;
    private Button btnReset;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_senha);

        editEmail = findViewById(R.id.editEmailR);
        btnReset = findViewById(R.id.btnReset);

        eventoClick();

    }

    private void eventoClick() {
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString().trim();
                resetarSenha(email);
            }
        });
    }

    private void resetarSenha(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(ResetSenhaActivity.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    alert("Um e-mail foi enviado para alterar sua senha!");
                    finish();
                }
                else{
                    alert("E-mail não encontrado!");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
    }

    private void alert(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
