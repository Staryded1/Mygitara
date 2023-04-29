package com.example.kursach;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kursach.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class SettingsActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private EditText fullNameEditText, userEmailEditText, addressEditText;
    private TextView saveTextButton,  closeTextBtn;

    private String checker = "";
    private Uri imageUri;
    private StorageReference storageProfilePictureRef;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        profileImageView = (CircleImageView) findViewById(R.id.settings_account_image);
        fullNameEditText = (EditText) findViewById(R.id.settings_fullname);
        userEmailEditText = (EditText) findViewById(R.id.settings_email);
        addressEditText = (EditText) findViewById(R.id.settings_address);
        saveTextButton = (TextView) findViewById(R.id.save_settings_tv);
        closeTextBtn = (TextView) findViewById(R.id.close_settings_tv);
        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");
        userInfoDisplay(profileImageView, fullNameEditText, userEmailEditText,addressEditText);

        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(SettingsActivity.this, HomeeActivity.class);
                startActivity(loginIntent);

            }
        });

        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (checker.equals("clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    updateOnlyUserInfo();
                }
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";




            }
        });


    }

    private void userInfoDisplay( final CircleImageView profileImageView, final EditText fullNameEditText, final EditText userEmailEditText, final EditText addressEditText) {
        String phone = Prevalent.currentOnlineUser.getEmail();
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    if (snapshot.child("image").exists())
                    {
                        String image = snapshot.child("image").getValue().toString();
                        String name = snapshot.child("name").getValue().toString();
                        String email = snapshot.child("email").getValue().toString();
                        String address = snapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);
                        fullNameEditText.setText(name);
                        userEmailEditText.setText(email);
                        addressEditText.setText(address);
                    }

                    if (snapshot.child("address").exists())
                    {
                        String name = snapshot.child("name").getValue().toString();
                        String email = snapshot.child("email").getValue().toString();
                        String address = snapshot.child("address").getValue().toString();

                        fullNameEditText.setText(name);
                        userEmailEditText.setText(email);
                        addressEditText.setText(address);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void userInfoSaved() {
        if (TextUtils.isEmpty(fullNameEditText.getText().toString()))
        {
            Toast.makeText(this, "Заполните имя.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Заполните адрес", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(userEmailEditText.getText().toString()))
        {
            Toast.makeText(this, "Заполните email", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked"))
        {
            uploadImage();

        }

    }





    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Обновляемся..");
        progressDialog.setMessage("Пожалуйста, подождите");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null)
        {
            final StorageReference fileRef = storageProfilePictureRef
                    .child(Prevalent.currentOnlineUser.getEmail() + ".WebP");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                        @Override
                        public Object then(@NonNull Task task) throws Exception
                        {
                            if (!task.isSuccessful())
                            {
                                throw task.getException();
                            }

                            return fileRef.getDownloadUrl();
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful())
                            {
                                Uri downloadUrl = task.getResult();
                                String myUrl = downloadUrl.toString();

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap. put("name", fullNameEditText.getText().toString());
                                userMap. put("address", addressEditText.getText().toString());
                                userMap. put("emailOrder", userEmailEditText.getText().toString());
                                userMap. put("image", myUrl);
                                ref.child(Prevalent.currentOnlineUser.getEmail()).updateChildren(userMap);

                                progressDialog.dismiss();

                                startActivity(new Intent(SettingsActivity.this, HomeeActivity.class));
                                Toast.makeText(SettingsActivity.this, "Информация успешно сохранена", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(SettingsActivity.this, "Error.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "Изображение не выбрано.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap. put("name", fullNameEditText.getText().toString());
        userMap. put("address", addressEditText.getText().toString());
        userMap. put("phoneOrder", userEmailEditText.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getEmail()).updateChildren(userMap);

        startActivity(new Intent(SettingsActivity.this, HomeeActivity.class));
        Toast.makeText(SettingsActivity.this, "Успешно сохранено", Toast.LENGTH_SHORT).show();
        finish();
    }
}