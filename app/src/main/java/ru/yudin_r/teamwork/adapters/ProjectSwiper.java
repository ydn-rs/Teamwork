package ru.yudin_r.teamwork.adapters;

import android.content.Context;
import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import ru.yudin_r.teamwork.R;
import ru.yudin_r.teamwork.models.Project;
import ru.yudin_r.teamwork.tools.Database;

public class ProjectSwiper extends ItemTouchHelper.SimpleCallback {

    private final Context context;
    private final ProjectAdapter projectAdapter;

    public ProjectSwiper(Context context, ProjectAdapter projectAdapter) {
        super(0, ItemTouchHelper.LEFT);
        this.context = context;
        this.projectAdapter = projectAdapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getLayoutPosition();
        Project project = projectAdapter.getProject(position);
        if (direction == ItemTouchHelper.LEFT) {
            if (project.getUsers().size() == 1) {
                projectAdapter.deleteProject(position);
            } else {
                ArrayList<String> users = project.getUsers();
                users.remove(new Database().getId());
                project.setCreatorId(users.get(0));
                project.setUsers(users);
                new Database().updateProjectData(project.getId(), project);
                projectAdapter.deleteItem(position);
            }
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState,
                isCurrentlyActive)
                .addBackgroundColor(ContextCompat.getColor(context, R.color.red))
                .addActionIcon(R.drawable.ic_delete).create().decorate();
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
