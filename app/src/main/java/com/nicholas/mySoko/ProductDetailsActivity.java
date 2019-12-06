package com.nicholas.mySoko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nicholas.mySoko.Adaptors.Login_Adaptor;
import com.nicholas.mySoko.Models.Products;
import com.rey.material.widget.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

private Button addtocart;
    private ImageView productImage;
    private TextView pname,pprice,pdescription;
    private String productId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productId =getIntent().getStringExtra("pid");
        addtocart=findViewById(R.id.product_add_to_cart_btn);
        productImage=findViewById(R.id.product_image_details);
        pname=findViewById(R.id.product_name_details);
        pprice=findViewById(R.id.product_price_details);
        pdescription=findViewById(R.id.product_description_details);

        getproductdetails(productId);


        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtocart();
            }
        });


    }

    private void addtocart() {

        String savecurrenttime , savecurrentdate;

        Calendar calfordate = Calendar.getInstance();

        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("MM dd ,YYYY");
        savecurrentdate=simpleDateFormat.format(calfordate.getTime());


        SimpleDateFormat currenttime= new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime=simpleDateFormat.format(calfordate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String , Object>cartMap = new HashMap<>();

        cartMap.put("pid",productId);
        cartMap.put("pname",pname.getText().toString());
        cartMap.put("price",pprice.getText().toString());
        cartMap.put("date",savecurrentdate);
        cartMap.put("time",savecurrenttime);
        cartMap.put("quantity","");
        cartMap.put("discount","");

        cartListRef.child("User View").child(Login_Adaptor.CurrentonlineUsers.getPhone()).child("Products")
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            cartListRef.child("Admin View").child(Login_Adaptor.CurrentonlineUsers.getPhone()).child("Products")
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()){
                                                Toast.makeText(ProductDetailsActivity.this, "Added to Cart list", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ProductDetailsActivity.this,Details.class);
                                                startActivity(intent);
                                            }

                                        }
                                    });
                        }

                    }
                });


    }

    private void getproductdetails(String productId) {

        DatabaseReference productref = FirebaseDatabase.getInstance().getReference().child("Products");

        productref.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    Products products = dataSnapshot.getValue(Products.class);

                    pname.setText(products.getName());
                    pdescription.setText(products.getDescription());
                    pprice.setText("Price = " + products.getPrice()+ "Ksh");
                    Picasso.with(getApplicationContext()).load(products.getImage()).into(productImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
