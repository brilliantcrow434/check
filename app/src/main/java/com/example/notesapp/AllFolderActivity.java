package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class AllFolderActivity extends AppCompatActivity {

    FolderAdapter folderAdapter;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_folder);

        recyclerView = findViewById(R.id.recycler_view);

        setUpRecyclerView();


    }

    public void setUpRecyclerView() {
        Query query = ReferenceHelper.getCollectionReferenceForFolders().orderBy("timeStamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<FolderModal> options = new FirestoreRecyclerOptions.Builder<FolderModal>()
                .setQuery(query, FolderModal.class)
                .build();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        folderAdapter = new FolderAdapter(options, this);
        recyclerView.setAdapter(folderAdapter);
    }



    @Override
    protected void onStart() {
        super.onStart();
        folderAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        folderAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        folderAdapter.notifyDataSetChanged();
    }


}