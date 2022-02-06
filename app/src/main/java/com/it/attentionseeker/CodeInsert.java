package com.it.attentionseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class CodeInsert extends AppCompatActivity {

    Button verify_otp;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference,db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_insert);

        mAuth = FirebaseAuth.getInstance();
        String mobile = mAuth.getCurrentUser().getPhoneNumber();

        EditText edit = findViewById(R.id.codeInsert);

        edit.requestFocus();

        verify_otp = findViewById(R.id.verify_otp_btn);


        verify_otp.setOnClickListener(view -> {

            String insertCode = edit.getText().toString();
            if (insertCode.isEmpty()){
                Toasty.warning(CodeInsert.this, "insert the code of your seeker", Toasty.LENGTH_SHORT, true).show();
            }else {
                    FirebaseDatabase.getInstance().getReference("code").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    String code = snapshot.getKey();
                                    if (insertCode.equals(code)){
                                        Toasty.success(CodeInsert.this, "Pairing Successful", Toasty.LENGTH_SHORT).show();
                                        databaseReference = FirebaseDatabase.getInstance().getReference(mobile);
                                        databaseReference.child("target").setValue(snapshot.getValue());
                                        db = FirebaseDatabase.getInstance().getReference(snapshot.getValue().toString());
                                        db.child("target").setValue(mobile);
                                        finish();
                                    }else {
                                        Toasty.error(CodeInsert.this, "Invalid Code", Toasty.LENGTH_SHORT).show();
                                    }
                                }

                            }else {
                                Toasty.error(CodeInsert.this, "Invalid Code", Toasty.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            }

        });


    }


}