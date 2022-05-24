package ru.yudin_r.teamwork;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import ru.yudin_r.teamwork.models.Project;
import ru.yudin_r.teamwork.models.User;
import ru.yudin_r.teamwork.tools.Database;

public class NameActivity extends AppCompatActivity {

    private TextInputEditText firstNameField, secondNameField;
    private String password, email;
    private Button createNewAccButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        firstNameField = findViewById(R.id.firstNameField);
        secondNameField = findViewById(R.id.secondNameField);
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        createNewAccButton = findViewById(R.id.createNewAccButton);
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        createNewAccButton.setEnabled(false);

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
                if (firstNameField.getText().toString().length() > 0
                        && secondNameField.getText().toString().length() > 0) {
                    createNewAccButton.setEnabled(true);
                }
            }
        };

        firstNameField.addTextChangedListener(textWatcher);
        secondNameField.addTextChangedListener(textWatcher);

        createNewAccButton.setOnClickListener(v -> createNewAcc());
    }

    private void createNewAcc() {
        String firstName = firstNameField.getText().toString();
        String secondName = secondNameField.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                task -> {
                    String id = new Database().getId();
                    User user = new User(id, firstName, secondName, email, password);
                    new Database().insertUserData(user);
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password);
                    ArrayList<String> users = new ArrayList<>();
                    users.add(id);
                    Project project = new Project(null, "Hello, Teamwork!", id, users);
                    new Database().insertProjectData(project);
                    startActivity(new Intent(NameActivity.this, MainActivity.class));
                    finish();
                }
        ).addOnFailureListener(e -> {
        });
    }
}