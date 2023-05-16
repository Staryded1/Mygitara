package com.example.kursach;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kursach.Model.Users;
import com.example.kursach.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity
{

    private EditText phoneInput, passwordInput;
    private ProgressDialog loadingbar;

    private final String parentDbName ="Users";
    private CheckBox checkBoxRememberMe;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginBtn = (Button) findViewById(R.id.login_btn);
        phoneInput = (EditText) findViewById(R.id.login_phone_input);
        passwordInput = (EditText) findViewById(R.id.login_password_input);
        loadingbar = new ProgressDialog(this);
        checkBoxRememberMe = (CheckBox)findViewById(R.id.login_checkbox);
        Paper.init(this);



        loginBtn.setOnClickListener(v -> loginUser());


    }


    private void loginUser()
    {
        String phone = phoneInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Введите номер телефона", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show();

        } else
        {
            loadingbar.setTitle("Вход в приложение");
            loadingbar.setMessage("Пожалуйста, подождите");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            ValidateUser(phone, password);
        }
    }

    private void ValidateUser( final String phone, final String password)
    {


        if(checkBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDbName).child(phone).exists())
                {
                    Users usersData = snapshot.child(parentDbName).child(phone).getValue(Users.class);


                    if(usersData.getPhone().equals(phone))
                    {
                        if(usersData.getPassword().equals(password))
                        {
                            Prevalent.currentOnlineUser = usersData;
                            loadingbar.dismiss();
                            Toast.makeText(LoginActivity.this, "Успешный вход", Toast.LENGTH_SHORT).show();

                            Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                            homeIntent.putExtra("phone", phone);

                            startActivity(homeIntent);

                        }
                        else
                        {
                            loadingbar.dismiss();
                            Toast.makeText(LoginActivity.this, "Неверный пароль", Toast.LENGTH_SHORT).show();

                        }

                    }

                }
                else
                {
                    loadingbar.dismiss();
                    Toast.makeText(LoginActivity.this,"Эта почта " + phone + " не существует", Toast.LENGTH_SHORT).show();

                    Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                    startActivity(registerIntent);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}