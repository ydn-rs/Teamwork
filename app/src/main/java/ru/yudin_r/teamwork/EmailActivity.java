package ru.yudin_r.teamwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import ru.yudin_r.teamwork.tools.Database;
import ru.yudin_r.teamwork.tools.OnCheckEmail;

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
                if (emailField.getText().toString().length() > 0) {
                    nextButton.setEnabled(true);
                }
            }
        };

        emailField.addTextChangedListener(textWatcher);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString();
                new Database().checkEmail(email, new OnCheckEmail() {
                    @Override
                    public void OnGetEmail(boolean b) {
                        if (b) {
                            goLogin(email);
                        } else {
                            goCreateNewAcc(email);
                        }
                    }
                });
            }
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