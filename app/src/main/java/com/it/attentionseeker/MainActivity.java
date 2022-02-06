package com.it.attentionseeker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.onesignal.OSDeviceState;
import com.onesignal.OneSignal;

public class MainActivity extends AppCompatActivity {

    private static final String ONESIGNAL_APP_ID = "2c1c10eb-f246-4285-81b9-ae158a69b20a";

    FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    Button attention, button1, button2, button3, button4;
    String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        mobile = currentUser.getPhoneNumber();
        attention = findViewById(R.id.attention);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);

        OSDeviceState osDeviceState = OneSignal.getDeviceState();
        String notificationId = osDeviceState != null ? osDeviceState.getUserId() : null;
        FirebaseDatabase.getInstance().getReference().child(mobile).child("notificationKey").setValue(notificationId);


        assert mobile != null;
        databaseReference = FirebaseDatabase.getInstance().getReference(mobile);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child("mobile").setValue(mobile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void attention(View v){
        startActivity(new Intent(MainActivity.this, Attention.class));
        attention.playSoundEffect(0);
    }

    public void name(View v){
        startActivity(new Intent(MainActivity.this, SeekerActivity.class));
        button1.playSoundEffect(0);

    }

    public void giver(View v){
        startActivity(new Intent(MainActivity.this,GiverName.class));
        button2.playSoundEffect(0);
    }

    public void code(View v){
        startActivity(new Intent(MainActivity.this,CodeCreate.class));
        button3.playSoundEffect(0);
    }

    public void insert(View v){
        startActivity(new Intent(MainActivity.this,CodeInsert.class));
        button4.playSoundEffect(0);
    }

}