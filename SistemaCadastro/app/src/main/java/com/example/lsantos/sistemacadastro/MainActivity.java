package com.example.lsantos.sistemacadastro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login");


    }

    public void logar(View v){

        TextView txt1 = findViewById(R.id.txtLogin);
        TextView txt2 = findViewById(R.id.txtSenha);

        String login_temp = txt1.getText().toString();
        String senha_temp = txt2.getText().toString();

        if(login_temp.equals("Lucas") && senha_temp.equals("123")){
            alert("Logado com sucesso!");
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        }else {
            alert("Não foi possivel realizar o login! ");
        }


    }
    public void alert(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }





}
