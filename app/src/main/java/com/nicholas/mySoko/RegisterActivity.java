package com.nicholas.mySoko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button createAccountbtn;
    private EditText nameInput ,phoneNumberInput , passwordInput,passwordInput2;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //finding views

        createAccountbtn=(Button)findViewById(R.id.register_btn);
        nameInput=(EditText) findViewById(R.id.register_username);
        phoneNumberInput=(EditText) findViewById(R.id.register_phoneNumber);
        passwordInput=(EditText) findViewById(R.id.register_password);
        passwordInput2=(EditText) findViewById(R.id.register_password2);

        loadingBar= new ProgressDialog(this);

        //account creation

        createAccountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createAccount();

            }
        });

    }


//account creation method
    private void createAccount() {
        //capturing input details
        //as strings

        String name=nameInput.getText().toString();
        String phone=phoneNumberInput.getText().toString();
        String password=passwordInput.getText().toString();
        String password2=passwordInput2.getText().toString();

        //check if fields are submitted empty

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please enter your name ...", Toast.LENGTH_SHORT).show();
        }else
        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please enter your Phone Number ...", Toast.LENGTH_SHORT).show();
        }else
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please create your password ...", Toast.LENGTH_SHORT).show();
        }
        else
        if (TextUtils.isEmpty(password2)||!password.equals(password2)){
            Toast.makeText(this, "Password Mismatch or is empty , please check again ...", Toast.LENGTH_SHORT).show();
        }
        else {

            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait as we are checking your credentials ...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            //Validating

            validatePhoneNumber(name,phone , password);

        }



    }
    ///validating phone number method

    private void validatePhoneNumber(final String name, final String phone, final String password) {

        final DatabaseReference reference;
        reference= FirebaseDatabase.getInstance().getReference();


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.child("Users").child(phone).exists()){

                //user does not exist so we save this data in the firebase DB

                    HashMap<String , Object> UserDataMap= new HashMap<>();

                    UserDataMap.put("phone",phone);
                    UserDataMap.put("password",password);
                    UserDataMap.put("name",name);

                    //updating parent

                    reference.child("Users").child(phone).updateChildren(UserDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Congratulations your account has been created successfully ", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        nameInput.setText("");
                                        phoneNumberInput.setText("");
                                        passwordInput.setText("");

                                        //redirect user to login page to login into his account

                                        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                        finish();


                                    }else {

                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Network Error : please try again after some time ...", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                }else {
//user credentials already exists
                    Toast.makeText(RegisterActivity.this, "This number"+ phone +" Already exists", Toast.LENGTH_SHORT).show();

                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try using another phone number ...", Toast.LENGTH_SHORT).show();

                    //redirect user back to the main activity

                    Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
