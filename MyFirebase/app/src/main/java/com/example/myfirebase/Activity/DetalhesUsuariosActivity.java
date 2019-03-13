package com.example.myfirebase.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.example.myfirebase.Modelo.Usuario;
import com.example.myfirebase.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DetalhesUsuariosActivity extends AppCompatActivity {

    private EditText editNome, editCPF, editTelefone;
    private Spinner sexo;
    private Button btnSalvar;
    private boolean mudou = false;

    private String sexos[] = {"M", "F"};
    private String sexoEscolhido, funcao;

    private static final int CHOOSE_IMAGE = 101;
    private Uri uri;
    private Uri fotoUsuario;

    private ProgressDialog bar;
    ImageView imagemPerfil;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference reference;
    private FirebaseUser user;
    private FirebaseStorage mStorage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhes_usuarios_activity);
        setTitle("Detalhes");

        //Views
        editNome = findViewById(R.id.editNomeP);
        editCPF = findViewById(R.id.editCPFP);
        editTelefone = findViewById(R.id.editTelefoneP);
        imagemPerfil = findViewById(R.id.imagePerfil);

        // Progress
        bar = new ProgressDialog(this);
        //Spinner
        sexo = findViewById(R.id.sexo);
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sexos);
        sexo.setAdapter(adapter);

        // Button
        btnSalvar = findViewById(R.id.btnSalvarAlteracoes);

        // Quando criar a Intent
        Aocarregar();

        //Cliar no button
        eventoClick();




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == CHOOSE_IMAGE) {
                uri = data.getData();
            }
        }
        Glide.with(DetalhesUsuariosActivity.this).load(uri).into(imagemPerfil);
    }

    private void eventoClick() {
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Usuario u = new Usuario();
                u.setId(user.getUid());
                u.setEmail(user.getEmail());
                u.setNome(editNome.getText().toString());
                u.setCpf(editCPF.getText().toString());
                u.setTelefone(editTelefone.getText().toString());
                u.setSexo(sexoEscolhido);
                u.setFuncao(funcao);
                reference.child("Usuarios").child(user.getUid()).setValue(u);
                if(uri != null) {
                    //bar.setMessage("Salvando...");
                    //bar.show();

                    storageReference = storageReference.child("Fotos").child(user.getUid()).child(uri.getLastPathSegment());
                    storageReference.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();

                            }
                            return storageReference.getDownloadUrl();
                        }

                    }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
                            user.updateProfile(profile);
                            finish();
                            //Intent i = new Intent(DetalhesUsuariosActivity.this, PerfilActivity.class);
                            //startActivity(i);

                        }
                    });

                }

                finish();


            }
        });

        imagemPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(i.createChooser(i,"Escolha uma imagem"),CHOOSE_IMAGE);
            }

        });

    }

    private void Aocarregar() {
        IniciarFirebase();

        //Buscar dados no banco
        if (user != null){
            Glide.with(DetalhesUsuariosActivity.this).load(user.getPhotoUrl()).into(imagemPerfil);
            reference.child("Usuarios").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Usuario u = dataSnapshot.getValue(Usuario.class);
                    editNome.setText(u.getNome());
                    editCPF.setText(u.getCpf());
                    editTelefone.setText(u.getTelefone());
                    funcao = u.getFuncao();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                   Log.d("Erro de leitura", String.valueOf(databaseError.getMessage()));

                }
            });

        }
        else {
            finish();
        }

        // Spinner M, F
        sexo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        sexoEscolhido = "M";
                        break;
                    case 1:
                        sexoEscolhido = "M";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void IniciarFirebase(){
        //Instanciar Firebase
        mDatabase = FirebaseDatabase.getInstance();
        reference = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance();
        storageReference = mStorage.getReference();


    }
}
