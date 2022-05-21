package ru.yudin_r.teamwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ru.yudin_r.teamwork.models.User;
import ru.yudin_r.teamwork.tools.Database;

public class CreateNewAccActivity extends AppCompatActivity {

    private TextInputEditText firstNameField, secondNameField, emailField, passwordField;
    private Button createNewAccButton, loginTextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_acc);
        firstNameField = findViewById(R.id.firstNameField);
        secondNameField= findViewById(R.id.secondNameField);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        createNewAccButton = findViewById(R.id.createNewAccButton);
        loginTextButton = findViewById(R.id.loginTextButton);

        loginTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String id = new Database().getId();
                            User user = new User(id, firstName, secondName, email, password);
                            new Database().insertUserData(user);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}