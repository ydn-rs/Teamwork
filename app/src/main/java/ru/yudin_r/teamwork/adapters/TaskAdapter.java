package ru.yudin_r.teamwork.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;

import ru.yudin_r.teamwork.R;
import ru.yudin_r.teamwork.models.Task;
import ru.yudin_r.teamwork.models.User;
import ru.yudin_r.teamwork.tools.Database;
import ru.yudin_r.teamwork.tools.OnGetUser;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private final ArrayList<Task> taskList;

    public TaskAdapter(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskCheckBox.setText(task.getText());
        new Database().getUserData(task.getCreatorId(), new OnGetUser() {
            @Override
            public void onGetUser(User user) {
                holder.taskCreator.setText(user.getFirstName() + " " + user.getSecondName());
            }
        });
        holder.taskCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            new Database().updateTaskStatus(task.getId(), 1);
                        } else {
                            new Database().updateTaskStatus(task.getId(), 0);
                        }
                    }
                }
        );
        holder.taskCheckBox.setChecked(converter(task.getStatus()));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    private boolean converter(int status) {
        return status != 0;
    }

    public void deleteItem(int position) {
        Task task = taskList.get(position);
        new Database().deleteTask(task.getId());
        taskList.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox taskCheckBox;
        Chip taskCreator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskCheckBox = itemView.findViewById(R.id.taskCheckBox);
            taskCreator = itemView.findViewById(R.id.creatorChip);
        }
    }
}
