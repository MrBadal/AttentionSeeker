package com.it.attentionseeker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import es.dmoral.toasty.Toasty;

public class Attention extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button clickN;
    public String key, target, mobile, gName, sName;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention);

        final MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.bell);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        mobile = currentUser.getPhoneNumber();
        clickN = findViewById(R.id.notiClick);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(mobile).child("target").getValue() != null){
                    target = dataSnapshot.child(mobile).child("target").getValue(String.class);
                    gName = dataSnapshot.child(mobile).child("giver").child("giverName").getValue(String.class);
                    Log.d("Target", "Value " + target);
                    clickN.setText("Click to Notify "+ "'"+gName+"'");
                }else {
                    Toasty.error(Attention.this,"'Attention Giver' Not Paired Yet !",Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        clickN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(mobile).child("target").exists()){
                            gName = snapshot.child(mobile).child("giver").child("giverName").getValue(String.class);
                            sName = snapshot.child(mobile).child("seeker").child("seekerName").getValue(String.class);

                            key =  snapshot.child(target).child("notificationKey").getValue(String.class);
                            new SendNotification("Hey " +gName+", "+sName+" is missing you.","Your attention has been requested 🥰", key);
                            clickN.setText("NOTIFIED!");
                            clickN.setEnabled(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}