package ru.yudin_r.teamwork;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
        createButton.setEnabled(false);

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
                createButton.setEnabled(taskTextField.getText().toString().length() > 0);
            }
        };

        taskTextField.addTextChangedListener(textWatcher);

        createButton.setOnClickListener(v -> createTask());
    }

    private void createTask() {
        String taskText = taskTextField.getText().toString();
        String creatorId = new Database().getId();

        Task task = new Task(null, projectId, taskText, creatorId, 0);
        new Database().insertTaskData(task);
        new Database().showMsg(CreateTaskActivity.this, "Задача успешно создана!");

        taskTextField.setText(null);
    }
}