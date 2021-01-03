package com.efeyegitoglu.instagram.Share;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.efeyegitoglu.instagram.Home.HomeFragment;
import com.efeyegitoglu.instagram.R;
import com.efeyegitoglu.instagram.Utils.ChangeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class GalleryFragment extends Fragment {

    View view;
    Button paylasBtn;
    ImageView selectPhoto;
    EditText aciklamaEdit;

    DatabaseReference reference;
    FirebaseAuth auth;
    StorageReference storageReference;

    //Resim işlemleri tanım
    Bitmap selectedImage;
    Uri imageData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_gallery, container, false);

        tanimlamalar();

        return view;
    }

    private void tanimlamalar() {

        reference=FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();


        aciklamaEdit = view.findViewById(R.id.aciklamaEdit);
        selectPhoto = view.findViewById(R.id.selectPhoto);
        paylasBtn = view.findViewById(R.id.paylasBtn);
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ResimSec();
            }
        });
        paylasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResimPaylas();
            }
        });

        crop();


    }

    private void crop(){
        CropImage.activity().start(getContext(), this);
    }

    void ResimSec() {

        //izin verilmemişse
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // izin iste(izin verilince yapılıcaklar aşağıda)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {

            //izin verildiyse galeriyi aç
            /*Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery, 2);*/
            crop();
        }
    }

    //izin verildiyse yapılacaklar (Bir kere döner,izin istenir,izin verilirse galeriye gider)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //kod 1 ise yani izin verildiyse
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /*Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery, 2);*/
                crop();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //galeriye gittiyse ve resmi seçtiyse ve resim dönüyorsa
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            CropImage.ActivityResult activityResult=CropImage.getActivityResult(data);


            //resmi uri olarak ata (resmin url si gibi)
            imageData = activityResult.getUri();

            try {

                //sdk versiyon 28 ve üzeri ise
                if (Build.VERSION.SDK_INT >= 28) {
                    //seçilen resmi bitmap dönüştürme alttaki eski versiyon yenisinde imageDecoder kullanılıyor
                    ImageDecoder.Source source = ImageDecoder.createSource(getContext().getContentResolver(), imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    selectPhoto.setImageBitmap(selectedImage);
                } else {
                    //seçilen resmi bitmape dönüştür ve ata
                    selectedImage = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageData);

                    //bitmape döüşen resmi profil fotosuna ata
                    selectPhoto.setImageBitmap(selectedImage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    void ResimPaylas() {

        if (imageData != null) {

            final ProgressDialog progressDialog=new ProgressDialog(getContext());
            progressDialog.setMessage("Paylaşılıyor");
            progressDialog.show();

            Bitmap smallProfileImage = makeSmallerImage(selectedImage);


            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            smallProfileImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] byteArray = outputStream.toByteArray();


            //resim url si boş değilse yani seç,m seçilmiş ise


            /*
            //universal uniq id (Rastgele id oluşturur)
            UUID uuid=UUID.randomUUID();
            //yeni resimler paylaşmak için .jpg öcesine uuid yi koy. (üstüne yazmaz yeni resim olarak kayıt eder)
            */

            final UUID uuid = UUID.randomUUID();

            final String imageFileYolu = "PostPhotos/" + auth.getUid() + "/" + uuid;

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

                            String postid=reference.child("Posts").push().getKey();

                            Map map = new HashMap();
                            map.put("postid",postid);
                            map.put("id", auth.getUid());
                            map.put("resim", downloadUrl);
                            map.put("comment", aciklamaEdit.getText().toString());
                            reference.child("Posts").child(postid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Paylaşıldı", Toast.LENGTH_LONG).show();
                                        ChangeFragment changeFragment=new ChangeFragment(getContext());
                                        changeFragment.change(new HomeFragment());
                                    }

                                }
                            });




                            //NOT ZAMAN: FieldValue.serverTimestamp


                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Hata Meydana Geldi", Toast.LENGTH_LONG).show();
                }
            });

        } else {

            Toast.makeText(getContext(), "Fotoğraf Seçmediniz", Toast.LENGTH_LONG).show();
        }
    }


    public Bitmap makeSmallerImage(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {
            width = selectPhoto.getWidth();
            height = (int) (width / bitmapRatio);

        } else {

            height = 450;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);

    }

}
