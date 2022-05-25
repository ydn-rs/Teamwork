package ru.yudin_r.teamwork;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
        createButton.setEnabled(false);

        createButton.setOnClickListener(v -> createProject());

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
                createButton.setEnabled(projectTitleField.getText().toString().length() > 0);
            }
        };

        projectTitleField.addTextChangedListener(textWatcher);

    }

    private void createProject() {
        String projectTitle = projectTitleField.getText().toString();
        String creatorId = new Database().getId();
        ArrayList<String> users = new ArrayList<>();
        users.add(creatorId);

        Project project = new Project(null, projectTitle, creatorId, users);
        new Database().insertProjectData(project);
        new Database().showMsg(CreateProjectActivity.this, "Проект успешно создан!");
        projectTitleField.setText(null);
    }
}