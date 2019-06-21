package com.example.landmarkmap;

import android.content.Context;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

public class StorageManager {

    static void saveToFile(Context context, String filename, HashMap<String, MarkerOptions> markers) {
        Gson gson = new Gson();
        String json = gson.toJson(markers);

        try {
            FileOutputStream outputStream = context.openFileOutput(filename, context.MODE_PRIVATE);
            outputStream.write(json.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static HashMap<String, MarkerOptions> loadFromFile(Context context, String filename) {
        Gson gson = new Gson();

        try {
            FileInputStream fileInputStream = context.openFileInput(filename);
            String jsonFromFile = IOUtils.toString(fileInputStream, (String)null);
            HashMap<String, MarkerOptions> markers = gson.fromJson(jsonFromFile, new TypeToken<HashMap<String, MarkerOptions>>(){}.getType());
            return markers;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}
