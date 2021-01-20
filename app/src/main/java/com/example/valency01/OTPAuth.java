package com.example.valency01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class OTPAuth extends AppCompatActivity {
EditText OtpBox1,OtpBox2,OtpBox3,OtpBox4,OtpBox5,OtpBox6;
Button verifyOTPButton;
EditText [] OTPstringArray;
String phoneVerificationId;
String userPhoneNumber;
PhoneAuthProvider phoneAuthProvider;
PhoneAuthCredential phoneAuthCredential;
FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_auth);
        IntializeViews();
        userPhoneNumber= getIntent().getStringExtra("phoneNumber");
        generateOTP(userPhoneNumber);
        verifyOTPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String OTPentered=OtpBox1.getText().toString()+OtpBox2.getText().toString()+OtpBox3.getText().toString()+OtpBox4.getText().toString()+OtpBox5.getText().toString()+OtpBox6.getText().toString();
                Toast.makeText(OTPAuth.this, "work bsdk"+OTPentered, Toast.LENGTH_SHORT).show();
                verifyOTP(OTPentered);

            }
        });
    }

    private void generateOTP(String userPhoneNumber) {
        phoneAuthProvider.getInstance().verifyPhoneNumber(userPhoneNumber,60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mCallBack);

    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String OTP=phoneAuthCredential.getSmsCode();
            if(OTP!=null){
                try {

                    OtpBox1.setText(Character.toString(OTP.charAt(0)));
                    OtpBox2.setText(Character.toString(OTP.charAt(1)));
                    OtpBox3.setText(Character.toString(OTP.charAt(2)));
                    OtpBox4.setText(Character.toString(OTP.charAt(3)));
                    OtpBox5.setText(Character.toString(OTP.charAt(4)));
                    OtpBox6.setText(Character.toString(OTP.charAt(5)));


                    Toast.makeText(OTPAuth.this, "recieved bsdk" + OTP, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(OTPAuth.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                verifyOTP(OTP);

            }
            else
                Toast.makeText(OTPAuth.this, "Auto Retreival failed", Toast.LENGTH_SHORT).show();


        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            phoneVerificationId=s;
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(OTPAuth.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };

    private void verifyOTP(String otp) {

        phoneAuthCredential=PhoneAuthProvider.getCredential(phoneVerificationId,otp);
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser firebaseUser=task.getResult().getUser();
                    String firebseUserID=firebaseUser.getUid();
                    DatabaseReference firebaseDatabaseReference=FirebaseDatabase.getInstance().getReference();
                    firebaseDatabaseReference.child("users").child(firebseUserID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                //go to home page
                                Toast.makeText(OTPAuth.this, "NOW YOU WILL BE DIRECTED TO HOME!!!", Toast.LENGTH_SHORT).show();

                            }
                            else{
                                Toast.makeText(OTPAuth.this, "Please Enter the Details", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(OTPAuth.this,AskingDetails.class).putExtra("userPhoneNumber",userPhoneNumber));
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });





                }
                else
                    Toast.makeText(OTPAuth.this, "!!!!!!!!!!!!!!!!!!BSDK!!!!!!!!!!!!!!!!!"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void IntializeViews() {
        OtpBox1=(EditText)findViewById(R.id.otpEditText1);
        OtpBox2=(EditText)findViewById(R.id.otpEditText2);
        OtpBox3=(EditText)findViewById(R.id.otpEditText3);
        OtpBox4=(EditText)findViewById(R.id.otpEditText4);
        OtpBox5=(EditText)findViewById(R.id.otpEditText5);
        OtpBox6=(EditText)findViewById(R.id.otpEditText6);
        verifyOTPButton=(Button)findViewById(R.id.verifyOTPButton);
        OTPstringArray=new EditText[]{OtpBox1,OtpBox2,OtpBox3,OtpBox4,OtpBox5,OtpBox6};
        firebaseAuth=FirebaseAuth.getInstance();
    }
}