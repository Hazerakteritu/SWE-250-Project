package com.example.eat_now.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.eat_now.models.FoodItem;
import com.example.eat_now.models.Restaurant;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RefactoredRestaurantFirestoreUtil {
    private static final String TAG = "RestaurantFirestoreUtil";


    public static void addSampleData(Context context) {
        RFSampleDataManager.addSampleData(context);
    }


}

