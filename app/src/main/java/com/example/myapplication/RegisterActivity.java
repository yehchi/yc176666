package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText etRegEmail;
    TextInputEditText etRegPassword;
    TextInputEditText etRegPassword2;
    TextView tvLoginHere;

    Button btnRegister;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etRegEmail = (TextInputEditText)findViewById(R.id.etRegEmail);
        etRegPassword = (TextInputEditText) findViewById(R.id.etRegPass);
        etRegPassword2 = (TextInputEditText) findViewById(R.id.etRegPass2);
        tvLoginHere = (TextView) findViewById(R.id.tvLoginHere);
        btnRegister = (Button)findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(view ->{
            createUser();
        });

        tvLoginHere.setOnClickListener(view ->{
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }
    private void createUser(){
        String email = etRegEmail.getText().toString();
        String password = etRegPassword.getText().toString();
        String password2 = etRegPassword2.getText().toString();

        if (TextUtils.isEmpty(email)){
            etRegEmail.setError("Email cannot be empty");
            etRegEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            etRegPassword.setError("Password cannot be empty");
            etRegPassword.requestFocus();
        }else if (TextUtils.isEmpty(password2)){
            etRegPassword2.setError("CheckPassword cannot be empty");
            etRegPassword2.requestFocus();
        }else if (!password.equals(password2)){
            etRegPassword2.setError("CheckPassword does not match");
            etRegPassword2.requestFocus();
        }else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task1) {
                                if(task1.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this, "User registered successfully,Please Check your email for verification", Toast.LENGTH_SHORT).show();
                                    etRegEmail.setText("");
                                    etRegPassword.setText("");
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                }else{
                                    Toast.makeText(RegisterActivity.this, task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                        //Toast.makeText(MainActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(MainActivity.this, MainActivity2.class));
                    }else{
                        Toast.makeText(RegisterActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}