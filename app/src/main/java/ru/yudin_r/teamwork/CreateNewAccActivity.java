package ru.yudin_r.teamwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ru.yudin_r.teamwork.models.User;
import ru.yudin_r.teamwork.tools.Database;

public class CreateNewAccActivity extends AppCompatActivity {

    private TextInputEditText emailField, passwordField;
    private Button nextButton, emailTextButton;
    private String email;
    private TextView emailTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_acc);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        nextButton = findViewById(R.id.nextButton);
        emailTextButton = findViewById(R.id.emailTextButton);
        emailTv = findViewById(R.id.emailTv);
        email = getIntent().getStringExtra("email");
        emailTv.setText(email);
        emailField.setText(email);

        emailTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                Intent intent = new Intent(CreateNewAccActivity.this, NameActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                startActivity(intent);
                finish();
            }
        });
    }
}