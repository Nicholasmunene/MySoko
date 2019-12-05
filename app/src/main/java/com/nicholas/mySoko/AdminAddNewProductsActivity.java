package com.nicholas.mySoko;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductsActivity extends AppCompatActivity {

    private String categoryName , pname, pdescription , pPrice , saveCurrentDate , saveCurrentTime;

    private EditText InputproductName , InputproductDescription , InputproductPrice ;
    private ImageView product_img;
    private Button add_product_btn;
    private static  final int GalleryPick = 1 ;
    private ProgressDialog loadingBar;

    private  String productRandomKey , downloadImageUrl;
    private StorageReference productStorageReference;
    private DatabaseReference productsref;

    private Uri ImageUri;

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

        loadingBar=new ProgressDialog(this);



        categoryName=getIntent().getExtras().get("Category").toString();

        productStorageReference= FirebaseStorage.getInstance().getReference().child("product images");
        productsref= FirebaseDatabase.getInstance().getReference().child("Products");


        product_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openGallery();

            }
        });

        add_product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ValidateProductData();

            }
        });


    }


    private void openGallery() {

        Intent galleryIntent= new Intent();

        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GalleryPick &&resultCode==
                RESULT_OK && data!=null && data.getData()!=null){
            ImageUri = data.getData();
            Picasso.with(this).load(ImageUri).into(product_img);
            //You Can Also Use
            //mImageView.setImageURI(mImageUri);
        }
    }

    private  void  ValidateProductData(){

        pdescription=InputproductDescription.getText().toString();
        pname=InputproductName.getText().toString();
        pPrice=InputproductPrice.getText().toString();


        if (ImageUri==null){
            Toast.makeText(this, "Product Image Is Mandatory", Toast.LENGTH_SHORT).show();


        }else  if (TextUtils.isEmpty(pdescription)){

            Toast.makeText(this, "Please Enter the product description", Toast.LENGTH_SHORT).show();


        }else  if (TextUtils.isEmpty(pname)){

            Toast.makeText(this, "Please Enter the product name", Toast.LENGTH_SHORT).show();


        }else  if (TextUtils.isEmpty(pPrice)){

            Toast.makeText(this, "Please Enter the product price", Toast.LENGTH_SHORT).show();
        }
        else {

            storeProductInformation();

        }

    }

    private void storeProductInformation() {

//getting current time and date;

        loadingBar.setTitle("Adding Product");
        loadingBar.setMessage("Dear Admin , Please wait as we are adding the product ...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        Calendar calendar= Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, YYYY");

        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");

        saveCurrentTime=currentTime.format(calendar.getTime());


        productRandomKey=saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = productStorageReference.child(ImageUri.getLastPathSegment()+ productRandomKey+ ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String message= e.toString();

                Toast.makeText(AdminAddNewProductsActivity.this, "Error:"+ message, Toast.LENGTH_SHORT).show();

                loadingBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(AdminAddNewProductsActivity.this, "Product Image uploaded successfully ...", Toast.LENGTH_SHORT).show();


                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {


                        if (!task.isSuccessful()){

                            throw task.getException();

                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();

                        return  filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if (task.isSuccessful()){

                            downloadImageUrl= task.getResult().toString();
                            Toast.makeText(AdminAddNewProductsActivity.this, "Getting the Image Url is successful", Toast.LENGTH_SHORT).show();

                            saveProductInfoToDtatabase();
                        }


                    }
                });
            }
        });

    }

    private void saveProductInfoToDtatabase() {


        HashMap<String , Object> productMap = new HashMap<>();

        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("image", downloadImageUrl);
        productMap.put("description", pdescription);
        productMap.put("Category", categoryName);
        productMap.put("name", pname);
        productMap.put("price", pPrice);


        productsref.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        if (task.isSuccessful()){
                            loadingBar.dismiss();

                            Toast.makeText(AdminAddNewProductsActivity.this, "Product Added Successfully", Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent(AdminAddNewProductsActivity.this,AdminCategoryActivity.class);
                            startActivity(intent);

                        }else
                        {
                            Intent intent=new Intent(AdminAddNewProductsActivity.this,AdminCategoryActivity.class);
                            startActivity(intent);
                            loadingBar.dismiss();

                            String message= task.getException().toString();

                            Toast.makeText(AdminAddNewProductsActivity.this, "Error:"+ message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }



}
