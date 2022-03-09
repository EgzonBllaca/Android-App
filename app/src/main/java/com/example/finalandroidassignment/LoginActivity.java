package com.example.finalandroidassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText txtEmail;
    private EditText txtPassword;
    private FirebaseAuth auth;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtEmail = findViewById(R.id.editTextEmail);
        txtPassword = findViewById(R.id.editTextPassword);
        auth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.btnLogin);
    }

    public void login(View view){
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        if (email.isEmpty() || password.isEmpty())
            Toast.makeText(this, "Empty credentials", Toast.LENGTH_SHORT).show();
        else if (password.length()<8)
            Toast.makeText(this, "Password must have at least 8 characters", Toast.LENGTH_SHORT).show();
        else{
            loginUser(email,password);
        }
    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                txtPassword.setText("");
            }
        });


    }

    public void register(View view){
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        if (email.isEmpty() || password.isEmpty())
            Toast.makeText(this, "Empty credentials", Toast.LENGTH_SHORT).show();
        else if (password.length()<8)
            Toast.makeText(this, "Password must have at least 8 characters", Toast.LENGTH_SHORT).show();
        else{
            registerUser(email,password);
            btnLogin.performClick();
        }
    }

    private void registerUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                    Toast.makeText( LoginActivity.this,"Registered successfully", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(LoginActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}