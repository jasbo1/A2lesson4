package com.e.lesson_4;

import android.widget.Toast;

public class Toaster {
    public static void show(String message){
        Toast.makeText(App.context,message,Toast.LENGTH_SHORT).show();

    }
}
