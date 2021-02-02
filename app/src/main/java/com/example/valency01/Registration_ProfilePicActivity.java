package com.example.valency01;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Registration_ProfilePicActivity extends AppCompatActivity {
    TextView skipNextTextView;
    ImageView userProfilePPic,selectProfilePic;
    StorageReference storageReference;
    int galleryPic=1;
    FirebaseAuth mAuth;
    Uri imageUri;
    DatabaseReference rootref;
    String currentDate,currentTime,profilePicName;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==galleryPic&&resultCode==RESULT_OK&&data!=null){
            imageUri=data.getData();
            Picasso.get().load(imageUri).noPlaceholder().centerCrop().fit().into(userProfilePPic);

            skipNextTextView.setText("Next>>>");
        }
    }

    private void uploadingProfilepic(Uri imageUri) {
        //profilePicName=imageUri.toString();
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MMMM-yyyy");
        currentDate=simpleDateFormat.format(calendar.getTime());
        Calendar calendar1=Calendar.getInstance();
        SimpleDateFormat simpleTimeFormat=new SimpleDateFormat("HH:mm");
        currentTime=simpleTimeFormat.format(calendar1.getTime());
        StorageReference filePath=storageReference.child("ProfilePictures").child(mAuth.getCurrentUser().getUid()+".jpg");
        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> firebaseUri=taskSnapshot.getStorage().getDownloadUrl();
                firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloaduri=uri.toString();
                        rootref.child("userProfilePic").setValue(downloaduri).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Registration_ProfilePicActivity.this, "Done!!!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(Registration_ProfilePicActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });

            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration__profile_pic);
        initializefields();
        selectProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectProfilePIcture();
            }
        });
        skipNextTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadingProfilepic(imageUri);
            }
        });
    }

    private void selectProfilePIcture() {
        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,galleryPic);
    }

    private void initializefields() {
        skipNextTextView=(TextView)findViewById(R.id.nextSkipTextView);
        userProfilePPic=(ImageView)findViewById(R.id.userProfilePicImageView);
        selectProfilePic=(ImageView)findViewById(R.id.selectProfilePicImageView);
        storageReference= FirebaseStorage.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        rootref= FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());

    }
}