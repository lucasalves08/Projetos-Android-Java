package com.example.lsantos.sistemacadastro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setTitle("Dashboard");
    }

    public void cadastrar(View v){
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);

    }

    public void listar(View v){
        Intent intent = new Intent(this, ListaActivity.class);
        startActivity(intent);

    }
}
