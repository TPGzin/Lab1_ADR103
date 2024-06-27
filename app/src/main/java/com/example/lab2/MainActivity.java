package com.example.lab2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FirebaseFirestore db;
    private List<Obj> objList;
    private ObjAdapter objAdapter;
    private ListView listViewObjs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        listViewObjs = findViewById(R.id.listViewObjs);
        objList = new ArrayList<>();
        objAdapter = new ObjAdapter(this, objList, new ObjAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(Obj obj) {
                deleteObj(obj.getId());
            }
        }, new ObjAdapter.OnEditClickListener() {
            @Override
            public void onEditClick(Obj obj) {
                showDialog(obj);
            }
        });
        listViewObjs.setAdapter(objAdapter);

        Button btnAddObj = findViewById(R.id.btnAddObj);
        btnAddObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(null);
            }
        });

        readObjs();
    }

    private void showDialog(final Obj obj) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_update, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = dialogView.findViewById(R.id.editTextName);
        final EditText editTextAge = dialogView.findViewById(R.id.editTextAge);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        if (obj != null) {
            editTextName.setText(obj.getName());
            editTextAge.setText(String.valueOf(obj.getAge()));
        }

        AlertDialog alertDialog = dialogBuilder.create();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                int age = Integer.parseInt(editTextAge.getText().toString());

                if (obj == null) {
                    createObj(new Obj(name, age));
                } else {
                    updateObj(obj.getId(), name, age);
                }

                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void createObj(Obj obj) {
        db.collection("objs")
                .add(obj)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        readObjs();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void readObjs() {
        db.collection("objs")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        objList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Obj obj = document.toObject(Obj.class);
                            obj.setId(document.getId());
                            objList.add(obj);
                        }
                        objAdapter.notifyDataSetChanged();
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    private void updateObj(String documentId, String newName, int newAge) {
        DocumentReference objRef = db.collection("objs").document(documentId);
        objRef.update("name", newName, "age", newAge)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                    readObjs();
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    private void deleteObj(String documentId) {
        db.collection("objs").document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    readObjs();
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));
    }
}
