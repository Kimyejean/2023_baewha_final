package com.example.refrigerator_2023final;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RecipeRegistPage extends AppCompatActivity {
    private String[] items = {"한식", "중식", "일식", "양식", "비건식", "국/찌개", "반찬", "면"};
    private boolean[] checkedItems = {false, false, false, false, false, false, false, false};
    private String[] allergyItems = {"계란", "우유", "밀", "메밀", "견과류", "육류", "생선", "복숭아", "새우", "게"};
    private boolean[] checkedAllergies = {false, false, false, false, false, false, false, false, false, false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button openDialogButton2 = findViewById(R.id.openDialogButton2);
        openDialogButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCustomDialog(items, checkedItems);
            }
        });

        Button openDialogButton3 = findViewById(R.id.openDialogButton3);
        openDialogButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCustomDialog(allergyItems, checkedAllergies);
            }
        });
    }

    private void openCustomDialog(final String[] dialogItems, final boolean[] checkedItems) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("카테고리");

        builder.setMultiChoiceItems(dialogItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            }
        });

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}