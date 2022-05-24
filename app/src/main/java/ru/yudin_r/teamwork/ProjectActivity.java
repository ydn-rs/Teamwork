package ru.yudin_r.teamwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;

import java.util.ArrayList;

import ru.yudin_r.teamwork.adapters.TaskAdapter;
import ru.yudin_r.teamwork.adapters.TaskSwiper;
import ru.yudin_r.teamwork.models.Task;
import ru.yudin_r.teamwork.tools.Constants;
import ru.yudin_r.teamwork.tools.Database;

public class ProjectActivity extends AppCompatActivity {

    static private String projectId;
    private MaterialToolbar topAppBar;
    private RecyclerView taskRv;
    private String creatorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        FloatingActionButton createTaskFAB = findViewById(R.id.createTaskFAB);
        taskRv = findViewById(R.id.taskList);
        if (getIntent().getStringExtra("projectId") != null) {
            projectId = getIntent().getStringExtra("projectId");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        createTaskFAB.setOnClickListener(v -> {
            Intent intent = new Intent(ProjectActivity.this, CreateTaskActivity.class);
            intent.putExtra("projectId", projectId);
            startActivity(intent);
        });

        setTitle();
        new Database().getTaskList(ProjectActivity.this, projectId);
        menuOnClickListeners();
        eventListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top_project_app_bar, menu);
        return true;
    }

    private void menuOnClickListeners() {
        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.settings) {
                if (new Database().getId().equals(creatorId)) {
                    Intent intent = new Intent(ProjectActivity.this, ManageProjectActivity.class);
                    intent.putExtra("projectId", projectId);
                    startActivity(intent);
                } else {
                    Toast.makeText(ProjectActivity.this, "У вас нет досутпа к этому разделу", Toast.LENGTH_SHORT)
                            .show();
                }
                return true;
            } else {
                return false;
            }
        });
    }

    private void setTitle() {
        new Database().getProjectData(projectId, project -> {
            topAppBar.setTitle("Проект '" + project.getTitle() + "'");
            this.creatorId = project.getCreatorId();
        });
    }

    public void showTaskList(ArrayList<Task> tasks) {
        taskRv.setLayoutManager(new LinearLayoutManager(ProjectActivity.this));
        TaskAdapter taskAdapter = new TaskAdapter(tasks);
        taskRv.setAdapter(taskAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new TaskSwiper(ProjectActivity.this, taskAdapter));
        itemTouchHelper.attachToRecyclerView(taskRv);
    }

    private void eventListener() {
        new Database().getDb().collection(Constants.TASKS).whereEqualTo("projectId", projectId)
                .addSnapshotListener((value, error) -> {
                    for (DocumentChange documentChange : value.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED ||
                                documentChange.getType() == DocumentChange.Type.REMOVED ||
                                documentChange.getType() == DocumentChange.Type.MODIFIED) {
                            new Database().getTaskList(ProjectActivity.this, projectId);
                        }
                    }
                });
    }

}