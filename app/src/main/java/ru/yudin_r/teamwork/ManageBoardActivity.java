package ru.yudin_r.teamwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import ru.yudin_r.teamwork.adapters.BoardSwiper;
import ru.yudin_r.teamwork.adapters.TaskAdapter;
import ru.yudin_r.teamwork.adapters.TaskSwiper;
import ru.yudin_r.teamwork.adapters.UserAdapter;
import ru.yudin_r.teamwork.adapters.UserSwiper;
import ru.yudin_r.teamwork.models.Board;
import ru.yudin_r.teamwork.models.Task;
import ru.yudin_r.teamwork.models.User;
import ru.yudin_r.teamwork.tools.Database;
import ru.yudin_r.teamwork.tools.OnGetBoard;

public class ManageBoardActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;
    private RecyclerView userRv;
    private FloatingActionButton inviteUserFAB;
    static private String id;
    private TextInputEditText boardTitleField;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_board);
        topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        inviteUserFAB = findViewById(R.id.inviteUserFAB);
        userRv = findViewById(R.id.userList);

        if (getIntent().getStringExtra("boardId") != null) {
            id = getIntent().getStringExtra("boardId");
        }

        boardTitleField = findViewById(R.id.boardTitleField);
        saveButton = findViewById(R.id.saveButton);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBoardTitle();
            }
        });

        inviteUserFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        ManageBoardActivity.this, InviteUserActivity.class);
                intent.putExtra("boardId", id);
                startActivity(intent);
            }
        });

        setTitle();
        getBoardUsers();

    }

    private void setTitle() {
        new Database().getBoardData(id, new OnGetBoard() {
            @Override
            public void OnGetBoard(Board board) {
                topAppBar.setTitle("Управление '" + board.getTitle() + "'");
                boardTitleField.setText(board.getTitle());
            }
        });
    }

    private void getBoardUsers() {
        new Database().getBoardData(id, new OnGetBoard() {
            @Override
            public void OnGetBoard(Board board) {
                new Database().getUserList(ManageBoardActivity.this, board.getUsers());
            }
        });
    }

    public void showUserList(ArrayList<User> userList) {
        userRv.setLayoutManager(new LinearLayoutManager(ManageBoardActivity.this));
        UserAdapter userAdapter = new UserAdapter(userList);
        userRv.setAdapter(userAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new UserSwiper(ManageBoardActivity.this, userAdapter, id));
        itemTouchHelper.attachToRecyclerView(userRv);
    }

    private void updateBoardTitle() {
        String title = boardTitleField.getText().toString();
        new Database().getBoardData(id, new OnGetBoard() {
            @Override
            public void OnGetBoard(Board board) {
                board.setTitle(title);
                new Database().updateBoardData(id, board);
            }
        });
    }
}