package com.nicholas.mySoko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nicholas.mySoko.Adaptors.Login_Adaptor;
import com.nicholas.mySoko.Models.Users;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText phoneNumberInput , passwordInput;
    private Button loginButton;
    private TextView adminLink,NotadminLink;

    private ProgressDialog loadingBar;

    private  String parentDbName = "Users";

    private CheckBox checkBoxRememberme ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //finding views

        phoneNumberInput=(EditText) findViewById(R.id.login_phoneNumber);
        passwordInput=(EditText) findViewById(R.id.login_password);
        loginButton=(Button)findViewById(R.id.login_btn);
        adminLink=(TextView)findViewById(R.id.admin_panel_link);
        NotadminLink=(TextView)findViewById(R.id.not_admin);

        loadingBar= new ProgressDialog(this);


        checkBoxRememberme=(CheckBox)findViewById(R.id.remember_me_checkbox);
        Paper.init(this);

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login Admin");
                adminLink.setVisibility(View.INVISIBLE);
                NotadminLink.setVisibility(View.VISIBLE);
                parentDbName="admins";
            }
        });


        NotadminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login ");
                adminLink.setVisibility(View.VISIBLE);
                NotadminLink.setVisibility(View.INVISIBLE);
                parentDbName="Users";

            }
        });

        //logging user in

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginUser();

            }
        });



    }
//logging in  user method


    private void loginUser() {


        String phone=phoneNumberInput.getText().toString();
        String password=passwordInput.getText().toString();

        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please enter your Phone Number ...", Toast.LENGTH_SHORT).show();
        }else
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter your password ...", Toast.LENGTH_SHORT).show();
        }else {

            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait as we are checking your credentials ...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            //allowing access to account
            AllowAccessToAccount(phone, password);

        }


    }

    //allowing access to account method

    private void AllowAccessToAccount(final String phone, final String password) {

        //initialising the check box

        if (checkBoxRememberme.isChecked()){

            Paper.book().write(Login_Adaptor.userphonekey,phone);
            Paper.book().write(Login_Adaptor.userpasswordkey,password);
        }
//connecting to db
        final DatabaseReference reference;
        reference= FirebaseDatabase.getInstance().getReference();



        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(parentDbName).child(phone).exists()){

                    Users Usersdata= dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if (Usersdata.getPhone().equals(phone)){

                        if (Usersdata.getPassword().equals(password)){

                            if (parentDbName.equals("admins")){
                                Toast.makeText(LoginActivity.this, "Welcome Admin ...", Toast.LENGTH_SHORT).show();

                                loadingBar.dismiss();

                                Intent intent=new Intent(LoginActivity.this,AdminCategoryActivity.class);
                                startActivity(intent);
                                finish();


                            }else if (parentDbName.equals("Users")){


                                Toast.makeText(LoginActivity.this, "Logged In Successfully ...", Toast.LENGTH_SHORT).show();

                                loadingBar.dismiss();

                               Intent intent=new Intent(LoginActivity.this,Details.class);
                               Login_Adaptor.CurrentonlineUsers = Usersdata;
                               startActivity(intent);
                               finish();

                            }




                        }else {

                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password incorrect", Toast.LENGTH_SHORT).show();
                        }

                    }

                }else {

                    Toast.makeText(LoginActivity.this, "Account with Number "+phone+" does not exist ", Toast.LENGTH_SHORT).show();

                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
