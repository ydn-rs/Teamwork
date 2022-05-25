package ru.yudin_r.teamwork;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import ru.yudin_r.teamwork.tools.Database;

public class InviteUserActivity extends AppCompatActivity {

    MaterialToolbar topAppBar;
    private TextInputEditText emailField;
    private String projectId;
    private String uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_user);
        topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        emailField = findViewById(R.id.emailField);
        Button inviteButton = findViewById(R.id.inviteButton);
        projectId = getIntent().getStringExtra("projectId");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        inviteButton.setOnClickListener(v -> inviteUser());
    }

    public void getId(String uId) {
        this.uId = uId;
    }

    private void inviteUser() {
        String email = emailField.getText().toString().toLowerCase().replaceAll(" ", "");

        new Database().checkEmail(email, b -> {
            if (b) {
                new Database().getUserDataByEmail(InviteUserActivity.this, email);

                new Database().getProjectData(projectId, project -> {
                    ArrayList<String> users = project.getUsers();
                    if (users.contains(uId)) {
                        new Database().showMsg(InviteUserActivity.this, "Пользователь уже присутствует в проекте");
                    } else {
                        users.add(uId);
                        project.setUsers(users);
                        new Database().updateProjectData(projectId, project);
                        new Database().showMsg(InviteUserActivity.this, "Пользователь добавлен");
                        emailField.setText(null);
                    }
                });
            } else {
                new Database().showMsg(InviteUserActivity.this, "Пользователь не найден");
            }
        });
    }
}