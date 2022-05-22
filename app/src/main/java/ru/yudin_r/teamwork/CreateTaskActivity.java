package ru.yudin_r.teamwork;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import ru.yudin_r.teamwork.models.Task;
import ru.yudin_r.teamwork.tools.Database;

public class CreateTaskActivity extends AppCompatActivity {

    MaterialToolbar topAppBar;
    private TextInputEditText taskTextField;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        taskTextField = findViewById(R.id.taskTextField);
        Button createButton = findViewById(R.id.createButton);
        id = getIntent().getStringExtra("boardId");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        createButton.setOnClickListener(v -> createTask());
    }

    private void createTask() {
        String taskText = taskTextField.getText().toString();
        String creatorId = new Database().getId();

        Task task = new Task(null, id, taskText, creatorId, 0);
        new Database().insertTaskData(task);

    }
}