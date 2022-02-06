package com.it.attentionseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;


public class CodeCreate extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_create);


        Button popupButton = findViewById(R.id.buttonPopup);
            popupButton.setOnClickListener(v -> {

                PopUpWindow popUpClass = new PopUpWindow();
                popUpClass.showPopupWindow(v);
            });
    }

}