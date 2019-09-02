package com.example.exemplofirebird;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.exemplofirebird.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SingUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    private Button btn_registrar;
    private EditText edt_name, edt_email, edt_password, edt_confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        btn_registrar = findViewById(R.id.btn_registrar);

        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        edt_confirm_password = findViewById(R.id.edt_confirm_password);

        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String name = edt_name.getText().toString().trim();
                final String email = edt_email.getText().toString().trim();
                final String password = edt_password.getText().toString().trim();
                final String confirm_password = edt_confirm_password.getText().toString().trim();

                if (name.equals("")) {
                    Toast.makeText(getApplicationContext(), "Nome é obrigatório", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (email.equals("")) {
                    Toast.makeText(getApplicationContext(), "E-mail é obrigatório", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Senha é obrigatório", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (confirm_password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Confirmar senha é obrigatório", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirm_password)) {
                    Toast.makeText(getApplicationContext(), "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "A senha deve possuir no mínimo 6 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SingUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser firebaseUser = mAuth.getCurrentUser();

                            DatabaseReference userRef = database.getReference("users/" + firebaseUser.getUid());

                            User user = new User();

                            user.setEmail(email);
                            user.setName(name);

                            userRef.setValue(user);

                            finish();
                            Intent intent = new Intent(getApplicationContext(), Home.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Ocorreu um erro ao criar o usuário", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}
