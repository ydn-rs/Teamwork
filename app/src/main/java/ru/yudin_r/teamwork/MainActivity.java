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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;

import java.util.ArrayList;

import ru.yudin_r.teamwork.adapters.ProjectAdapter;
import ru.yudin_r.teamwork.adapters.ProjectSwiper;
import ru.yudin_r.teamwork.models.Project;
import ru.yudin_r.teamwork.tools.Constants;
import ru.yudin_r.teamwork.tools.Database;

public class MainActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;
    private RecyclerView projectRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        FloatingActionButton createProjectFAB = findViewById(R.id.createProjectFAB);
        projectRv = findViewById(R.id.projectRv);
        initFun();

        createProjectFAB.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CreateProjectActivity.class)));
        eventListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top_main_app_bar, menu);
        return true;
    }

    private void menuOnClickListener() {
        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.logOut) {
                logOut();
                return true;
            } else {
                return false;
            }
        });
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this, EmailActivity.class));
        finish();
    }

    private void setTitle() {
        new Database().getUserData(new Database().getId(), user ->
                topAppBar.setTitle("Привет, " + user.getFirstName()));
    }

    public void showProjectList(ArrayList<Project> projects) {
        projectRv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        ProjectAdapter projectAdapter = new ProjectAdapter(projects, (position, project) -> {
            Intent intent = new Intent(MainActivity.this, ProjectActivity.class);
            intent.putExtra("projectId", project.getId());
            startActivity(intent);
        });
        projectRv.setAdapter(projectAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ProjectSwiper(MainActivity.this, projectAdapter));
        itemTouchHelper.attachToRecyclerView(projectRv);
    }

    private void initFun() {
        menuOnClickListener();
        setTitle();
        new Database().getProjectList(MainActivity.this);
    }

    private void eventListener() {
        new Database().getDb().collection(Constants.PROJECTS).whereArrayContains("users",
                new Database().getId()).addSnapshotListener((value, error) -> {
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED ||
                        documentChange.getType() == DocumentChange.Type.REMOVED ||
                        documentChange.getType() == DocumentChange.Type.MODIFIED) {
                    new Database().getProjectList(MainActivity.this);
                }
            }
        });
    }
}