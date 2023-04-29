package com.example.kursach;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button registerBtn;
    private EditText usernameInput, surnameInput, emailInput, passwordInput;

    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBtn = findViewById(R.id.register_btn);
        usernameInput = findViewById(R.id.register_name_input);
        surnameInput = findViewById(R.id.register_surname_input);
        emailInput = findViewById(R.id.register_email_input);
        passwordInput = findViewById(R.id.register_password_input);
        loadingbar = new ProgressDialog(this);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {

        String name = usernameInput.getText().toString();
        String surname = surnameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();


        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Введите имя", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(surname)) {
            Toast.makeText(this, "Введите фамилию", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Введите email", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show();

        } else {
            loadingbar.setTitle("Регистрация аккаунта...");
            loadingbar.setMessage("Пожалуйста, подождите");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            ValidateEmail(name, surname, email, password);


        }
    }


    private void ValidateEmail(String name, String surname, String email, String password)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Users").child(email).exists()))
                {
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("name",name);
                    userDataMap.put("surname",surname);
                    userDataMap.put("email",email);
                    userDataMap.put("password",password);

                    RootRef.child("Users").child(email).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {


                                    if(task.isSuccessful())
                                    {
                                        loadingbar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();

                                        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(loginIntent);
                                    }
                                    else
                                    {
                                        loadingbar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Ошибка, такой email уже существует", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });

                }
                else
                {
                    loadingbar.dismiss();
                    Toast.makeText(RegisterActivity.this,"Email" + email +"Этот email уже используется", Toast.LENGTH_SHORT).show();


                    Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}