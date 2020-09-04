package com.example.postie11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsernameActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private Button removeBtn ,CreateAccountBtn;
    private EditText username;
    private ProgressBar progressBar;
    private TextView Skip;
    private Uri photoUri;

    public final static String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";
    private StorageReference storage;
    private FirebaseAuth firebaseAuth;
    private String url = "";
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);

        init();

        storage = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();



        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dexter.withActivity(UsernameActivity.this)
                        .withPermissions(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        ).withListener(new MultiplePermissionsListener() {
                    @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            selectImage();
                        }else{
                            Toast.makeText(UsernameActivity.this,"Please allow permission",Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();


            }
        });
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoUri=null;
                profileImageView.setImageResource(R.drawable.profile);

            }
        });
        CreateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setError(null);
                if(username.getText().toString().isEmpty()||username.getText().toString().length()<3){
                    username.setError("Username must be greater then 3 characters");
                    return;
                }
                if(!username.getText().toString().matches(USERNAME_PATTERN)){
                    username.setError(" Only \" a to z ,0 to 9,_and _-\"these characters are allowed");
                }

                progressBar.setVisibility(View.VISIBLE);
                firestore.getInstance().collection("users").whereEqualTo("username",username.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<DocumentSnapshot> document =task.getResult().getDocuments();
                            if(document.isEmpty()){
                                username.setError("username not found");
                                uploadData();
                                return;
                            }else{
                                progressBar.setVisibility(View.INVISIBLE);
                                username.setError("Already exists!");
                                return;
                            }
                        }else{
                            String error = task.getException().getMessage();
                            Toast.makeText(UsernameActivity.this,error,Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                    }
                });

            }
        });

    }

    private void init(){

        profileImageView = findViewById(R.id.profile_image);
        CreateAccountBtn = findViewById(R.id.create_account_btn);
        removeBtn = findViewById(R.id.remove_btn);
        progressBar = findViewById(R.id.progressbar);
        username = findViewById(R.id.username);
        Skip = findViewById(R.id.skip);


    }
    private  void selectImage(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityMenuIconColor(getResources().getColor(R.color.colorAccent))
                .setActivityTitle("Profile Photo")
                .setFixAspectRatio(true)
                .setAspectRatio(1,1)
                .start(this);

    }
    private void uploadData(){
        if(photoUri!=null){
            final StorageReference ref = storage.child("profiles/"+firebaseAuth.getCurrentUser().getUid());
            UploadTask uploadTask = ref.putFile(photoUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {

                        String error = task.getException().getMessage();
                        Toast.makeText(UsernameActivity.this,error,Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);

                        throw task.getException();

                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            url = uri.toString();
                        }
                    });
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        uploadusername();
                    } else {

                        String error = task.getException().getMessage();
                        Toast.makeText(UsernameActivity.this,error,Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        // Handle failures
                        // ...
                    }
                }
            });


        }else{
            uploadusername();

        }

    }
    private  void uploadusername(){
        Map<String , Object> map= new HashMap<>();
        map.put("username",username.getText().toString());
        map.put("profile_Url",url);
        firestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent mainIntent = new Intent(UsernameActivity.this,MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }else{
                    String error = task.getException().getMessage();
                    Toast.makeText(UsernameActivity.this,error,Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri photoUri = result.getUri();

                Glide
                        .with(this)
                        .load(photoUri)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(profileImageView);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
}
