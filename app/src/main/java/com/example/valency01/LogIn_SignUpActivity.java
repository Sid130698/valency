package com.example.valency01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

public class LogIn_SignUpActivity extends AppCompatActivity {
EditText userNumber;
Button nextButton;
Spinner countryCodeSpinner;
ArrayAdapter<String> CountryCodesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in__sign_up);
        InitializeViews();
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code=CountryDetails.countryAreaCodes[countryCodeSpinner.getSelectedItemPosition()];
                String enteredNumber=userNumber.getText().toString();
                if(enteredNumber.isEmpty())
                    Toast.makeText(LogIn_SignUpActivity.this, "Enter a Valid Number ", Toast.LENGTH_SHORT).show();
                else{
                    Intent OTPverification=new Intent(LogIn_SignUpActivity.this,OTPAuth.class).putExtra("phoneNumber","+"+code+enteredNumber);
                    startActivity(OTPverification);
                    finish();
                }
            }
        });

    }

    private void InitializeViews() {
        userNumber=(EditText)findViewById(R.id.numberEditText);
         nextButton=(Button)findViewById(R.id.nextButtonAfterNumber);
         countryCodeSpinner=(Spinner)findViewById(R.id.spinnerNumberEditText);
         CountryCodesAdapter=new ArrayAdapter<String>(this,R.layout.spinner_items,CountryDetails.countryNames);
         countryCodeSpinner.setAdapter(CountryCodesAdapter);
    }
}