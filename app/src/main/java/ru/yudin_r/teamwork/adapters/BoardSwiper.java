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
import ru.yudin_r.teamwork.models.Board;
import ru.yudin_r.teamwork.tools.Database;

public class BoardSwiper extends ItemTouchHelper.SimpleCallback {

    private BoardAdapter boardAdapter;
    private Context context;

    public BoardSwiper(Context context, BoardAdapter boardAdapter) {
        super(0, ItemTouchHelper.LEFT);
        this.context = context;
        this.boardAdapter = boardAdapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getLayoutPosition();
        Board board = boardAdapter.getBoard(position);
        if (direction == ItemTouchHelper.LEFT) {
            if (board.getUsers().size() == 1) {
                boardAdapter.deleteBoard(position);
            } else {
                ArrayList<String> users = board.getUsers();
                users.remove(new Database().getId());
                board.setCreatorId(users.get(0));
                board.setUsers(users);
                new Database().updateBoardData(board.getId(), board);
                boardAdapter.deleteItem(position);
            }
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
