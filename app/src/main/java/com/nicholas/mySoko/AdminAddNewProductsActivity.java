package com.nicholas.mySoko;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AdminAddNewProductsActivity extends AppCompatActivity {

    private String categoryName;

    private EditText InputproductName , InputproductDescription , InputproductPrice ;
    private ImageView product_img;
    private Button add_product_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_products);

        //finding views

        InputproductName=(EditText)findViewById(R.id.product_name);
        InputproductDescription=(EditText)findViewById(R.id.product_description);
        InputproductPrice=(EditText)findViewById(R.id.product_price);
        product_img=(ImageView)findViewById(R.id.select_product_image);
        add_product_btn=(Button)findViewById(R.id.add_product);



        categoryName=getIntent().getExtras().get("Category").toString();




    }
}
