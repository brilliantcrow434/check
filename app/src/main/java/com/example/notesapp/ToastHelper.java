package com.example.notesapp;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {

    static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}
