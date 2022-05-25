package ru.yudin_r.teamwork;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import ru.yudin_r.teamwork.tools.Constants;
import ru.yudin_r.teamwork.tools.Database;

public class PasswordActivity extends AppCompatActivity {

    private TextInputEditText passwordField;
    private String email;
    private String password;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        passwordField = findViewById(R.id.passwordField);
        email = getIntent().getStringExtra("email");
        loginButton = findViewById(R.id.loginButton);
        TextView emailTv = findViewById(R.id.emailTv);
        emailTv.setText(email);
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        loginButton.setEnabled(false);


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loginButton.setEnabled(passwordField.getText().toString().length() > 0);
            }
        };

        passwordField.addTextChangedListener(textWatcher);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loginButton.setOnClickListener(v -> login());

    }

    private void login() {

        String password_input = passwordField.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password_input).addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        startActivity(
                                new Intent(
                                        PasswordActivity.this, MainActivity.class));
                    }
                }
        ).addOnFailureListener(e -> new Database().showErrorMsg(PasswordActivity.this, e));
    }
}