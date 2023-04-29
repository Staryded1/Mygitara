package com.example.kursach;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kursach.Admin.AdminCategoryActivity;
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

    private Button loginBtn;
    private EditText  emailInput, passwordInput;
    private ProgressDialog loadingbar;
    private TextView AdminLink, NotAdminLink;

    private String parentDbName ="Users";
    private CheckBox checkBoxRememberMe;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = (Button) findViewById(R.id.login_btn);
        emailInput = (EditText) findViewById(R.id.login_email_input);
        passwordInput = (EditText) findViewById(R.id.login_password_input);
        loadingbar = new ProgressDialog(this);
        checkBoxRememberMe = (CheckBox)findViewById(R.id.login_checkbox);
        Paper.init(this);

        AdminLink = (TextView)findViewById(R.id.admin_panel_link);
        NotAdminLink = (TextView)findViewById(R.id.not_admin_panel_link);

        loginBtn.setOnClickListener(v -> loginUser());

        AdminLink.setOnClickListener(v -> {
            AdminLink.setVisibility(View.INVISIBLE);
            NotAdminLink.setVisibility(View.VISIBLE);
            loginBtn.setText("Вход для админа");
            parentDbName ="Admins";
        });

        NotAdminLink.setOnClickListener(v -> {
            AdminLink.setVisibility(View.VISIBLE);
            NotAdminLink.setVisibility(View.INVISIBLE);
            loginBtn.setText("Войти");
            parentDbName ="Users";
        });
    }


    private void loginUser()
    {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Введите email", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show();

        } else
        {
            loadingbar.setTitle("Вход в приложение");
            loadingbar.setMessage("Пожалуйста, подождите");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            ValidateUser(email, password);
        }
    }

    private void ValidateUser( final String email, final String password)
    {


        if(checkBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent.UserEmailKey, email);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDbName).child(email).exists())
                {
                    Users usersData = snapshot.child(parentDbName).child(email).getValue(Users.class);

                    if(usersData.getEmail().equals(email))
                    {
                        if(usersData.getPassword().equals(password))
                        {
                            loadingbar.dismiss();
                            Toast.makeText(LoginActivity.this, "Успешный вход", Toast.LENGTH_SHORT).show();

                            Intent homeIntent = new Intent(LoginActivity.this,HomeeActivity.class);
                            startActivity(homeIntent);
                        }
                        else if(parentDbName.equals("Admins")){
                            loadingbar.dismiss();
                            Toast.makeText(LoginActivity.this, "Успешный вход!", Toast.LENGTH_SHORT).show();

                            Intent homeIntent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
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
                    Toast.makeText(LoginActivity.this,"Эта почта " + email + " не существует", Toast.LENGTH_SHORT).show();

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