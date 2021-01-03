package com.efeyegitoglu.instagram.Profile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.efeyegitoglu.instagram.R;
import com.efeyegitoglu.instagram.Utils.ChangeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class EditProfileFragment extends Fragment {

    View view;
    ImageView closeImage, kaydetTik;
    CircleImageView profile_image_edit;
    TextView fotografDegis, mailText;
    EditText input_name, input_userName, input_website, input_bio, input_number, input_cinsiyet;

    DatabaseReference referenceProfil, referenceKayit, referenceResim;
    FirebaseAuth auth;
    FirebaseUser user;
    StorageReference storageReference;


    //Resim işlemleri tanım
    Bitmap selectedImage;
    Uri imageData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        tanimlamalar();
        profilBilgiGetir();
        kullaniciKayitBilgiGetir();
        profilResmiGetir();

        bosResimGuncelle();

        return view;
    }

    void tanimlamalar() {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        referenceKayit = FirebaseDatabase.getInstance().getReference().child("KullaniciKayitBilgi").child(auth.getUid());
        referenceProfil = FirebaseDatabase.getInstance().getReference().child("ProfilBilgi").child(auth.getUid());
        storageReference = FirebaseStorage.getInstance().getReference();
        referenceResim = FirebaseDatabase.getInstance().getReference().child("ProfileImages").child(auth.getUid());

        kaydetTik = view.findViewById(R.id.kaydetTik);
        profile_image_edit = view.findViewById(R.id.profile_image_edit);
        fotografDegis = view.findViewById(R.id.fotografDegis);
        input_name = view.findViewById(R.id.input_name);
        input_userName = view.findViewById(R.id.input_userName);
        input_website = view.findViewById(R.id.input_website);
        input_bio = view.findViewById(R.id.input_bio);
        mailText = view.findViewById(R.id.mailText);
        input_number = view.findViewById(R.id.input_number);
        input_cinsiyet = view.findViewById(R.id.input_cinsiyet);
        closeImage = view.findViewById(R.id.closeImage);
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment = new ChangeFragment(getContext());
                changeFragment.change(new ProfileFragment());
            }
        });

        kaydetTik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilBilgiGüncelle();
                userNameDegistr();
                bosResimGuncelle();
            }
        });

        profile_image_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resmeTikla();
            }
        });

        fotografDegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilResmiKaydet();
            }
        });
    }

    void profilBilgiGüncelle() {

        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Bilgileriniz Güncelleniyor");
        progressDialog.show();


        String isim = input_name.getText().toString();
        String website = input_website.getText().toString();
        String bio = input_bio.getText().toString();
        String telNo = input_number.getText().toString();
        String cinsiyet = input_cinsiyet.getText().toString();


        Map map = new HashMap();
        //map.put("isim", isim);
        map.put("website", website);
        map.put("bio", bio);
        map.put("telNo", telNo);
        map.put("cinsiyet", cinsiyet);
        referenceProfil.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Bilgileriniz Güncellendi", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Güncelleme Başarısız", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    void profilBilgiGetir() {
        referenceProfil.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProfilBilgiModel pbm = dataSnapshot.getValue(ProfilBilgiModel.class);

                //input_name.setText(pbm.getIsim());
                input_website.setText(pbm.getWebsite());
                input_bio.setText(pbm.getBio());
                input_number.setText(pbm.getTelNo());
                input_cinsiyet.setText(pbm.getCinsiyet());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    void kullaniciKayitBilgiGetir() {

        referenceKayit.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                KullaniciKayitModel kpkm = dataSnapshot.getValue(KullaniciKayitModel.class);


                input_name.setText(kpkm.getIsim());
                input_userName.setText(kpkm.getUserName());
                mailText.setText(kpkm.getMail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    void profilResmiGetir() {

        referenceResim.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ProfileResmiModel profilResmiModel = dataSnapshot.getValue(ProfileResmiModel.class);

                if (!profilResmiModel.getResim().equals("")) {

                    //picasso burda
                    Picasso.get().load(profilResmiModel.getResim()).into(profile_image_edit);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    void userNameDegistr() {
        ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Bilgileriniz Güncelleniyor");
        progressDialog.show();

        Map map = new HashMap<>();
        map.put("isim",input_name.getText().toString());
        map.put("userName", input_userName.getText().toString());
        map.put("mail", user.getEmail());
        referenceKayit.setValue(map);

       progressDialog.dismiss();
    }


    void resmeTikla() {

        //izin verilmemişse
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // izin iste(izin verilince yapılıcaklar aşağıda)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {

            //izin verildiyse galeriyi aç
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery, 2);
        }
    }


    //izin verildiyse yapılacaklar (Bir kere döner,izin istenir,izin verilirse galeriye gider)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //kod 1 ise yani izin verildiyse
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery, 2);
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //galeriye gittiyse ve resmi seçtiyse ve resim dönüyorsa
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            //resmi uri olarak ata (resmin url si gibi)
            imageData = data.getData();

            try {

                //sdk versiyon 28 ve üzeri ise
                if (Build.VERSION.SDK_INT >= 28) {
                    //seçilen resmi bitmap dönüştürme alttaki eski versiyon yenisinde imageDecoder kullanılıyor
                    ImageDecoder.Source source = ImageDecoder.createSource(getContext().getContentResolver(), imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    profile_image_edit.setImageBitmap(selectedImage);
                } else {
                    //seçilen resmi bitmape dönüştür ve ata
                    selectedImage = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageData);

                    //bitmape döüşen resmi profil fotosuna ata
                    profile_image_edit.setImageBitmap(selectedImage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    void profilResmiKaydet() {

        if (imageData != null) {

            final ProgressDialog progressDialog=new ProgressDialog(getContext());
            progressDialog.setMessage("Bilgileriniz Güncelleniyor");
            progressDialog.show();

            Bitmap smallProfileImage = makeSmallerImage(selectedImage, 200);


            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            smallProfileImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] byteArray = outputStream.toByteArray();


            //resim url si boş değilse yani seç,m seçilmiş ise


            /*
            //universal uniq id (Rastgele id oluşturur)
            UUID uuid=UUID.randomUUID();
            //yeni resimler paylaşmak için .jpg öcesine uuid yi koy. (üstüne yazmak yeni resim olarak kayıt eder)
            */


            final String imageFileYolu = "profileImages/" + auth.getUid() + "/" + "profileImage.jpg";

            //veri tabanına bağlan resmi kullanıcı id sine yükle
            storageReference.child(imageFileYolu).putBytes(byteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //Download url alınacak resim gösterilecek

                    //Resmin yolu bulundu ve resim alındı
                    final StorageReference newStoredgeReference = FirebaseStorage.getInstance().getReference(imageFileYolu);
                    newStoredgeReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            //resmin url si alındı ve atandı
                            String downloadUrl = uri.toString();

                            Map map = new HashMap();
                            map.put("id",auth.getUid());
                            map.put("resim", downloadUrl);
                            map.put("userName",input_userName.getText().toString());
                            map.put("isim",input_name.getText().toString());
                            referenceResim.setValue(map);

                            progressDialog.dismiss();

                            Toast.makeText(getContext(), "Resim Güncellendi", Toast.LENGTH_LONG).show();

                            //NOT ZAMAN: FieldValue.serverTimestamp


                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Hata Meydana Geldi", Toast.LENGTH_LONG).show();
                }
            });

        } else {

            bosResimGuncelle();


        }


    }


    public Bitmap makeSmallerImage(Bitmap image, int maximumSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {
            width = maximumSize;
            height = (int) (width / bitmapRatio);

        } else {

            height = maximumSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);

    }

    void bosResimGuncelle(){
        referenceResim.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ProfileResmiModel profilResmiModel=dataSnapshot.getValue(ProfileResmiModel.class);


                Map map =new HashMap();
                map.put("id",auth.getUid());
                map.put("userName",input_userName.getText().toString());
                map.put("isim",input_name.getText().toString());
                map.put("resim",profilResmiModel.getResim());
                referenceResim.setValue(map);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
