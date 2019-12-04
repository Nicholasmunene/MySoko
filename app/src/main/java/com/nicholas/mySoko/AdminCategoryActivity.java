package com.nicholas.mySoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView tshirts , dresses , coats , sportstshirts;

    private ImageView watches , bag , shoes , hats ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);


        //find views

        tshirts = findViewById(R.id.tshirts) ;
        dresses =findViewById(R.id.dresses) ;
       coats = findViewById(R.id.coats) ;
       sportstshirts = findViewById(R.id.sports) ;


        watches = findViewById(R.id.watch) ;
       bag =findViewById(R.id.bag) ;
        shoes= findViewById(R.id.shoe) ;
        hats = findViewById(R.id.hat) ;




        tshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProductsActivity.class);
                intent.putExtra("Category", "tshirts");
                startActivity(intent);
            }
        });


        dresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProductsActivity.class);
                intent.putExtra("Category", "dresses");
                startActivity(intent);
            }
        });


        coats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProductsActivity.class);
                intent.putExtra("Category", "coats");
                startActivity(intent);
            }
        });

        sportstshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProductsActivity.class);
                intent.putExtra("Category", "SportTshirts");
                startActivity(intent);
            }
        });


        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProductsActivity.class);
                intent.putExtra("Category", "watches");
                startActivity(intent);
            }
        });


        bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProductsActivity.class);
                intent.putExtra("Category", "bags");
                startActivity(intent);
            }
        });

        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProductsActivity.class);
                intent.putExtra("Category", "shoes");
                startActivity(intent);
            }
        });

        hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminAddNewProductsActivity.class);
                intent.putExtra("Category", "hats");
                startActivity(intent);
            }
        });


    }
}
