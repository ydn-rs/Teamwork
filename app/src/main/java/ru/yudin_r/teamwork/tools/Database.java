package ru.yudin_r.teamwork.tools;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import ru.yudin_r.teamwork.InviteUserActivity;
import ru.yudin_r.teamwork.MainActivity;
import ru.yudin_r.teamwork.ManageProjectActivity;
import ru.yudin_r.teamwork.PasswordActivity;
import ru.yudin_r.teamwork.ProjectActivity;
import ru.yudin_r.teamwork.models.Project;
import ru.yudin_r.teamwork.models.Task;
import ru.yudin_r.teamwork.models.User;

public class Database {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public String getId() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String id = null;
        if (firebaseUser != null) {
            id = firebaseUser.getUid();
        }
        return id;
    }

    public void insertUserData(User userData) {
        db.collection(Constants.USERS).document(getId()).set(userData).addOnSuccessListener(
                unused -> Log.d(Constants.DATABASE, "Пользователь успешно создан, uId: "
                        + userData.getId())
        ).addOnFailureListener(e -> Log.e(Constants.DATABASE, e.toString()));
    }

    public void getUserData(String id, OnGetUser onGetUser) {
        db.collection(Constants.USERS).document(id).get().addOnSuccessListener(dS -> {
            User user = dS.toObject(User.class);
            onGetUser.getUser(user);
        }).addOnFailureListener(e -> Log.e(Constants.DATABASE, e.toString()));
    }

    public void insertProjectData(Project project) {
        db.collection(Constants.PROJECTS).add(project).addOnSuccessListener(dR -> {
            String id = dR.getId();
            db.collection(Constants.PROJECTS).document(id).update("id", id);
        }).addOnFailureListener(e -> Log.e(Constants.DATABASE, e.toString()));
    }

    public void getProjectList(MainActivity activity) {
        db.collection(Constants.PROJECTS).whereArrayContains("users", getId()).get()
                .addOnSuccessListener(queryDS -> {
                    ArrayList<Project> projects = new ArrayList<>();
                    for (int i = 0; i < queryDS.size(); i++) {
                        DocumentSnapshot dS = queryDS.getDocuments().get(i);
                        Project project = dS.toObject(Project.class);
                        projects.add(project);
                    }
                    activity.showProjectList(projects);
                });
    }

    public void getProjectData(String id, OnGetProject onGetProject) {
        db.collection(Constants.PROJECTS).document(id).get().addOnSuccessListener(dS -> {
            Project project = dS.toObject(Project.class);
            onGetProject.getProject(project);
        }).addOnFailureListener(e -> Log.e(Constants.DATABASE, e.toString()));
    }

    public void insertTaskData(Task task) {
        db.collection(Constants.TASKS).add(task).addOnSuccessListener(dR -> {
            String id = dR.getId();
            db.collection(Constants.TASKS).document(id).update("id", id);
        }).addOnFailureListener(e -> Log.e(Constants.DATABASE, e.toString()));
    }

    public void getTaskList(ProjectActivity activity, String projectId) {
        db.collection(Constants.TASKS).whereEqualTo("projectId", projectId)
                .orderBy("status", Query.Direction.ASCENDING).get()
                .addOnSuccessListener(qDS -> {
                            ArrayList<Task> tasks = new ArrayList<>();
                            for (int i = 0; i < qDS.size(); i++) {
                                DocumentSnapshot dS = qDS.getDocuments().get(i);
                                Task task = dS.toObject(Task.class);
                                tasks.add(task);
                            }
                            activity.showTaskList(tasks);
                        }
                );
    }

    public void updateTaskStatus(String id, int status) {
        db.collection(Constants.TASKS).document(id).update("status", status)
                .addOnSuccessListener(unused ->
                        Log.d(Constants.DATABASE, "Данные задания " + id + " обновлены"))
                .addOnFailureListener(e -> Log.e(Constants.DATABASE, e.toString()));
    }

    public void deleteTask(String id) {
        db.collection(Constants.TASKS).document(id).delete();
    }

    public void deleteProject(String id) {
        db.collection(Constants.PROJECTS).document(id).delete().addOnSuccessListener(unused ->
                deleteAllTask(id)).addOnFailureListener(e -> Log.e(Constants.DATABASE, e.toString()));
    }

    public void deleteAllTask(String id) {
        db.collection(Constants.TASKS).whereEqualTo("projectId", id).get().addOnSuccessListener(
                qDS -> {
                    ArrayList<String> taskIdList = new ArrayList<>();
                    for (int i = 0; i < qDS.size(); i++) {
                        DocumentSnapshot dS = qDS.getDocuments().get(i);
                        taskIdList.add(dS.getId());
                    }
                    for (int i = 0; i < taskIdList.size(); i++) {
                        deleteTask(taskIdList.get(i));
                    }
                }
        ).addOnFailureListener(e -> Log.e(Constants.DATABASE, e.toString()));
    }

    public void updateProjectData(String id, Project project) {
        db.collection(Constants.PROJECTS).document(id).set(project).addOnSuccessListener(unused -> {
                }
        ).addOnFailureListener(e -> Log.e(Constants.DATABASE, e.toString()));
    }

    public void getUserList(ManageProjectActivity activity, ArrayList<String> users) {
        db.collection(Constants.USERS).whereIn("id", users).get().addOnSuccessListener(
                qDS -> {
                    ArrayList<User> userList = new ArrayList<>();
                    for (int i = 0; i < qDS.size(); i++) {
                        DocumentSnapshot dS = qDS.getDocuments().get(i);
                        User user = dS.toObject(User.class);
                        userList.add(user);
                    }
                    activity.showUserList(userList);
                }
        ).addOnFailureListener(e -> Log.e(Constants.DATABASE, e.toString()));
    }

    public void getUserDataByEmail(InviteUserActivity activity, String email) {
        db.collection(Constants.USERS).whereEqualTo("email", email).get().addOnSuccessListener(
                qDS -> {
                    User user = qDS.getDocuments().get(0).toObject(User.class);
                    activity.getId(user.getId());
                }
        );
    }

    public void checkEmail(String email, OnCheckEmail onCheckEmail) {
        db.collection(Constants.USERS).whereEqualTo("email", email).get().addOnSuccessListener(
                qDS -> onCheckEmail.getEmail(qDS.size() != 0)
        ).addOnFailureListener(e -> Log.e(Constants.DATABASE, e.toString()));
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public void showErrorMsg(Context context, Exception e) {
        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        Log.e(Constants.DATABASE, e.toString());
    }

    public void showMsg(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        Log.e(Constants.DATABASE, "uId: " + getId() + " Text: " + text);
    }
}
