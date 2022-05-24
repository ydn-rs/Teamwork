package ru.yudin_r.teamwork;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import ru.yudin_r.teamwork.models.Project;
import ru.yudin_r.teamwork.tools.Database;

public class CreateProjectActivity extends AppCompatActivity {

    MaterialToolbar topAppBar;
    private TextInputEditText projectTitleField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
        topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        projectTitleField = findViewById(R.id.projectTitleField);
        Button createButton = findViewById(R.id.createButton);

        createButton.setOnClickListener(v -> createProject());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private void createProject() {
        String projectTitle = projectTitleField.getText().toString();
        String creatorId = new Database().getId();
        ArrayList<String> users = new ArrayList<>();
        users.add(creatorId);

        Project project = new Project(null, projectTitle, creatorId, users);
        new Database().insertProjectData(project);
        Toast.makeText(CreateProjectActivity.this, "Проект успешно создан!", Toast.LENGTH_SHORT).show();
        projectTitleField.setText(null);
    }
}