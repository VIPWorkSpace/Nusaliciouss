package com.workspace.nusali.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.workspace.nusali.Model.UserModel;
import com.workspace.nusali.R;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText textName, textPhone, textEmail, textPassword;
    Button btnRegister;
    DatabaseReference referenceRegister;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final LoadingDialog loadingDialog = new LoadingDialog(RegisterActivity.this);

        textName = findViewById(R.id.text_name);
        textPhone = findViewById(R.id.phone);
        textEmail = findViewById(R.id.text_email);
        textPassword = findViewById(R.id.text_password);
        btnRegister = findViewById(R.id.btn_register);


        referenceRegister = FirebaseDatabase.getInstance().getReference("User");
        firebaseAuth = FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissDialog();
                    }
                }, 4500);
                final String name = String.valueOf(textName.getText());
                final String phone = String.valueOf(textPhone.getText());
                final String email = String.valueOf(textEmail.getText());
                final String password = String.valueOf(textPassword.getText());

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Nama tidak valid",
                            Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(email) || email.length() < 5) {
                    Toast.makeText(getApplicationContext(), "Email tidak Valid",
                            Toast.LENGTH_LONG).show();
                }
                else if (TextUtils.isEmpty(phone) || phone.length() <= 11) {
                    Toast.makeText(getApplicationContext(), "telepon minimal 11 digit",
                            Toast.LENGTH_LONG).show();
                }
                else if (TextUtils.isEmpty(password)|| password.length() < 6){
                    Toast.makeText(getApplicationContext(), "password minimal 6 karakter",
                            Toast.LENGTH_SHORT).show();

                }
                else {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                UserModel userModel =new UserModel(name, phone, email, password);
                                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("pribadi").setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        String userId = "";
                                        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                        userId = currentUser.getUid();
                                        FirebaseDatabase.getInstance().getReference("User").child(userId).child("pribadi").child("saldo").setValue(50000);
                                        FirebaseDatabase.getInstance().getReference("User").child(userId).child("pribadi").child("url_foto").setValue("https://firebasestorage.googleapis.com/v0/b/nusalicious-ed650.appspot.com/o/avaDefault.jpg?alt=media&token=f8925c60-4d15-449c-931b-d5c5099fce46");


                                        Toast.makeText(RegisterActivity.this, "Daftar Sukses",
                                                Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                        finish();
                                    }
                                });
                            }
                            else {

                                Toast.makeText(RegisterActivity.this, "Email sudah terdaftar, silahkan login",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }

        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}