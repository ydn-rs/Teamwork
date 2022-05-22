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
    private String id;
    private String uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_user);
        topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        emailField = findViewById(R.id.emailField);
        Button inviteButton = findViewById(R.id.inviteButton);
        id = getIntent().getStringExtra("boardId");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviteUser();
            }
        });
    }

    public void getId(String uId) {
        this.uId = uId;
    }

    private void inviteUser() {
        String email = emailField.getText().toString();

        new Database().checkEmail(email, b -> {
            if (b) {
                new Database().getUserDataByEmail(InviteUserActivity.this, email);

                new Database().getBoardData(id, board -> {
                    ArrayList<String> users = board.getUsers();
                    if (users.contains(uId)) {
                        Toast.makeText(InviteUserActivity.this, "Error", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        users.add(uId);
                        board.setUsers(users);
                        new Database().updateBoardData(id, board);
                    }
                });
            } else {
                Toast.makeText(InviteUserActivity.this, "Проверьте правильность ввода, пробелы", Toast.LENGTH_SHORT)
                        .show();
            }
        });


    }
}