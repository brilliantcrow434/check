package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    NotesAdapter noteAdapter;

    FloatingActionButton addNoteBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn;


    Button pinBtn, folderBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNoteBtn = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.recycler_view);
        menuBtn = findViewById(R.id.menu_btn);

        pinBtn = findViewById(R.id.pin_btn);
        folderBtn = findViewById(R.id.folderBtn);

        folderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AllFolderActivity.class);
                startActivity(intent);
            }
        });

        pinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PinnedActivity.class);
                startActivity(intent);
            }
        });

        addNoteBtn.setOnClickListener((view -> startActivity(new Intent(MainActivity.this, NotesActivity.class))));

        menuBtn.setOnClickListener((view -> showMenu()));
        setUpRecyclerView();

    }

    public void showMenu(){

        PopupMenu popupMenu = new PopupMenu(MainActivity.this, menuBtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.getMenu().add("Settings");
        popupMenu.getMenu().add("Profile");
        popupMenu.getMenu().add("Create Folder");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle() == "Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                    return true;
                }
                else if(menuItem.getTitle() == "Settings"){
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    finish();
                    return true;
                }
                else if(menuItem.getTitle() == "Profile"){
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    finish();
                    return true;
                }
                else if(menuItem.getTitle() == "Create Folder"){
                    startActivity(new Intent(MainActivity.this, CreateFolder.class));
                    finish();
                    return true;
                }
                return false;
            }

        });

    }



    public void setUpRecyclerView(){
        Query query  = ReferenceHelper.getCollectionReferenceForNotes().orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<NoteModal> options = new FirestoreRecyclerOptions.Builder<NoteModal>()
                .setQuery(query,NoteModal.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NotesAdapter(options,this);
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
