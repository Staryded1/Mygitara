package com.example.kursach.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kursach.R;

public class AdminCategoryActivity extends AppCompatActivity {

    private EditText men, woman, kids;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        init();

        men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "men");
                startActivity(intent);
            }
        });

        woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "woman");
                startActivity(intent);
            }
        });

        kids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "kids");
                startActivity(intent);
            }
        });


    }

    private void init() {
        men = findViewById(R.id.men);
        woman = findViewById(R.id.woman);
        kids = findViewById(R.id.kids);

    }
}