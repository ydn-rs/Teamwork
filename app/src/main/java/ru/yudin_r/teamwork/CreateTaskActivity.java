package ru.yudin_r.teamwork;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import ru.yudin_r.teamwork.models.Task;
import ru.yudin_r.teamwork.tools.Database;

public class CreateTaskActivity extends AppCompatActivity {

    MaterialToolbar topAppBar;
    private TextInputEditText taskTextField;
    private String projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        taskTextField = findViewById(R.id.taskTextField);
        Button createButton = findViewById(R.id.createButton);
        projectId = getIntent().getStringExtra("projectId");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        createButton.setOnClickListener(v -> createTask());
    }

    private void createTask() {
        String taskText = taskTextField.getText().toString();
        String creatorId = new Database().getId();

        Task task = new Task(null, projectId, taskText, creatorId, 0);
        new Database().insertTaskData(task);
        Toast.makeText(CreateTaskActivity.this, "Задача успешно создана!", Toast.LENGTH_SHORT).show();
        taskTextField.setText(null);
    }
}