package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.model.Document;

public class NotesActivity extends AppCompatActivity {

    EditText noteTitle, noteContent;
    MaterialButton saveNoteBtn;

    private String passTitle;
    private String passContent;
    private TextView newTitle;
    String docId;
    boolean isEditMode;

    Button delButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        noteTitle = findViewById(R.id.note_title);
        noteContent = findViewById(R.id.note_content);
        saveNoteBtn = findViewById(R.id.save_btn);
        newTitle = findViewById(R.id.note_mode);
        delButton = findViewById(R.id.button_del);

        passTitle = getIntent().getStringExtra("title");
        passContent = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        //receiving the data

        isEditMode = docId != null && !docId.isEmpty();

        if(isEditMode){
            delButton.setVisibility(View.VISIBLE);
            newTitle.setText("Edit Your Note");
            noteTitle.setText(passTitle);
            noteContent.setText(passContent);
        }



        delButton.setOnClickListener((view -> delNote()));

        saveNoteBtn.setOnClickListener( (view -> saveNote()));


    }

    public void delNote(){

        DocumentReference documentReference;


            documentReference = ReferenceHelper.getCollectionReferenceForNotes().document(docId);

  documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    ToastHelper.showToast(NotesActivity.this, "Note deleted successfully");
                    finish();
                }else{
                    ToastHelper.showToast(NotesActivity.this, "failed to delete note");
                }
            }
        });
    }



    public  void saveNote(){
        String title = noteTitle.getText().toString();
        String content = noteContent.getText().toString();

        if(title == null || title.isEmpty()){
            noteTitle.setError("Note must have title");
            return;
        }

        NoteModal note = new NoteModal();
        note.setNoteTitle(title);
        note.setNoteContent(content);
        note.setTimestamp(Timestamp.now());

        saveNoteToFirebase(note);


    }

    public void saveNoteToFirebase(NoteModal note){

        DocumentReference documentReference;

        if(isEditMode){
            //update note

            documentReference = ReferenceHelper.getCollectionReferenceForNotes().document(docId);
        }else{
            //create new note

            documentReference = ReferenceHelper.getCollectionReferenceForNotes().document();
        }




        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    ToastHelper.showToast(NotesActivity.this, "Note added successfully");
                    finish();
                }else{
                    ToastHelper.showToast(NotesActivity.this, "failed to add note");
                }
            }
        });
    }

}