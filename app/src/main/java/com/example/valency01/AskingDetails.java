package com.example.valency01;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AskingDetails extends AppCompatActivity {
    String userPhoneNumber,userName,userGender,userDateOfBirth,userAge,userID,currentDate;
    int userAgeInt;
    LocalDate dateOfBirth,todayDate;
    EditText nameEditText;
    Button submitButton,dateOfBirthButton;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    TextView dateOfBirthChangeTextView;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asking_details);
        InitializingFields();
        dateOfBirthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar=Calendar.getInstance();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int date=calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(AskingDetails.this,R.style.Theme_AppCompat_Dialog_MinWidth,mDateSetListener,year,month,date);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(0,74,137)));
                datePickerDialog.show();

            }
        });
        mDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                month=month+1;
                int todayYear=todayDate.getYear();
                userAgeInt=todayYear-year;
                userAge=Integer.toString(userAgeInt);
                userDateOfBirth=Integer.toString(date)+"/"+Integer.toString(month)+"/"+Integer.toString(year);
                dateOfBirthChangeTextView.setText(userDateOfBirth);


            }
        };
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName=nameEditText.getText().toString();
                if(userAgeInt>12&&!userGender.isEmpty()&&!userName.isEmpty()){
                    createAccount();
                    Toast.makeText(AskingDetails.this, "Select your profile picture", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AskingDetails.this,Registration_ProfilePicActivity.class));
                }
                else
                    Toast.makeText(AskingDetails.this, "failed Submission", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void createAccount() {
        databaseReference.child("users").child(userID).setValue("");
        HashMap<String,String> userDetailshashMap=new HashMap<>();
        userDetailshashMap.put("userID",userID);
        userDetailshashMap.put("userName",userName);
        userDetailshashMap.put("userAge",userAge);
        userDetailshashMap.put("userDateOfBirth",userDateOfBirth);
        userDetailshashMap.put("userPhoneNumber",userPhoneNumber);
        userDetailshashMap.put("userGender",userGender);
        userDetailshashMap.put("accountCreationDate",currentDate);
        userDetailshashMap.put("userProfilePic","");
        databaseReference.child("users").child(userID).setValue(userDetailshashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
             if(task.isSuccessful()){
                 //we will intent something here
                 Toast.makeText(AskingDetails.this, "user profile Created", Toast.LENGTH_SHORT).show();
             }
             else
                 Toast.makeText(AskingDetails.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void InitializingFields() {
        userPhoneNumber=getIntent().getStringExtra("userPhoneNumber");
        nameEditText=(EditText)findViewById(R.id.userNameEditText);
        submitButton=(Button)findViewById(R.id.detailsSubmitButton);
        dateOfBirthButton=(Button)findViewById(R.id.dateOfBirthButton);
        dateOfBirthChangeTextView=(TextView)findViewById(R.id.dateOfBirthChangeTextView);
        todayDate=LocalDate.now();
        mAuth=FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference();
        userID=mAuth.getCurrentUser().getUid();
        currentDate=todayDate.toString();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked=((RadioButton)view).isChecked();
        switch (view.getId()){
            case R.id.genderMaleRadioButton:
                if(checked)
                userGender="Male";
                break;
            case R.id.genderFemaleRadioButton:
                if(checked)
                userGender="Female";
                break;
            default:
                break;

        }
    }
}