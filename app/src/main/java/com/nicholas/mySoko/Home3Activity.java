package com.nicholas.mySoko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.rey.material.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Home3Activity extends AppCompatActivity {
    private  ImageView imageView;
    private TextView textView;

    private DrawerLayout drawerLayout;
    private androidx.appcompat.app.ActionBarDrawerToggle toggler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home3);

        Paper.init(this);

        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);

        textView=(TextView) findViewById(R.id.user_name);
        imageView=(ImageView)findViewById(R.id.profile_image);

        toggler= new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open ,R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggler);
        toggler.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (toggler.onOptionsItemSelected(item)){
            int id = item.getItemId();

            if (id == R.id.cart)
            {

            }
            else if (id == R.id.orders)
            {

            }
            else if (id == R.id.categories)
            {

            }
            else if (id == R.id.settings)
            {
                //Intent intent = new Intent(Home3Activity.this, SettinsActivity.class);
                //startActivity(intent);
            }
            else if (id == R.id.nav_logout)
            {
                Paper.book().destroy();

                Intent intent = new Intent(Home3Activity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
