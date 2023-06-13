package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    MaterialButton loginBtn;
    ProgressBar progressBar;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.inputEmail);
        passwordEditText = findViewById(R.id.inputPassword);


        loginBtn = findViewById(R.id.login_btn);

        progressBar = findViewById(R.id.progressBar3);

        login = findViewById(R.id.login_text);

        loginBtn.setOnClickListener(view -> loginAccount());
        login.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class)));

    }

    public void loginAccount(){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();


        loginFirebaseAccount(email, password);

    }

    public void loginFirebaseAccount(String email, String password){
        inProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                inProgress(false);

                if(task.isSuccessful()){
                    //login is success
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        //move to main activity
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }else{
                        ToastHelper.showToast(LoginActivity.this, "Email is not verify");
                    }

                }else {
                    ToastHelper.showToast(LoginActivity.this, task.getException().getLocalizedMessage());
                }

            }
        });


    }

    void inProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }


}