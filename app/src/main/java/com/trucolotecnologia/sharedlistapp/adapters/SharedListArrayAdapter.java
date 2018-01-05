package com.trucolotecnologia.sharedlistapp.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by tailan.trucolo on 05/01/2018.
 */

public class SharedListArrayAdapter extends ArrayAdapter<String> {

    public SharedListArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
    }
}
