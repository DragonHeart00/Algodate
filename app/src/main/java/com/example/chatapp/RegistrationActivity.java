package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    private Button register;
    private EditText email, password, name;
    private RadioGroup radioGroup;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null){
                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };



        register = findViewById(R.id.registerbt);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        radioGroup = findViewById(R.id.radio_group);
        name = findViewById(R.id.name);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkemail = email.getText().toString();
                String checkPassword = password.getText().toString();
                String checkName = name.getText().toString();
                if (TextUtils.isEmpty(checkemail)) {
                    email.setError("missed me!");
                    return;
                }else if(TextUtils.isEmpty(checkPassword)){
                    password.setError("missed me!");
                    return;

                }else if(TextUtils.isEmpty(checkName)){
                    name.setError("missed me!");
                    return;

                }else {

                    int selectId = radioGroup.getCheckedRadioButtonId();
                    final RadioButton radioButton = findViewById(selectId);

                    if (radioButton.getText() == null){
                        return;
                    }

                    final String mEmail = email.getText().toString();
                    final String mPassword = password.getText().toString();
                    final String mName = name.getText().toString();
                    firebaseAuth.createUserWithEmailAndPassword(mEmail, mPassword).
                            addOnCompleteListener(RegistrationActivity.this,
                                    new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(RegistrationActivity.this, "sign up error!", Toast.LENGTH_SHORT).show();
                                            }else {
                                                String userId = firebaseAuth.getCurrentUser().getUid();
                                                DatabaseReference currentUserDB = FirebaseDatabase.getInstance().getReference().
                                                        child("Users").
                                                        child(radioButton.getText().toString()).
                                                        child(userId).
                                                        child("name");

                                                currentUserDB.setValue(mName);
                                            }

                                        }
                                    });


                }



            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthStateListener);
            }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}