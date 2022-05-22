package ru.yudin_r.teamwork.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.yudin_r.teamwork.R;
import ru.yudin_r.teamwork.models.Board;
import ru.yudin_r.teamwork.models.User;
import ru.yudin_r.teamwork.tools.Database;
import ru.yudin_r.teamwork.tools.OnGetUser;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {

    private ArrayList<Board> boardList;
    private OnClickListener onClickListener;

    public BoardAdapter(ArrayList<Board> boardList) {
        this.boardList = boardList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_list_board, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Board board = boardList.get(position);
        holder.boardTitle.setText(board.getTitle());
        holder.users.setText("Пользователей: " + board.getUsers().size());
        new Database().getUserData(board.getCreatorId(), new OnGetUser() {
            @Override
            public void onGetUser(User user) {
                holder.boardCreator.setText("Создатель: " + user.getFirstName() + " "
                        + user.getSecondName());
            }
        });
        holder.itemView.setOnClickListener(view -> {
            if (onClickListener != null) {
                onClickListener.onClick(getItemCount(), board);
            }
        });
    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public Board getBoard(int position) {
        Board board = boardList.get(position);
        return board;
    }

    public void deleteItem(int position) {
        boardList.remove(position);
        notifyItemRemoved(position);
    }

    public void deleteBoard(int position) {
        Board board = boardList.get(position);
        new Database().deleteBoard(board.getId());
        deleteItem(position);
    }

    public interface OnClickListener {
        void onClick(int position, Board board);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView boardTitle, users, boardCreator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            boardTitle = itemView.findViewById(R.id.boardTitle);
            users = itemView.findViewById(R.id.users);
            boardCreator = itemView.findViewById(R.id.boardCreator);
        }
    }
}
