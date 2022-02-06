package com.it.attentionseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class SeekerActivity extends AppCompatActivity {


    private EditText user;
    private TextView seeker;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker);
        mAuth = FirebaseAuth.getInstance();
        TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();

        Log.i("Country Code: ",countryCodeValue);

        String mobile = mAuth.getCurrentUser().getPhoneNumber();
        user = findViewById(R.id.seekerName);
        seeker = findViewById(R.id.seeker);
        Button submit = findViewById(R.id.submitBtn);
        databaseReference = FirebaseDatabase.getInstance().getReference(mobile);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addName();
            }
        });

        databaseReference.child("seeker").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String sName = snapshot.child("seekerName").getValue(String.class);
                    seeker.setText(sName);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        
    }

    private void addName() {
        String name = user.getText().toString().trim();
        if (!TextUtils.isEmpty(name)) {
            SeekerName seeker = new SeekerName(name);
            databaseReference.child("seeker").setValue(seeker);
            Toasty.success(SeekerActivity.this,"Your Name Added Successfully.",Toasty.LENGTH_SHORT).show();
            finish();

        } else {
            Toasty.error(SeekerActivity.this,"Your Name Please.",Toasty.LENGTH_SHORT).show();
        }

    }

}