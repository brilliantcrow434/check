package com.example.notesapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class NotesAdapter extends FirestoreRecyclerAdapter<NoteModal, NotesAdapter.MyViewHolder> {

    Context context;


    public NotesAdapter(@NonNull FirestoreRecyclerOptions<NoteModal> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull NoteModal model) {
        holder.noteTitle.setText(model.noteTitle);
        holder.noteContent.setText(model.noteContent);
        holder.timeStamp.setText(ConvertTime.timestampToString(model.timestamp));

        if (model.isPinned()) {
            holder.pinItem.setVisibility(View.VISIBLE);
        } else {
            holder.pinItem.setVisibility(View.GONE);
        }

        final NotesAdapter noteAdapter = this;


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.itemView);

                final String docId = getSnapshots().getSnapshot(position).getId();

                // Create menu items programmatically
                popupMenu.getMenu().add(Menu.NONE, 1, 1, "Pin/Unpin");
                popupMenu.getMenu().add(Menu.NONE, 2, 2, "Delete");
                popupMenu.getMenu().add(Menu.NONE, 3, 3, "Move");
                popupMenu.getMenu().add(Menu.NONE, 4, 4, "Share");

                // Set a click listener for the popup menu items
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == 1) {
                            // Handle Pin item click
                            DocumentReference documentReference = ReferenceHelper.getCollectionReferenceForNotes().document(docId);
                            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    boolean isPinned = documentSnapshot.getBoolean("pinned");
                                    boolean newPinnedStatus = !isPinned;
                                    documentReference.update("pinned", newPinnedStatus)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        if (newPinnedStatus) {
                                                            ToastHelper.showToast(context, "Note pinned successfully");
                                                        } else {
                                                            ToastHelper.showToast(context, "Note unpinned successfully");
                                                        }
                                                    } else {
                                                        ToastHelper.showToast(context, "Failed to update pinned status");
                                                    }
                                                }
                                            });
                                }
                            });
                        } else if (itemId == 2) {
                            // Handle Delete item click
                            DocumentReference documentReference = ReferenceHelper.getCollectionReferenceForNotes().document(docId);
                            documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        ToastHelper.showToast(context, "Note deleted successfully");
                                    } else {
                                        ToastHelper.showToast(context, "Failed to delete note");
                                    }
                                }
                            });
                        } else if (itemId == 4) {
                            // Handle Share item click
                            NoteModal note = getItem(position);

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_SUBJECT, note.getNoteTitle());
                            intent.putExtra(Intent.EXTRA_TEXT, note.getNoteContent());
                            intent.setType("text/plain");

                            if (intent.resolveActivity(context.getPackageManager()) != null) {
                                context.startActivity(intent);
                            }
                        }

                        return true;
                    }
                });

                // Show the popup menu
                popupMenu.show();

                return true;
            }
        });


        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, NotesActivity.class);
            intent.putExtra("title",model.noteTitle);
            intent.putExtra("content", model.noteContent);

            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);

            context.startActivity(intent);

        });


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout, parent, false);
        return new MyViewHolder(view);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView noteTitle, noteContent, timeStamp;
        ImageButton pinItem;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.my_noteTitle);
            noteContent = itemView.findViewById(R.id.my_noteContent);
            timeStamp = itemView.findViewById(R.id.time);
            pinItem = itemView.findViewById(R.id.pin_note);


        }
    }
}