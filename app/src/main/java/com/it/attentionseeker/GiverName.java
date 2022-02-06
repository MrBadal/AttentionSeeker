package com.it.attentionseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
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

public class GiverName extends AppCompatActivity {

    private EditText user;
    private TextView attentionGiver;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giver_name);
        mAuth = FirebaseAuth.getInstance();
        String mobile = mAuth.getCurrentUser().getPhoneNumber();
        attentionGiver = findViewById(R.id.giver);
        user = findViewById(R.id.giverName);
        Button submit = findViewById(R.id.submitBtn);
        databaseReference = FirebaseDatabase.getInstance().getReference(mobile);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addName();
            }
        });

        databaseReference.child("giver").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String gName = snapshot.child("giverName").getValue(String.class);
                    attentionGiver.setText(gName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addName() {
        String giverName = user.getText().toString().trim();
        if (!TextUtils.isEmpty(giverName)) {
            Giver giver = new Giver(giverName);
            databaseReference.child("giver").setValue(giver);

            Toasty.success(GiverName.this, "Giver Name Added Successfully", Toasty.LENGTH_SHORT).show();
            finish();

        } else {
            Toasty.error(GiverName.this, "Add Giver Name", Toasty.LENGTH_SHORT).show();
        }

    }
}