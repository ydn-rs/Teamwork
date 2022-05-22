package ru.yudin_r.teamwork;

import androidx.annotation.Nullable;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import ru.yudin_r.teamwork.adapters.BoardAdapter;
import ru.yudin_r.teamwork.adapters.BoardSwiper;
import ru.yudin_r.teamwork.adapters.TaskSwiper;
import ru.yudin_r.teamwork.models.Board;
import ru.yudin_r.teamwork.models.User;
import ru.yudin_r.teamwork.tools.Constants;
import ru.yudin_r.teamwork.tools.Database;
import ru.yudin_r.teamwork.tools.OnGetUser;

public class MainActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;
    private RecyclerView boardRV;
    private FloatingActionButton createBoardFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        createBoardFAB = findViewById(R.id.createBoardFAB);
        boardRV = findViewById(R.id.boardList);
        initFun();

        createBoardFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateBoardActivity.class));
            }
        });

        eventListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top_main_app_bar, menu);
        return true;
    }

    private void menuOnClickListener() {
        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.logOut) {
                    logOut();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this, EmailActivity.class));
        finish();
    }

    private void setTitle() {
        new Database().getUserData(new Database().getId(), new OnGetUser() {
            @Override
            public void onGetUser(User user) {
               topAppBar.setTitle("Привет, " + user.getFirstName());
            }
        });
    }

    public void showBoardList(ArrayList<Board> boardList) {
        boardRV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        BoardAdapter boardAdapter = new BoardAdapter(boardList);
        boardRV.setAdapter(boardAdapter);
        boardAdapter.setOnClickListener(new BoardAdapter.OnClickListener() {
            @Override
            public void onClick(int position, Board board) {
                Intent intent = new Intent(MainActivity.this, BoardActivity.class);
                intent.putExtra("boardId", board.getId());
                startActivity(intent);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new BoardSwiper(MainActivity.this, boardAdapter));
        itemTouchHelper.attachToRecyclerView(boardRV);
    }

    private void initFun() {
        menuOnClickListener();
        setTitle();
        new Database().getBoardList(MainActivity.this);
    }

    private void eventListener() {
        new Database().getDb().collection(Constants.BOARDS).whereArrayContains("users",
                new Database().getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED ||
                            documentChange.getType() == DocumentChange.Type.REMOVED ||
                            documentChange.getType() == DocumentChange.Type.MODIFIED) {
                        new Database().getBoardList(MainActivity.this);
                    }
                }
            }
        });
    }
}