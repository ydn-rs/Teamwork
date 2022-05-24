package ru.yudin_r.teamwork.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.yudin_r.teamwork.R;
import ru.yudin_r.teamwork.models.Project;
import ru.yudin_r.teamwork.tools.Database;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    private final ArrayList<Project> projects;
    private final OnClickListener onClickListener;

    public ProjectAdapter(ArrayList<Project> projects, OnClickListener onClickListener) {
        this.projects = projects;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_list_project, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Project project = projects.get(position);
        holder.projectTitle.setText(project.getTitle());
        holder.projectUsers.setText("Пользователей: " + project.getUsers().size());
        new Database().getUserData(project.getCreatorId(), user ->
                holder.projectCreator.setText("Создатель: " + user.getFullName()));
        holder.itemView.setOnClickListener(view -> {
            if (onClickListener != null) {
                onClickListener.onClick(getItemCount(), project);
            }
        });
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public Project getProject(int position) {
        return projects.get(position);
    }

    public void deleteItem(int position) {
        projects.remove(position);
        notifyItemRemoved(position);
    }

    public void deleteProject(int position) {
        Project project = projects.get(position);
        new Database().deleteProject(project.getId());
        deleteItem(position);
    }

    public interface OnClickListener {
        void onClick(int position, Project project);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView projectTitle, projectCreator, projectUsers;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            projectTitle = itemView.findViewById(R.id.projectTitle);
            projectCreator = itemView.findViewById(R.id.projectCreator);
            projectUsers = itemView.findViewById(R.id.projectUsers);
        }
    }
}
