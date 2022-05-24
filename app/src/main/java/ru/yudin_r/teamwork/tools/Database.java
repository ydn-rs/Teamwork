package ru.yudin_r.teamwork.tools;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import ru.yudin_r.teamwork.InviteUserActivity;
import ru.yudin_r.teamwork.MainActivity;
import ru.yudin_r.teamwork.ManageProjectActivity;
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
                unused -> {

                }
        ).addOnFailureListener(e -> {

        });
    }

    public void getUserData(String id, OnGetUser onGetUser) {
        db.collection(Constants.USERS).document(id).get().addOnSuccessListener(documentSnapshot -> {
            User user = documentSnapshot.toObject(User.class);
            onGetUser.getUser(user);
        }).addOnFailureListener(e -> {

        });
    }

    public void insertProjectData(Project project) {
        db.collection(Constants.PROJECTS).add(project).addOnSuccessListener(documentReference -> {
            String id = documentReference.getId();
            db.collection(Constants.PROJECTS).document(id).update("id", id);
        }).addOnFailureListener(e -> {
        });
    }

    public void getProjectList(MainActivity activity) {
        db.collection(Constants.PROJECTS).whereArrayContains("users", getId()).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Project> projects = new ArrayList<>();
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        DocumentSnapshot dS = queryDocumentSnapshots.getDocuments().get(i);
                        Project project = dS.toObject(Project.class);
                        projects.add(project);
                    }
                    activity.showProjectList(projects);
                });
    }

    public void getProjectData(String id, OnGetProject onGetProject) {
        db.collection(Constants.PROJECTS).document(id).get().addOnSuccessListener(documentSnapshot -> {
            Project project = documentSnapshot.toObject(Project.class);
            onGetProject.getProject(project);
        }).addOnFailureListener(e -> {

        });
    }

    public void insertTaskData(Task task) {
        db.collection(Constants.TASKS).add(task).addOnSuccessListener(documentReference -> {
            String id = documentReference.getId();
            db.collection(Constants.TASKS).document(id).update("id", id);
        }).addOnFailureListener(e -> {

        });
    }

    public void getTaskList(ProjectActivity activity, String projectId) {
        db.collection(Constants.TASKS).whereEqualTo("projectId", projectId)
                .orderBy("status", Query.Direction.ASCENDING).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                            ArrayList<Task> tasks = new ArrayList<>();
                            for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                                DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(i);
                                Task task = documentSnapshot.toObject(Task.class);
                                tasks.add(task);
                            }
                            activity.showTaskList(tasks);
                        }
                );
    }

    public void updateTaskStatus(String id, int status) {
        db.collection(Constants.TASKS).document(id).update("status", status)
                .addOnSuccessListener(unused -> {
                }).addOnFailureListener(e -> {
                });
    }

    public void deleteTask(String id) {
        db.collection(Constants.TASKS).document(id).delete();
    }

    public void deleteProject(String id) {
        db.collection(Constants.PROJECTS).document(id).delete().addOnSuccessListener(unused ->
                deleteAllTask(id)).addOnFailureListener(e -> {
        });
    }

    public void deleteAllTask(String id) {
        db.collection(Constants.TASKS).whereEqualTo("projectId", id).get().addOnSuccessListener(
                queryDocumentSnapshots -> {
                    ArrayList<String> taskIdList = new ArrayList<>();
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots
                                .getDocuments().get(i);
                        taskIdList.add(documentSnapshot.getId());
                    }
                    for (int i = 0; i < taskIdList.size(); i++) {
                        deleteTask(taskIdList.get(i));
                    }
                }
        ).addOnFailureListener(e -> {
        });
    }

    public void updateProjectData(String id, Project project) {
        db.collection(Constants.PROJECTS).document(id).set(project).addOnSuccessListener(unused -> {
                }
        ).addOnFailureListener(e -> {
        });
    }

    public void getUserList(ManageProjectActivity activity, ArrayList<String> users) {
        db.collection(Constants.USERS).whereIn("id", users).get().addOnSuccessListener(
                queryDocumentSnapshots -> {
                    ArrayList<User> userList = new ArrayList<>();
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(i);
                        User user = documentSnapshot.toObject(User.class);
                        userList.add(user);
                    }
                    activity.showUserList(userList);
                }
        ).addOnFailureListener(e -> {
        });
    }

    public void getUserDataByEmail(InviteUserActivity activity, String email) {
        db.collection(Constants.USERS).whereEqualTo("email", email).get().addOnSuccessListener(
                queryDocumentSnapshots -> {
                    User user = queryDocumentSnapshots.getDocuments().get(0).toObject(User.class);
                    activity.getId(user.getId());
                }
        );
    }

    public void checkEmail(String email, OnCheckEmail onCheckEmail) {
        db.collection(Constants.USERS).whereEqualTo("email", email).get().addOnSuccessListener(
                queryDocumentSnapshots -> onCheckEmail.getEmail(queryDocumentSnapshots.size() != 0)
        ).addOnFailureListener(e -> {
        });
    }

    public FirebaseFirestore getDb() {
        return db;
    }
}
