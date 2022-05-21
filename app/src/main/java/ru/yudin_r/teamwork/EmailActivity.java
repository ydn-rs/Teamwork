package ru.yudin_r.teamwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString();
                new Database().checkEmail(EmailActivity.this, email);
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