package com.example.teachcooking.util;

import android.widget.Toast;

import com.example.teachcooking.Myapplication;

/**
 * Created by Administrator on 2016/5/16.
 */
public class ShowToast {
    public static void ShowS(String s) {
        Toast.makeText(Myapplication.myapplication, s, Toast.LENGTH_SHORT);
    }
}
