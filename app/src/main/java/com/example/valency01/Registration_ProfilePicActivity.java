package com.example.valency01;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Registration_ProfilePicActivity extends AppCompatActivity {
    TextView skipNextTextView;
    ImageView userProfilePPic,selectProfilePic;
    int galleryPic=1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==galleryPic&&resultCode==RESULT_OK&&data!=null){
            Uri imageUri=data.getData();
            Picasso.get().load(imageUri).noPlaceholder().centerCrop().fit().into(userProfilePPic);
            skipNextTextView.setText("Next>>>");
        }
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
    }
}