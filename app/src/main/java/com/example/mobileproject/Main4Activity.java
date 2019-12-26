package com.example.mobileproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.EventLog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;

public class Main4Activity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;

    MediaPlayer plycorrect,plywrong,plycong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        firestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        sorucek();

    }

    public int ssayi=0;
    public int skor=0;


    final Random rastgele=new Random();
    int[] cekilensorular=new int[10];

    public int rastgelesoru;
    public int i;

    CountDownTimer countDownTimer;

    public void sorucek()
    {
        plycorrect=MediaPlayer.create(Main4Activity.this,R.raw.correct);
        plywrong=MediaPlayer.create(Main4Activity.this,R.raw.wrong);
        plycong=MediaPlayer.create(Main4Activity.this,R.raw.cong);

        final TextView soru=(TextView)findViewById(R.id.question);
        final Button secenek1=(Button)findViewById(R.id.choice1);
        final Button secenek2=(Button)findViewById(R.id.choice2);
        final Button secenek3=(Button)findViewById(R.id.choice3);
        final Button secenek4=(Button)findViewById(R.id.choice4);
        final TextView skortext=(TextView)findViewById(R.id.score);
        final TextView timetext=(TextView)findViewById(R.id.texttime);

        sorukontrol();

        skortext.setText(String.valueOf(skor));

        CollectionReference collectionReference = firestore.collection("WordGame");

        collectionReference.whereEqualTo("qnumber",rastgelesoru).addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if(e != null)
                {
                    Toast.makeText(Main4Activity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_SHORT).show();
                }

                if(queryDocumentSnapshots != null)
                {
                    for(DocumentSnapshot snapshot:queryDocumentSnapshots.getDocuments())
                    {
                        Map<String,Object> data=snapshot.getData();

                        String question=(String) data.get("question");
                        final String choise1=(String) data.get("choice1");
                        String choise2=(String) data.get("choice2");
                        String choise3=(String) data.get("choice3");
                        String choise4=(String) data.get("choice4");
                        final String answer=(String) data.get("answer");

                        soru.setText(question);
                        secenek1.setText(choise1);
                        secenek2.setText(choise2);
                        secenek3.setText(choise3);
                        secenek4.setText(choise4);

                        if(ssayi == 0)
                        {
                            countDownTimer=new CountDownTimer(60000,1000){

                                @Override
                                public void onTick(long millisUntilFinished)
                                {
                                    timetext.setText(""+millisUntilFinished/1000);
                                }
                                @Override
                                public void onFinish()
                                {
                                    Toast.makeText(Main4Activity.this,R.string.hata2,Toast.LENGTH_SHORT).show();
                                    Intent i=new Intent(Main4Activity.this,Main2Activity.class);
                                    startActivity(i);
                                }

                            }.start();
                        }

                        secenek1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(answer.equals(secenek1.getText().toString()))
                                {
                                    plycorrect.start();
                                    secenek1.setBackgroundColor(Color.parseColor("#008140"));

                                    new CountDownTimer(1000,1000){

                                        @Override
                                        public void onTick(long millisUntilFinished)
                                        {
                                        }
                                        @Override
                                        public void onFinish()
                                        {
                                            plycorrect.stop();
                                            secenek1.setBackgroundColor(Color.parseColor("#0091EA"));
                                            skor=skor+10;
                                            skortext.setText(String.valueOf(skor));

                                            ssayi++;

                                            if(ssayi== 10)
                                            {
                                                secenek1.setEnabled(false);
                                                secenek2.setEnabled(false);
                                                secenek3.setEnabled(false);
                                                secenek4.setEnabled(false);

                                                plycong.start();
                                                Toast.makeText(Main4Activity.this,R.string.tebrikler,Toast.LENGTH_LONG).show();

                                                new CountDownTimer(4000,1000){
                                                    @Override
                                                    public void onTick(long millisUntilFinished)
                                                    {
                                                    }
                                                    @Override
                                                    public void onFinish()
                                                    {
                                                        plycong.stop();
                                                        countDownTimer.cancel();


                                                        Intent i=new Intent(Main4Activity.this,Main2Activity.class);
                                                        startActivity(i);
                                                        finish();
                                                    }

                                                }.start();

                                                //Toast.makeText(Main4Activity.this,R.string.tebrikler,Toast.LENGTH_LONG).show();

                                                FirebaseUser  firebaseUser = firebaseAuth.getCurrentUser();
                                                String kullanicimail=firebaseUser.getEmail();
                                                String kisim="tring";

                                                HashMap<String,Object> gonderilen = new HashMap<>();
                                                gonderilen.put("E-mail",kullanicimail);
                                                gonderilen.put("Score",skor);
                                                gonderilen.put("Bölüm",kisim);


                                                firestore.collection("Scores").add(gonderilen).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(Main4Activity.this,R.string.hata3,Toast.LENGTH_SHORT).show();
                                                    }
                                                });


                                                countDownTimer.cancel();



                                            }
                                            else
                                            {
                                                sorucek();
                                            }
                                        }

                                    }.start();

                                }
                                else
                                {
                                    plywrong.start();

                                    secenek1.setBackgroundColor(Color.parseColor("#EC4849"));

                                    new CountDownTimer(1000,1000){
                                        @Override
                                        public void onTick(long millisUntilFinished)
                                        {
                                        }
                                        @Override
                                        public void onFinish()
                                        {
                                            countDownTimer.cancel();
                                            plywrong.stop();

                                            FirebaseUser  firebaseUser = firebaseAuth.getCurrentUser();
                                            String kullanicimail=firebaseUser.getEmail();
                                            String kisim="tring";

                                            HashMap<String,Object> gonderilen = new HashMap<>();
                                            gonderilen.put("E-mail",kullanicimail);
                                            gonderilen.put("Score",skor);
                                            gonderilen.put("Bölüm",kisim);

                                            firestore.collection("Scores").add(gonderilen).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(Main4Activity.this,R.string.hata3,Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            Toast.makeText(Main4Activity.this,R.string.hata1,Toast.LENGTH_SHORT).show();
                                            Intent i=new Intent(Main4Activity.this,Main2Activity.class);
                                            startActivity(i);

                                            finish();

                                        }

                                    }.start();


                                }
                            }
                        });


                        secenek2.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                if(answer.equals(secenek2.getText().toString()))
                                {
                                    plycorrect.start();
                                    secenek2.setBackgroundColor(Color.parseColor("#008140"));

                                    new CountDownTimer(1000,1000){

                                        @Override
                                        public void onTick(long millisUntilFinished)
                                        {
                                        }
                                        @Override
                                        public void onFinish()
                                        {
                                            plycorrect.stop();
                                            secenek2.setBackgroundColor(Color.parseColor("#0091EA"));
                                            skor=skor+10;
                                            skortext.setText(String.valueOf(skor));

                                            ssayi++;

                                            if(ssayi== 10)
                                            {

                                                secenek1.setEnabled(false);
                                                secenek2.setEnabled(false);
                                                secenek3.setEnabled(false);
                                                secenek4.setEnabled(false);

                                                plycong.start();
                                                Toast.makeText(Main4Activity.this,R.string.tebrikler,Toast.LENGTH_LONG).show();

                                                new CountDownTimer(4000,1000){
                                                    @Override
                                                    public void onTick(long millisUntilFinished)
                                                    {
                                                    }
                                                    @Override
                                                    public void onFinish()
                                                    {


                                                        plycong.stop();
                                                        countDownTimer.cancel();


                                                        Intent i=new Intent(Main4Activity.this,Main2Activity.class);
                                                        startActivity(i);
                                                        finish();
                                                    }

                                                }.start();


                                                FirebaseUser  firebaseUser = firebaseAuth.getCurrentUser();
                                                String kullanicimail=firebaseUser.getEmail();
                                                String kisim="tring";

                                                HashMap<String,Object> gonderilen = new HashMap<>();
                                                gonderilen.put("E-mail",kullanicimail);
                                                gonderilen.put("Score",skor);
                                                gonderilen.put("Bölüm",kisim);


                                                firestore.collection("Scores").add(gonderilen).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(Main4Activity.this,R.string.hata3,Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                                countDownTimer.cancel();

                                            }
                                            else
                                            {
                                                sorucek();
                                            }


                                        }

                                    }.start();

                                }
                                else
                                {
                                    plywrong.start();
                                    secenek2.setBackgroundColor(Color.parseColor("#EC4849"));

                                    new CountDownTimer(1000,1000){
                                        @Override
                                        public void onTick(long millisUntilFinished)
                                        {
                                        }
                                        @Override
                                        public void onFinish()
                                        {
                                            plywrong.stop();
                                            countDownTimer.cancel();

                                            Toast.makeText(Main4Activity.this,R.string.hata1,Toast.LENGTH_SHORT).show();
                                            Intent i=new Intent(Main4Activity.this,Main2Activity.class);
                                            startActivity(i);
                                            finish();
                                        }

                                    }.start();

                                    FirebaseUser  firebaseUser = firebaseAuth.getCurrentUser();
                                    String kullanicimail=firebaseUser.getEmail();
                                    String kisim="tring";

                                    HashMap<String,Object> gonderilen = new HashMap<>();
                                    gonderilen.put("E-mail",kullanicimail);
                                    gonderilen.put("Score",skor);
                                    gonderilen.put("Bölüm",kisim);


                                    firestore.collection("Scores").add(gonderilen).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Main4Activity.this,R.string.hata3,Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    countDownTimer.cancel();
                                }
                            }
                        });

                        secenek3.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                if(answer.equals(secenek3.getText().toString()))
                                {
                                    plycorrect.start();
                                    secenek3.setBackgroundColor(Color.parseColor("#008140"));

                                    new CountDownTimer(1000,1000){

                                        @Override
                                        public void onTick(long millisUntilFinished)
                                        {
                                        }
                                        @Override
                                        public void onFinish()
                                        {

                                            plycorrect.stop();
                                            secenek3.setBackgroundColor(Color.parseColor("#0091EA"));
                                            skor=skor+10;
                                            skortext.setText(String.valueOf(skor));

                                            ssayi++;

                                            if(ssayi== 10)
                                            {

                                                secenek1.setEnabled(false);
                                                secenek2.setEnabled(false);
                                                secenek3.setEnabled(false);
                                                secenek4.setEnabled(false);

                                                plycong.start();
                                                Toast.makeText(Main4Activity.this,R.string.tebrikler,Toast.LENGTH_LONG).show();

                                                new CountDownTimer(4000,1000){
                                                    @Override
                                                    public void onTick(long millisUntilFinished)
                                                    {
                                                    }
                                                    @Override
                                                    public void onFinish()
                                                    {
                                                        plycong.stop();
                                                        countDownTimer.cancel();


                                                        Intent i=new Intent(Main4Activity.this,Main2Activity.class);
                                                        startActivity(i);
                                                        finish();
                                                    }

                                                }.start();

                                                //Toast.makeText(Main4Activity.this,R.string.tebrikler,Toast.LENGTH_LONG).show();

                                                FirebaseUser  firebaseUser = firebaseAuth.getCurrentUser();
                                                String kullanicimail=firebaseUser.getEmail();
                                                String kisim="tring";

                                                HashMap<String,Object> gonderilen = new HashMap<>();
                                                gonderilen.put("E-mail",kullanicimail);
                                                gonderilen.put("Score",skor);
                                                gonderilen.put("Bölüm",kisim);


                                                firestore.collection("Scores").add(gonderilen).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(Main4Activity.this,R.string.hata3,Toast.LENGTH_SHORT).show();
                                                    }
                                                });


                                                countDownTimer.cancel();



                                            }
                                            else
                                            {
                                                sorucek();
                                            }
                                        }

                                    }.start();

                                }
                                else
                                {
                                    plywrong.start();

                                    secenek3.setBackgroundColor(Color.parseColor("#EC4849"));

                                    new CountDownTimer(1000,1000){
                                        @Override
                                        public void onTick(long millisUntilFinished)
                                        {
                                        }
                                        @Override
                                        public void onFinish()
                                        {
                                            plywrong.stop();
                                            countDownTimer.cancel();

                                            Toast.makeText(Main4Activity.this,R.string.hata1,Toast.LENGTH_SHORT).show();
                                            Intent i=new Intent(Main4Activity.this,Main2Activity.class);
                                            startActivity(i);
                                            finish();
                                        }

                                    }.start();

                                    FirebaseUser  firebaseUser = firebaseAuth.getCurrentUser();
                                    String kullanicimail=firebaseUser.getEmail();
                                    String kisim="tring";

                                    HashMap<String,Object> gonderilen = new HashMap<>();
                                    gonderilen.put("E-mail",kullanicimail);
                                    gonderilen.put("Score",skor);
                                    gonderilen.put("Bölüm",kisim);


                                    firestore.collection("Scores").add(gonderilen).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Main4Activity.this,R.string.hata3,Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    countDownTimer.cancel();

                                }
                            }
                        });

                        secenek4.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                if(answer.equals(secenek4.getText().toString()))
                                {
                                    plycorrect.start();
                                    secenek4.setBackgroundColor(Color.parseColor("#008140"));

                                    new CountDownTimer(1000,1000){

                                        @Override
                                        public void onTick(long millisUntilFinished)
                                        {
                                        }
                                        @Override
                                        public void onFinish()
                                        {
                                            plycorrect.stop();

                                            secenek4.setBackgroundColor(Color.parseColor("#0091EA"));
                                            skor=skor+10;
                                            skortext.setText(String.valueOf(skor));

                                            ssayi++;

                                            if(ssayi== 10)
                                            {
                                                secenek1.setEnabled(false);
                                                secenek2.setEnabled(false);
                                                secenek3.setEnabled(false);
                                                secenek4.setEnabled(false);

                                                plycong.start();
                                                Toast.makeText(Main4Activity.this,R.string.tebrikler,Toast.LENGTH_LONG).show();

                                                new CountDownTimer(4000,1000){
                                                    @Override
                                                    public void onTick(long millisUntilFinished)
                                                    {
                                                    }
                                                    @Override
                                                    public void onFinish()
                                                    {
                                                        plycong.stop();
                                                        countDownTimer.cancel();


                                                        Intent i=new Intent(Main4Activity.this,Main2Activity.class);
                                                        startActivity(i);
                                                        finish();
                                                    }

                                                }.start();

                                                //Toast.makeText(Main4Activity.this,R.string.tebrikler,Toast.LENGTH_LONG).show();

                                                FirebaseUser  firebaseUser = firebaseAuth.getCurrentUser();
                                                String kullanicimail=firebaseUser.getEmail();
                                                String kisim="tring";

                                                HashMap<String,Object> gonderilen = new HashMap<>();
                                                gonderilen.put("E-mail",kullanicimail);
                                                gonderilen.put("Score",skor);
                                                gonderilen.put("Bölüm",kisim);


                                                firestore.collection("Scores").add(gonderilen).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(Main4Activity.this,R.string.hata3,Toast.LENGTH_SHORT).show();
                                                    }
                                                });


                                                countDownTimer.cancel();



                                            }
                                            else
                                            {
                                                sorucek();
                                            }
                                        }

                                    }.start();

                                }
                                else
                                {
                                    plywrong.start();
                                    secenek4.setBackgroundColor(Color.parseColor("#EC4849"));

                                    new CountDownTimer(1000,1000){
                                        @Override
                                        public void onTick(long millisUntilFinished)
                                        {
                                        }
                                        @Override
                                        public void onFinish()
                                        {

                                            plywrong.stop();
                                            countDownTimer.cancel();

                                            Toast.makeText(Main4Activity.this,R.string.hata1,Toast.LENGTH_SHORT).show();
                                            Intent i=new Intent(Main4Activity.this,Main2Activity.class);
                                            startActivity(i);
                                            finish();
                                        }

                                    }.start();

                                    FirebaseUser  firebaseUser = firebaseAuth.getCurrentUser();
                                    String kullanicimail=firebaseUser.getEmail();
                                    String kisim="tring";

                                    HashMap<String,Object> gonderilen = new HashMap<>();
                                    gonderilen.put("E-mail",kullanicimail);
                                    gonderilen.put("Score",skor);
                                    gonderilen.put("Bölüm",kisim);


                                    firestore.collection("Scores").add(gonderilen).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Main4Activity.this,R.string.hata3,Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    countDownTimer.cancel();

                                }
                            }
                        });
                    }
                }
            }
        });
    }


    public void sorukontrol()
    {
        rastgelesoru=rastgele.nextInt(11) + 10;

        for(i=0;i<10;i++){
            if(cekilensorular[i] == rastgelesoru || rastgelesoru == 10)
            {
                sorukontrol();
            }
        }

        cekilensorular[ssayi]=rastgelesoru;

    }


}