package com.efeyegitoglu.instagram.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.efeyegitoglu.instagram.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.theartofdev.edmodo.cropper.CropImage;

public class LoginActivity extends AppCompatActivity {

    Button login_button;
    EditText input_login_password, input_login_mail;
    TextView kaydolText;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tanimlamalar();
    }

    void tanimlamalar() {
        input_login_mail = findViewById(R.id.input_login_mail);
        input_login_password = findViewById(R.id.input_login_password);
        login_button = findViewById(R.id.login_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String mail = input_login_mail.getText().toString();
                String password = input_login_password.getText().toString();

                if (!mail.equals("") && !password.equals("")) {
                    LogIn(mail, password);
                } else {
                    Toast.makeText(getApplicationContext(), "Bilgileri Kontrol Ediniz", Toast.LENGTH_LONG).show();
                }

            }
        });

        kaydolText = findViewById(R.id.kaydolText);
        kaydolText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);

            }
        });



    }

    void LogIn(String mail, String password) {

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Giriş Yapılıyor");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Hata Meydana Geldi", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }

            }
        });

    }
}
