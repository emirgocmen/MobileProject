package com.example.mobileproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Main2Activity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.options_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.changeprofile)
        {
            firebaseAuth.signOut();
            Intent i=new Intent(Main2Activity.this,MainActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        firebaseAuth =FirebaseAuth.getInstance();
    }

    public void startgame (View view) {
        Intent i=new Intent(Main2Activity.this,Main3Activity.class);
        startActivity(i);

    }

    public void startgame2 (View view) {
        Intent i=new Intent(Main2Activity.this,Main4Activity.class);
        startActivity(i);

    }
    public void cikis(View view){
        System.exit(0);

    }

    public void skorlar(View view) {
        Intent i=new Intent(Main2Activity.this,Main5Activity.class);
        startActivity(i);

    }

}