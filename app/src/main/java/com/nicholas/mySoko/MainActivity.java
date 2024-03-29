package com.nicholas.mySoko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nicholas.mySoko.Adaptors.Login_Adaptor;
import com.nicholas.mySoko.Models.Users;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button joinNowButton , loginButton;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);
        loadingBar= new ProgressDialog(this);

        //finding views

        joinNowButton=(Button)findViewById(R.id.main_Join_now_btn);
        loginButton=(Button)findViewById(R.id.main_login_btn);

        //setting an Onclick listener to move to login activity

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        //setting Onclick listener to move to account creation activity

        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        String UserPhoneKey =Paper.book().read(Login_Adaptor.userphonekey);
        String UserPasswordKey =Paper.book().read(Login_Adaptor.userpasswordkey);

        if (UserPhoneKey !="" && UserPasswordKey!=""){

            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)){

                AllowAccess(UserPhoneKey,UserPasswordKey);

                loadingBar.setTitle("Login Account");
                loadingBar.setMessage("Please wait as we are checking your credentials ...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

            }
        }
    }

    private void AllowAccess(final  String phone, final String password) {

        final DatabaseReference reference;
        reference= FirebaseDatabase.getInstance().getReference();



        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("Users").child(phone).exists()){

                    Users Usersdata= dataSnapshot.child("Users").child(phone).getValue(Users.class);

                    if (Usersdata.getPhone().equals(phone)){

                        if (Usersdata.getPassword().equals(password)){

                            Toast.makeText(MainActivity.this, "Please wait ,You are already logged in ...", Toast.LENGTH_SHORT).show();

                            loadingBar.dismiss();

                            Intent intent=new Intent(MainActivity.this,Details.class);
                            startActivity(intent);
                            finish();


                        }else {

                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Password is Incorrect", Toast.LENGTH_SHORT).show();
                        }

                    }

                }else {

                    Toast.makeText(MainActivity.this, "Account with Number "+phone+" does not exist ", Toast.LENGTH_SHORT).show();

                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
