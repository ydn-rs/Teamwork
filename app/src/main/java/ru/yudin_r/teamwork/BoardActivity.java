package ru.yudin_r.teamwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import ru.yudin_r.teamwork.adapters.TaskAdapter;
import ru.yudin_r.teamwork.adapters.TaskSwiper;
import ru.yudin_r.teamwork.models.Task;
import ru.yudin_r.teamwork.tools.Constants;
import ru.yudin_r.teamwork.tools.Database;

public class BoardActivity extends AppCompatActivity {

    static private String id;
    private MaterialToolbar topAppBar;
    private RecyclerView taskRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        FloatingActionButton createTaskFAB = findViewById(R.id.createTaskFAB);
        taskRv = findViewById(R.id.taskList);
        if (getIntent().getStringExtra("boardId") != null) {
            id = getIntent().getStringExtra("boardId");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        createTaskFAB.setOnClickListener(v -> {
            Intent intent = new Intent(BoardActivity.this, CreateTaskActivity.class);
            intent.putExtra("boardId", id);
            startActivity(intent);
        });

        setTitle();
        new Database().getTaskList(BoardActivity.this, id);
        menuOnClickListeners();
        eventListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top_board_app_bar, menu);
        return true;
    }

    private void menuOnClickListeners() {
        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.settings) {
                Intent intent = new Intent(
                        BoardActivity.this, ManageBoardActivity.class);
                intent.putExtra("boardId", id);
                startActivity(intent);
                return true;
            } else {
                return false;
            }
        });
    }

    private void setTitle() {
        new Database().getBoardData(id, board ->
                topAppBar.setTitle("Проект '" + board.getTitle() + "'"));
    }

    public void showTaskList(ArrayList<Task> taskList) {
        taskRv.setLayoutManager(new LinearLayoutManager(BoardActivity.this));
        TaskAdapter taskAdapter = new TaskAdapter(taskList);
        taskRv.setAdapter(taskAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new TaskSwiper(BoardActivity.this, taskAdapter));
        itemTouchHelper.attachToRecyclerView(taskRv);
    }

    private void eventListener() {
        new Database().getDb().collection(Constants.TASKS).whereEqualTo("boardId", id).orderBy("status",
                Query.Direction.ASCENDING).addSnapshotListener((value, error) -> {
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED ||
                        documentChange.getType() == DocumentChange.Type.REMOVED ||
                        documentChange.getType() == DocumentChange.Type.MODIFIED) {
                    new Database().getTaskList(BoardActivity.this, id);
                }
            }
        });
    }

}