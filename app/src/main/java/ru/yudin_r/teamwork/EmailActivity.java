package ru.yudin_r.teamwork;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import ru.yudin_r.teamwork.tools.Database;

public class EmailActivity extends AppCompatActivity {

    private TextInputEditText emailField;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        emailField = findViewById(R.id.emailField);
        nextButton = findViewById(R.id.nextButton);
        nextButton.setEnabled(false);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                nextButton.setEnabled(emailField.getText().toString().length() > 0 &&
                        emailField.getText().toString().contains("@") &&
                        emailField.getText().toString().contains("."));
            }
        };

        emailField.addTextChangedListener(textWatcher);

        nextButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().toLowerCase().replaceAll(" ", "");
            new Database().checkEmail(email, b -> {
                if (b) {
                    goLogin(email);
                } else {
                    goCreateNewAcc(email);
                }
            });
        });
    }

    public void goLogin(String email) {
        Intent intent = new Intent(EmailActivity.this, PasswordActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    public void goCreateNewAcc(String email) {
        Intent intent = new Intent(EmailActivity.this, CreateNewAccActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }

}