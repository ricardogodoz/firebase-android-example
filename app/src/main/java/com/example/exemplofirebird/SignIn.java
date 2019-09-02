package com.example.exemplofirebird;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;

public class SignIn extends AppCompatActivity {

    // Authentication attribute
    private FirebaseAuth mAuth;

    // XML layout attributes
    private Button btn_login, btn_registrar;
    private EditText edt_email, edt_password;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // Start authentication instance
        mAuth = FirebaseAuth.getInstance();

        // Attributes receive the object related to its own ID of XML layout
        btn_login = findViewById(R.id.btn_login);
        btn_registrar = findViewById(R.id.btn_registrar);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        progress = findViewById(R.id.progress);

        // When click on login button
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get email and password of edit text inputs
                String email = edt_email.getText().toString().trim();
                String password = edt_password.getText().toString().trim();

                // If email is not informed the application shows an error to the user
                if (email.equals("")) {
                    Toast.makeText(getApplicationContext(), "Nome é obrigatório", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Password validation
                if (password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Senha é obrigatório", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Progress bar get visible
                progress.setVisibility(View.VISIBLE);

                // Disable the login button
                btn_login.setEnabled(false);

                // Try to login with the e-mail and password
                mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // When complete the request the progrees bar is hidden and login button is enabled
                        progress.setVisibility(View.GONE);
                        btn_login.setEnabled(true);

                        // If the login was successful
                        if (task.isSuccessful()) {

                            // Close the current activity (SignIn)
                            finish();

                            // Start a new activity (Home)
                            Intent intent = new Intent(getApplicationContext(), Home.class);
                            startActivity(intent);

                        } else {

                            // If the login was not successful the application shows an error to the user
                            Toast.makeText(getApplicationContext(), "Usuário e senha não encontrados", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        // When you click on register button
        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Start a new activity(Home)
                // Its not needed to finish the current activity because the user can click on back button to login
                // Once you finish the activity you can not back to it
                Intent intent = new Intent(getApplicationContext(), SingUp.class);
                startActivity(intent);
            }
        });

    }

    // When the activity start
    @Override
    public void onStart() {
        super.onStart();

        // Get the current authenticated user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // If there is not a logged in user, the currentUser value is null
        if (currentUser != null) {

            // Close the current activity (SignIn)
            finish();

            // Start a new activity (Home)
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
        }
    }
}
