package ru.yudin_r.teamwork.tools;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import ru.yudin_r.teamwork.BoardActivity;
import ru.yudin_r.teamwork.InviteUserActivity;
import ru.yudin_r.teamwork.MainActivity;
import ru.yudin_r.teamwork.ManageBoardActivity;
import ru.yudin_r.teamwork.models.Board;
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
            onGetUser.onGetUser(user);
        }).addOnFailureListener(e -> {

        });
    }

    public void insertBoardData(Board board) {
        db.collection(Constants.BOARDS).add(board).addOnSuccessListener(documentReference -> {
            String id = documentReference.getId();
            db.collection(Constants.BOARDS).document(id).update("id", id);
        }).addOnFailureListener(e -> {
        });
    }

    public void getBoardList(MainActivity activity) {
        db.collection(Constants.BOARDS).whereArrayContains(Constants.USERS, getId()).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Board> boardList = new ArrayList<>();
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        DocumentSnapshot dS = queryDocumentSnapshots.getDocuments().get(i);
                        Board board = dS.toObject(Board.class);
                        boardList.add(board);
                    }
                    activity.showBoardList(boardList);
                });
    }

    public void getBoardData(String id, OnGetBoard onGetBoard) {
        db.collection(Constants.BOARDS).document(id).get().addOnSuccessListener(documentSnapshot -> {
            Board board = documentSnapshot.toObject(Board.class);
            onGetBoard.OnGetBoard(board);
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

    public void getTaskList(BoardActivity activity, String id) {
        db.collection(Constants.TASKS).whereEqualTo("boardId", id).orderBy("status",
                Query.Direction.ASCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Task> taskList = new ArrayList<>();
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(i);
                        Task task = documentSnapshot.toObject(Task.class);
                        taskList.add(task);
                    }
                    activity.showTaskList(taskList);
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

    public void deleteBoard(String id) {
        db.collection(Constants.BOARDS).document(id).delete().addOnSuccessListener(unused ->
                deleteAllTask(id)).addOnFailureListener(e -> {
        });
    }

    public void deleteAllTask(String id) {
        db.collection(Constants.TASKS).whereEqualTo("boardId", id).get().addOnSuccessListener(
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

    public void updateBoardData(String id, Board board) {
        db.collection(Constants.BOARDS).document(id).set(board).addOnSuccessListener(unused -> {
                }
        ).addOnFailureListener(e -> {
        });
    }

    public void getUserList(ManageBoardActivity activity, ArrayList<String> users) {
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
                queryDocumentSnapshots -> {
                    onCheckEmail.OnGetEmail(queryDocumentSnapshots.size() != 0);
                }
        ).addOnFailureListener(e -> {
        });
    }

    public FirebaseFirestore getDb() {
        return db;
    }
}
