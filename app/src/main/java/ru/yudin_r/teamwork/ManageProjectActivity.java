package ru.yudin_r.teamwork;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import ru.yudin_r.teamwork.adapters.UserAdapter;
import ru.yudin_r.teamwork.adapters.UserSwiper;
import ru.yudin_r.teamwork.models.User;
import ru.yudin_r.teamwork.tools.Database;

public class ManageProjectActivity extends AppCompatActivity {

    static private String projectId;
    private MaterialToolbar topAppBar;
    private RecyclerView userRv;
    private TextInputEditText projectTitleField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_project);
        topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        FloatingActionButton inviteUserFAB = findViewById(R.id.inviteUserFAB);
        userRv = findViewById(R.id.userList);

        if (getIntent().getStringExtra("projectId") != null) {
            projectId = getIntent().getStringExtra("projectId");
        }

        projectTitleField = findViewById(R.id.projectTitleField);
        Button saveButton = findViewById(R.id.saveButton);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        saveButton.setOnClickListener(v -> updateProjectTitle());

        inviteUserFAB.setOnClickListener(v -> {
            Intent intent = new Intent(
                    ManageProjectActivity.this, InviteUserActivity.class);
            intent.putExtra("projectId", projectId);
            startActivity(intent);
        });

        setTitle();
        getProjectUsers();

    }

    private void setTitle() {
        new Database().getProjectData(projectId, project -> {
            topAppBar.setTitle("Управление '" + project.getTitle() + "'");
            projectTitleField.setText(project.getTitle());
        });
    }

    private void getProjectUsers() {
        new Database().getProjectData(projectId, project ->
                new Database().getUserList(ManageProjectActivity.this, project.getUsers()));
    }

    public void showUserList(ArrayList<User> users) {
        userRv.setLayoutManager(new LinearLayoutManager(ManageProjectActivity.this));
        UserAdapter userAdapter = new UserAdapter(users);
        userRv.setAdapter(userAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new UserSwiper(ManageProjectActivity.this, userAdapter, projectId));
        itemTouchHelper.attachToRecyclerView(userRv);
    }

    private void updateProjectTitle() {
        String title = projectTitleField.getText().toString();
        new Database().getProjectData(projectId, project -> {
            project.setTitle(title);
            new Database().updateProjectData(projectId, project);
        });
    }
}