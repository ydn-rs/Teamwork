package ru.yudin_r.teamwork.adapters;

import android.content.Context;
import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import ru.yudin_r.teamwork.R;
import ru.yudin_r.teamwork.models.User;
import ru.yudin_r.teamwork.tools.Database;

public class UserSwiper extends ItemTouchHelper.SimpleCallback {

    private final UserAdapter userAdapter;
    private final Context context;
    private final String id;

    public UserSwiper(Context context, UserAdapter userAdapter, String id) {
        super(0, ItemTouchHelper.LEFT);
        this.context = context;
        this.userAdapter = userAdapter;
        this.id = id;

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
        User user = userAdapter.getUser(position);
        String uId = user.getId();
        if (direction == ItemTouchHelper.LEFT) {
            new Database().getProjectData(id, project -> {
                project.getUsers().remove(uId);
                new Database().updateProjectData(id, project);
                userAdapter.deleteItem(position);
            });
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState,
                isCurrentlyActive)
                .addBackgroundColor(ContextCompat.getColor(context, R.color.red))
                .addActionIcon(R.drawable.ic_kick_user).create().decorate();
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
