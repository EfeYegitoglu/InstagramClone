package com.efeyegitoglu.instagram.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.efeyegitoglu.instagram.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText input_register_userName, input_register_mail, input_register_password;
    TextView girisYapText;
    Button register_button;

    FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tanimlamalar();
    }

    void tanimlamalar() {
        input_register_userName = findViewById(R.id.input_register_userName);
        input_register_mail = findViewById(R.id.input_register_mail);
        input_register_password = findViewById(R.id.input_register_password);
        girisYapText = findViewById(R.id.girisYapText);
        register_button = findViewById(R.id.register_button);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = input_register_userName.getText().toString();
                String mail = input_register_mail.getText().toString();
                String password = input_register_password.getText().toString();


                if (!userName.equals("") && !mail.equals("") && !password.equals("")) {
                    register(mail, password);
                } else {
                    Toast.makeText(getApplicationContext(), "Bilgileri Kontrol Ediniz", Toast.LENGTH_LONG).show();
                }
            }
        });


        girisYapText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void register(String mail, String password) {

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Giriş Yapılıyor");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {


                    String userName = input_register_userName.getText().toString();
                    String mail = input_register_mail.getText().toString();


                    DatabaseReference referenceKayit = FirebaseDatabase.getInstance().getReference()
                            .child("KullaniciKayitBilgi").child(auth.getUid());

                    Map map = new HashMap();
                    map.put("isim", "");
                    map.put("userName", userName);
                    map.put("mail", mail);
                    referenceKayit.setValue(map);


                    DatabaseReference referenceProfil = FirebaseDatabase.getInstance().getReference()
                            .child("ProfilBilgi").child(auth.getUid());
                    Map map1 = new HashMap();
                    //map1.put("isim", "");
                    map1.put("website", "");
                    map1.put("bio", "");
                    map1.put("telNo", "");
                    map1.put("cinsiyet", "");
                    referenceProfil.setValue(map1);


                    DatabaseReference referenceProfileImages = FirebaseDatabase.getInstance().getReference()
                            .child("ProfileImages").child(auth.getUid());

                    Map map2=new HashMap();
                    map2.put("id",auth.getUid());
                    map2.put("userName",input_register_userName.getText().toString());
                    map2.put("isim","");
                    map2.put("resim","");
                    referenceProfileImages.setValue(map2);


                    progressDialog.dismiss();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Mail Kullanılıyor", Toast.LENGTH_LONG).show();
                }

            }
        });


    }
}
