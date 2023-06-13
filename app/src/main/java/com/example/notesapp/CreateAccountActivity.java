package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText, repeatPasswordEditText;
    MaterialButton registerBtn;
    ProgressBar progressBar;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailEditText = findViewById(R.id.inputEmail);
        passwordEditText = findViewById(R.id.inputPassword);
        repeatPasswordEditText = findViewById(R.id.inputConfirmPassword);

        registerBtn = findViewById(R.id.save_btn);

        progressBar = findViewById(R.id.progressBar2);

        login = findViewById(R.id.login_text);

        registerBtn.setOnClickListener(view -> createAccount());
        login.setOnClickListener(view -> startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class)));
    }

    public void createAccount(){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String rep_password = repeatPasswordEditText.getText().toString();

        boolean isValidated = validateInputs(email, password, rep_password);

        if(!isValidated){
            return;
        }

        createAccountInFirebase(email, password, rep_password);

    }

    public void   createAccountInFirebase(String email, String password, String rep_password){
        inProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                inProgress(false);

                if(task.isSuccessful()){
                    //account success
                    ToastHelper.showToast(CreateAccountActivity.this, "Account created Successfully, Verify Account Via Email");
                    firebaseAuth.getCurrentUser().sendEmailVerification();
                    firebaseAuth.signOut();
                    finish();

                }else{
                    //account failure
                ToastHelper.showToast(CreateAccountActivity.this, task.getException().getLocalizedMessage());

                }
            }
        });

    }

    void inProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            registerBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            registerBtn.setVisibility(View.VISIBLE);
        }
    }

    public Boolean validateInputs(String email, String password, String rep_password){

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Invalid Email");
            return false;
        }

        if(password.length() < 8){
            passwordEditText.setError("Password Length should be greater than 7");
        }



        if(!password.equals(rep_password)){
            repeatPasswordEditText.setError("Password does not match");
            return false;
        }

        return true;
    }

}