package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class PinnedActivity extends AppCompatActivity {

    NotesAdapter noteAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinned);

        recyclerView = findViewById(R.id.recycler_view);

        setUpRecyclerView();

    }


    public void setUpRecyclerView() {
        Query query = ReferenceHelper.getCollectionReferenceForNotes()
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .whereEqualTo("pinned", true); // Filter for pinned items

        FirestoreRecyclerOptions<NoteModal> options = new FirestoreRecyclerOptions.Builder<NoteModal>()
                .setQuery(query, NoteModal.class)
                .build();

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        noteAdapter = new NotesAdapter(options, getApplicationContext());
        recyclerView.setAdapter(noteAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }


}