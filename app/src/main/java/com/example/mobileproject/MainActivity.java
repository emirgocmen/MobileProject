package com.example.mobileproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    EditText editemail,editpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth=FirebaseAuth.getInstance();
        editemail=findViewById(R.id.editemail);
        editpassword=findViewById(R.id.editpassword);

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            Intent i=new Intent(MainActivity.this,Main2Activity.class);
            startActivity(i);
            finish();
        }
    }

    public void signup (View view) {

        String email=editemail.getText().toString();
        String password=editpassword.getText().toString();

        if(email.isEmpty())
        {
            Toast.makeText(MainActivity.this,R.string.hata5,Toast.LENGTH_SHORT).show();
        }
        else if(password.isEmpty()){
            Toast.makeText(MainActivity.this,R.string.hata6,Toast.LENGTH_SHORT).show();
        }
        else if(password.length() < 6)
        {
            Toast.makeText(MainActivity.this,R.string.hata4,Toast.LENGTH_SHORT).show();
        }
        else {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(MainActivity.this, R.string.basarili, Toast.LENGTH_LONG).show();

                    Intent i=new Intent(MainActivity.this,Main2Activity.class);
                    startActivity(i);
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this,R.string.hata7, Toast.LENGTH_LONG).show();
                }
            });

        }



    }

    public void signin(View view) {

        String email = editemail.getText().toString();
        String password = editpassword.getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(MainActivity.this, R.string.hata5, Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(MainActivity.this, R.string.hata6, Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent i = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(i);
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
               if(e.getLocalizedMessage().toString().equals("The email address is badly formatted."))
                    {
                        Toast.makeText(MainActivity.this,R.string.hata7,Toast.LENGTH_SHORT).show();
                    }
               else
               {
                   Toast.makeText(MainActivity.this,R.string.hata8,Toast.LENGTH_SHORT).show();
               }


                }
            });


        }

    }

}
