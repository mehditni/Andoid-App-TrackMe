package com.mouad.trackapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText username;
    MaterialEditText email;
    MaterialEditText password;
    Button register;
    FirebaseAuth auth;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().show();

        register=findViewById(R.id.btn_register);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        username=findViewById(R.id.username);
        auth=FirebaseAuth.getInstance();

        register.setOnClickListener(view -> {
            String txt_username=username.getText().toString();
            String txt_email=email.getText().toString();
            String txt_password=password.getText().toString();

            if(TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                Toast.makeText(RegisterActivity.this,"All fields are required",Toast.LENGTH_SHORT).show();

            }else if (txt_password.length()<6){
                Toast.makeText(RegisterActivity.this,"password must be at least 6 characters",Toast.LENGTH_SHORT).show();
            }else{
                register(txt_username,txt_email,txt_password);
            }

        });

    }

    private void register(String username,String email,String password){
        auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(task -> {
            Toast.makeText(RegisterActivity.this,"oncomplete",Toast.LENGTH_SHORT).show();
            if(task.isSuccessful()){
                Toast.makeText(RegisterActivity.this,"issuccessful",Toast.LENGTH_SHORT).show();
                FirebaseUser firebaseUser=auth.getCurrentUser();
                assert firebaseUser!=null;
                String userid = firebaseUser.getUid();
                reference=FirebaseDatabase.getInstance().getReference("Users").child(userid);
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("id",userid);
                hashMap.put("email",email);
                hashMap.put("username",username);
                hashMap.put("password",password);
                hashMap.put("imageURL","default");
                reference.setValue(hashMap).addOnCompleteListener(task1 -> {

                    if (task1.isSuccessful()) {
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }

                );
        }else {
                Toast.makeText(RegisterActivity.this,"you cannot register with this email or password",Toast.LENGTH_SHORT).show();

            }
        });
    }

}



