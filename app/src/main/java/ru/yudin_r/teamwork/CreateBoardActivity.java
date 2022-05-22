package ru.yudin_r.teamwork;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import ru.yudin_r.teamwork.models.Board;
import ru.yudin_r.teamwork.tools.Database;

public class CreateBoardActivity extends AppCompatActivity {

    MaterialToolbar topAppBar;
    private TextInputEditText boardTitleField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);
        topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        boardTitleField = findViewById(R.id.boardTitleField);
        Button createButton = findViewById(R.id.createButton);

        createButton.setOnClickListener(v -> createBoard());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private void createBoard() {
        String boardTitle = boardTitleField.getText().toString();
        String creatorId = new Database().getId();
        ArrayList<String> users = new ArrayList<>();
        users.add(creatorId);

        Board board = new Board(null, boardTitle, creatorId, users);
        new Database().insertBoardData(board);
    }
}