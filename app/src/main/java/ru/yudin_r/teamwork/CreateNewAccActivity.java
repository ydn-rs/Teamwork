package ru.yudin_r.teamwork;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import ru.yudin_r.teamwork.tools.Database;

public class CreateNewAccActivity extends AppCompatActivity {

    private TextInputEditText emailField, passwordField;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_acc);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        nextButton = findViewById(R.id.nextButton);
        TextView emailTv = findViewById(R.id.emailTv);
        String email = getIntent().getStringExtra("email");
        emailTv.setText(email);
        emailField.setText(email);
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        nextButton.setEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (emailField.getText().toString().length() > 0 &&
                        passwordField.getText().toString().length() > 0) {
                    nextButton.setEnabled(true);
                }
            }
        };

        emailField.addTextChangedListener(textWatcher);
        passwordField.addTextChangedListener(textWatcher);

        nextButton.setOnClickListener(v -> {

            String email_input = emailField.getText().toString().toLowerCase().replaceAll(" ", "");
            String password = passwordField.getText().toString();

            if (password.length() < 6) {
                passwordField.setError("Пароль должен содержать 6+ символов");
            } else {
                new Database().checkEmail(email_input, b -> {
                    if (b) {
                        Intent intent = new Intent(CreateNewAccActivity.this, PasswordActivity.class);
                        intent.putExtra("email", email_input);
                        startActivity(intent);
                        finish();
                    } else {
                        if (email_input.length() == 0) {
                            emailField.setError("Введите email!");
                        } else {
                            Intent intent = new Intent(CreateNewAccActivity.this, NameActivity.class);
                            intent.putExtra("email", email_input);
                            intent.putExtra("password", password);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }
}