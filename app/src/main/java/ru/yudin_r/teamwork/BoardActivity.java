package ru.yudin_r.teamwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ru.yudin_r.teamwork.adapters.TaskAdapter;
import ru.yudin_r.teamwork.adapters.TaskSwiper;
import ru.yudin_r.teamwork.models.Board;
import ru.yudin_r.teamwork.models.Task;
import ru.yudin_r.teamwork.tools.Database;
import ru.yudin_r.teamwork.tools.OnGetBoard;

public class BoardActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;
    private RecyclerView taskRv;
    private FloatingActionButton createTaskFAB;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        createTaskFAB = findViewById(R.id.createTaskFAB);
        taskRv = findViewById(R.id.taskList);
        id = getIntent().getStringExtra("boardId");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        createTaskFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        BoardActivity.this, CreateTaskActivity.class);
                intent.putExtra("boardId", id);
                startActivity(intent);
            }
        });

        setTitle();
        new Database().getTaskList(BoardActivity.this, id);
        menuOnClickListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top_board_app_bar, menu);
        return true;
    }

    private void menuOnClickListeners() {
        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.settings) {
                    Intent intent = new Intent(
                            BoardActivity.this, ManageBoardActivity.class);
                    intent.putExtra("boardId", id);
                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void setTitle() {
        new Database().getBoardData(id, new OnGetBoard() {
            @Override
            public void OnGetBoard(Board board) {
                topAppBar.setTitle("Проект '" + board.getTitle() + "'");
            }
        });
    }

    public void showTaskList(ArrayList<Task> taskList) {
        taskRv.setLayoutManager(new LinearLayoutManager(BoardActivity.this));
        TaskAdapter taskAdapter = new TaskAdapter(taskList);
        taskRv.setAdapter(taskAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new TaskSwiper(BoardActivity.this, taskAdapter));
        itemTouchHelper.attachToRecyclerView(taskRv);
    }
}