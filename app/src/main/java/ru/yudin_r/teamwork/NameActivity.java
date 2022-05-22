package ru.yudin_r.teamwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ru.yudin_r.teamwork.models.User;
import ru.yudin_r.teamwork.tools.Database;

public class NameActivity extends AppCompatActivity {

    private TextInputEditText firstNameField, secondNameField;
    private String password, email;
    private Button createNewAccButton;
    private MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        firstNameField = findViewById(R.id.firstNameField);
        secondNameField = findViewById(R.id.secondNameField);
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        createNewAccButton = findViewById(R.id.createNewAccButton);
        topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        createNewAccButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAcc();
            }
        });
    }

    private void createNewAcc() {
        String firstName = firstNameField.getText().toString();
        String secondName = secondNameField.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String id = new Database().getId();
                        User user = new User(id, firstName, secondName, email, password);
                        new Database().insertUserData(user);
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password);
                        startActivity(new Intent(NameActivity.this, MainActivity.class));
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}