package com.example.mobileproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.okhttp.internal.DiskLruCache;

import org.w3c.dom.Text;

import java.lang.ref.Reference;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main5Activity extends AppCompatActivity {

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        firestore=FirebaseFirestore.getInstance();

        vericek();
    }

    String mailler[]=new String[10];
    String puanlar[]=new String[10];
    String bolumler[]=new String[10];
    int i=0;


    public void vericek()
    {
        CollectionReference collectionReference = firestore.collection("Scores");

        collectionReference.orderBy("Score",Query.Direction.DESCENDING).limit(10).whereGreaterThan("Score",0).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if(queryDocumentSnapshots != null) {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();

                        String mail=(String) data.get("E-mail");

                        String puan=(String.valueOf(data.get("Score")));

                        String secenek=(String) data.get("Bölüm");
                        String secenek2="";

                        if(secenek.equals("tring"))
                        {
                            secenek2="Turkish -> English";
                        }
                        else if(secenek.equals("ingtr"))
                        {
                            secenek2="English -> Turkish";
                        }

                        mailler[i]=mail;
                        puanlar[i]=puan;
                        bolumler[i]=secenek2;
                        i++;

                            cekcek();

                    }

                }

            }

        });

    }
    int u;

    public void cekcek()
    {

        ListView listem=findViewById(R.id.liste);

        ArrayList<String> elemanlar=new ArrayList<>();


        for(u=0;u<mailler.length;u++)
        {
            if(mailler[u] != null)
            {
                elemanlar.add(mailler[u]+"    "+puanlar[u]+"     "+bolumler[u]);
            }

        }

        ArrayAdapter adapter = new ArrayAdapter(Main5Activity.this,android.R.layout.simple_list_item_1,elemanlar);

        listem.setAdapter(adapter);

    }

    }