package com.example.refrigerator_2023final;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class allergyfilterpage extends AppCompatActivity {

    CheckBox[] checkBoxes;
    Integer[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allergyfilterpage);

        checkBoxes = new CheckBox[10];
        items = new Integer[]{R.id.checkBox, R.id.checkBox2, R.id.checkBox3, R.id.checkBox4, R.id.checkBox5, R.id.checkBox6, R.id.checkBox7, R.id.checkBox8, R.id.checkBox9, R.id.checkBox10};

        for (int i = 0; i < checkBoxes.length; i++) {
            checkBoxes[i] = findViewById(items[i]);
            checkBoxes[i].setOnCheckedChangeListener(checkChangeListener);
        }
    }

    CompoundButton.OnCheckedChangeListener checkChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
            ArrayList<String> arr = new ArrayList<>();

            for (int i = 0; i < checkBoxes.length; i++) {
                if (checkBoxes[i].isChecked() == true) {
                    arr.add(checkBoxes[i].getText().toString());
                }
            }
            if (!arr.isEmpty()) {
                Toast.makeText(allergyfilterpage.this, arr.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    };
}