package com.example.notesapp;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;

public class ConvertTime {

    static String timestampToString(Timestamp timestamp){
        return new SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate());
    }

}
