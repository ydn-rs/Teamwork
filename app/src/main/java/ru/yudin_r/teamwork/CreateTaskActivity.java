package ru.yudin_r.teamwork;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import ru.yudin_r.teamwork.models.Task;
import ru.yudin_r.teamwork.tools.Database;

public class CreateTaskActivity extends AppCompatActivity {

    private TextInputEditText taskTextField;
    private Button createButton;
    MaterialToolbar topAppBar;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        taskTextField = findViewById(R.id.taskTextField);
        createButton = findViewById(R.id.createButton);
        id = getIntent().getStringExtra("boardId");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTask();
            }
        });
    }

    private void createTask() {
        String taskText = taskTextField.getText().toString();
        String creatorId = new Database().getId();

        Task task = new Task(null, id, taskText, creatorId, 0);
        new Database().insertTaskData(task);

    }
}