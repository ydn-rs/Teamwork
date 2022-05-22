package ru.yudin_r.teamwork.adapters;

import android.content.Context;
import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import ru.yudin_r.teamwork.R;


public class TaskSwiper extends ItemTouchHelper.SimpleCallback {
    private final TaskAdapter taskAdapter;
    private final Context context;

    public TaskSwiper(Context context, TaskAdapter taskAdapter) {
        super(0, ItemTouchHelper.LEFT);
        this.context = context;
        this.taskAdapter = taskAdapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getLayoutPosition();
        if (direction == ItemTouchHelper.LEFT) {
            taskAdapter.deleteItem(position);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addBackgroundColor(ContextCompat.getColor(context, R.color.red))
                .addActionIcon(R.drawable.ic_delete).create().decorate();
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
