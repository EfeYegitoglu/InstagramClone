package com.efeyegitoglu.instagram.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.efeyegitoglu.instagram.Home.HomeFragment;
import com.efeyegitoglu.instagram.R;
import com.efeyegitoglu.instagram.Utils.ChangeFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddStoryActivity extends AppCompatActivity {

    //Resim işlemleri tanım
    Bitmap selectedImage;
    Uri imageData;

    FirebaseAuth auth;
    StorageReference storageReference;

    ImageView resimBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);

        tanimlamalar();
        ResimSec();
    }

    private void tanimlamalar() {
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        crop();
        resimBitmap=findViewById(R.id.resimBitmap);


    }

    private void crop() {
        CropImage.activity().setAspectRatio(9, 16).start(AddStoryActivity.this);
    }

    void ResimSec() {

        //izin verilmemişse
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // izin iste(izin verilince yapılıcaklar aşağıda)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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

            CropImage.ActivityResult activityResult = CropImage.getActivityResult(data);


            //resmi uri olarak ata (resmin url si gibi)
            imageData = activityResult.getUri();

            try {

                //sdk versiyon 28 ve üzeri ise
                if (Build.VERSION.SDK_INT >= 28) {
                    //seçilen resmi bitmap dönüştürme alttaki eski versiyon yenisinde imageDecoder kullanılıyor
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);

                    ResimPaylas();

                } else {
                    //seçilen resmi bitmape dönüştür ve ata
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageData);

                    ResimPaylas();


                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    void ResimPaylas() {

        if (imageData != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Paylaşılıyor");
            progressDialog.show();

            Bitmap smallSizeImage=makeSmallerImage(selectedImage);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            smallSizeImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] byteArray = outputStream.toByteArray();

            /*
            //universal uniq id (Rastgele id oluşturur)
            UUID uuid=UUID.randomUUID();
            //yeni resimler paylaşmak için .jpg öcesine uuid yi koy. (üstüne yazmaz yeni resim olarak kayıt eder)
            */

            //final UUID uuid = UUID.randomUUID();


            final String imageFileYolu = "Story/" + System.currentTimeMillis();

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

                            DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Story").child(auth.getUid());

                            String storyId = reference.child("Posts").push().getKey();
                            long timeEnd=System.currentTimeMillis()+86400000;

                            HashMap<String,Object> map=new HashMap<>();
                            map.put("imageUrl",downloadUrl);
                            map.put("timeStart", ServerValue.TIMESTAMP);
                            map.put("timeEnd",timeEnd);
                            map.put("storyId",storyId);
                            map.put("userId",auth.getUid());

                            reference.child(storyId).setValue(map);
                            progressDialog.dismiss();
                            finish();

                            //NOT ZAMAN: FieldValue.serverTimestamp
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AddStoryActivity.this, "Hata Meydana Geldi", Toast.LENGTH_LONG).show();
                }
            });

        } else {

            Toast.makeText(AddStoryActivity.this, "Fotoğraf Seçmediniz", Toast.LENGTH_LONG).show();
        }
    }

    public Bitmap makeSmallerImage(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {
            width = resimBitmap.getWidth();
            height = (int) (width / bitmapRatio);

        } else {

            height = resimBitmap.getHeight();
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);

    }


}
