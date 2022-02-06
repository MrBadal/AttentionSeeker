package com.it.attentionseeker;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import es.dmoral.toasty.Toasty;

import static android.content.Context.CLIPBOARD_SERVICE;

public class PopUpWindow {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference, db;

    public static String getRandomNumberString() {

        Random rnd = new Random();
        int number = rnd.nextInt(9999);

        // this will convert any number sequence into 6 character.
        return String.format("%04d", number);
    }

    public void showPopupWindow(final View view) {
        mAuth = FirebaseAuth.getInstance();
        String mobile = mAuth.getCurrentUser().getPhoneNumber();
        databaseReference = FirebaseDatabase.getInstance().getReference(mobile);
        db = FirebaseDatabase.getInstance().getReference("code");


        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_pop_up_window, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        TextView test2 = popupView.findViewById(R.id.titleText);
        test2.setText(getRandomNumberString());

        Button buttonEdit = popupView.findViewById(R.id.messageButton);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager myClipboard = (ClipboardManager) v.getContext().getSystemService(CLIPBOARD_SERVICE);
                String text;
                text = test2.getText().toString();

                ClipData myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);
                databaseReference.child("myCode").setValue(text);
                db.child(text).setValue(mobile);
                //As an example, display the message
                Toasty.info(view.getContext(), "Code Copied", Toasty.LENGTH_SHORT).show();
                popupWindow.dismiss();

            }
        });



        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });
    }




}