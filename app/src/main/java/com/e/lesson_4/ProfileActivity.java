package com.e.lesson_4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private final int REQUEST_GALLERY = 101;
    EditText editText;
    ImageView imageView;
    private Uri uri;
    //private  Glide glide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editText = findViewById(R.id.editName);
        imageView = findViewById(R.id.imageView);
        getData();
    }

        private void getData() {
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String name= task.getResult().getString("name");
                    editText.setText(name);
                }

            }
        });
        }



    public void onClick(View view){
        String name =editText.getText().toString().trim();
        Map<String,Object>map=new HashMap<>();
        map.put("name", name);
        map.put("email", "chocolateoops@gmail.com");
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toaster.show("Успешно");
                        } else {
                            Toaster.show("Ошибка");
                        }
                    }
                });
    }


   // private void showAvatar(String avatar) {
       // Glide.with(this).load(avatar).apply(RequestOptions.circleCropTransform()).into(imageView);
   // }


    public void onClickImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_GALLERY) {
            uri = data.getData();
            imageView.setImageURI(uri);

        }
    }

    private void uploadImage() {
        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("avatars/avatar.jpg");
        Task<Uri> task = ref.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (task.isSuccessful()) {
                    return ref.getDownloadUrl();
                }

                return null;

            }

        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Toaster.show("Успешно");
                    saveUser(downloadUri);
                } else {
                    Toaster.show("Ощибка");

                }
            }
        });
    }


private void saveUser(Uri downloadUri) {
    String name = editText.getText().toString().trim();
    Map<String, Object> map = new HashMap<>();
    map.put("name", name);
    map.put("email", "chocolateoops@gmail.com");
    map.put("avatar", downloadUri.toString());
    FirebaseFirestore.getInstance()
            .collection("users")
            .document(FirebaseAuth.getInstance().getUid())
            .set(map)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toaster.show("Успешно");
                    } else {
                        Toaster.show("Ошибка");
                    }
                }
            });
}}
