package com.example.esp32_homemonitoringapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword,editTextPhoneNumber;
    Button buttonReg;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword=findViewById(R.id.password);
        buttonReg = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginNow);
        editTextPhoneNumber = findViewById(R.id.phone);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // read data from textfield in strings
                String email,password,phoneNumber;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                phoneNumber = String.valueOf(editTextPhoneNumber.getText());
                progressBar.setVisibility(View.VISIBLE);

                //Check if password is empty or not

                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(RegisterActivity.this,"Enter email",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(RegisterActivity.this,"Enter password",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(phoneNumber))
                {
                    Toast.makeText(RegisterActivity.this,"Enter phone number",Toast.LENGTH_SHORT).show();
                    return;
                }
                checkCredentials();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {

                                    Toast.makeText(RegisterActivity.this, "Account Created !",
                                            Toast.LENGTH_SHORT).show();


                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }

    private void checkCredentials() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String phone = editTextPhoneNumber.getText().toString();

        if(email.isEmpty() || !email.contains("@") || email.length()<11)
        {
            showError1(editTextEmail,"Your email is not valid");
        }
        if(password.isEmpty() || password.length()<7)
        {
            showError2(editTextPassword,"Password must be 7 characters");
        }
        if(phone.isEmpty() || phone.length()!=10 || !phone.contains("7"))
        {
            showError3(editTextPhoneNumber,"Phone number must have 10 characters");
        }
        if(email.contains("@") && password.length()>=7 && phone.length()==10 && (email.endsWith("@gmail.com") || email.endsWith("@yahoo.com")))
        {
            Toast.makeText(this,"Call registration method", Toast.LENGTH_SHORT).show();
        }

    }

    private void showError1(TextInputEditText input, String s) {
        editTextEmail.setError(s);
        input.setError(s);
        input.requestFocus();
    }

    private void showError2(TextInputEditText input, String s) {
        editTextPassword.setError(s);
        input.setError(s);
        input.requestFocus();
    }

    private void showError3(TextInputEditText input, String s) {
        editTextPhoneNumber.setError(s);
        input.setError(s);
        input.requestFocus();
    }
}

