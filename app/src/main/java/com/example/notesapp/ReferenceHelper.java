package com.example.notesapp;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ReferenceHelper {

    static CollectionReference getCollectionReferenceForNotes() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        return FirebaseFirestore.getInstance().collection("notes").document(currentUser.getUid()).collection("my_notes");


    }

    static CollectionReference getCollectionReferenceForFolders() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String uid = currentUser != null ? currentUser.getUid() : "null";
        Log.d("TAG", "Current User UID: " + uid);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("folders").document(uid).collection("folders");

        // Print the path of the CollectionReference
        Log.d("TAG", "Collection Path: " + collectionRef.getPath());

        return collectionRef;
    }


}
