package org.team5401.froscoutingapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static android.os.Environment.getExternalStorageDirectory;
import static java.lang.String.valueOf;

public class Export extends AppCompatActivity {

    Map<String, ?> scoutingNamesValues, scoutingDataValues;
    String match, robot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        System.out.println(getExternalStorageDirectory());

        SharedPreferences mr = getSharedPreferences("matchRobot", MODE_PRIVATE);
        match = mr.getString("match", "");
        robot = mr.getString("robot", "");

        SharedPreferences scoutingNames = getSharedPreferences("scoutingNames", MODE_PRIVATE);
        scoutingNamesValues = scoutingNames.getAll();
        SharedPreferences scoutingData = getSharedPreferences("scoutingData", MODE_PRIVATE);
        scoutingDataValues = scoutingData.getAll();
    }

    //back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //NavUtils.navigateUpFromSameTask(this);
            finish();
            return true;
        }
        //return super.OnOptionsItemSelected(item);
        return false;
    }

    public void saveData (View view) throws IOException {
        String folderName = "/FROScoutingApp/ScoutingData";
        File folder = new File(getExternalStorageDirectory(), folderName);
        if (!folder.exists()) {
            folder.mkdirs();
            System.out.println("Created folder (mkdirs)");
        }

        String filepath = getExternalStorageDirectory() + folderName + "/";
        //String fileName = "Match-" + match + ";Robot-" + robot + ".txt";
        String fileName = "data.txt";
        File myFile = new File(filepath + fileName);

        /*
        boolean fileAlreadyExists = true;
        int counter = 0;
        while (fileAlreadyExists) {
            myFile = new File(filepath + fileName);
            if (myFile.exists() && myFile.isFile()) {
                counter++;
                fileName = "Match-" + match + ";Robot-" + robot + "(" + counter + ").txt";
            } else {
                fileAlreadyExists = false;
            }
        }
        */
        System.out.println("writing to file named " + fileName);
        System.out.println("filepath: " + filepath);

        //FileWriter writer = new FileWriter(myFile, true);
        BufferedWriter writer = new BufferedWriter(new FileWriter(myFile, true));
        BufferedReader reader = new BufferedReader(new FileReader(myFile));
        if (reader.readLine() != null) {
            writer.write(System.lineSeparator());
            System.out.println("new line");
        }
        writer.write((match + ", " + robot + ", "));
        for (Map.Entry<String, ?> entry : scoutingDataValues.entrySet()) {
            String data = entry.getValue().toString();
            writer.write(data);
            writer.write(", ");
        }
        writer.close();
    }

    public void saveLayout (View view) throws IOException {
        String folderName = "/FROScoutingApp/InputLayouts";
        File folder = new File(getExternalStorageDirectory(), folderName);
        if (!folder.exists()) {
            folder.mkdirs();
            System.out.println("Created folder (mkdirs)");
        }

        EditText layoutNameRaw = (EditText) findViewById(R.id.layout_name);
        String layoutName = layoutNameRaw.getText().toString();

        String filepath = getExternalStorageDirectory() + folderName + "/";
        String fileName = layoutName + ".layout";
        File myFile = new File(filepath + fileName);

        boolean fileAlreadyExists = true;
        int counter = 0;
        while (fileAlreadyExists) {
            myFile = new File(filepath + fileName);
            if (myFile.exists() && myFile.isFile()) {
                counter++;
                fileName = layoutName + "(" + counter + ").layout";
            } else {
                fileAlreadyExists = false;
            }
        }
        System.out.println("created file named " + fileName);

        ArrayList<InputData> views = new ArrayList<InputData>();
        SharedPreferences scoutingInput = getSharedPreferences("scoutingInput", MODE_PRIVATE);
        Gson gson = new Gson();
        for (int i = 0; i < scoutingInput.getAll().size(); i++) {
            String json = scoutingInput.getString(valueOf(i), "");
            InputData data = gson.fromJson(json, InputData.class);
            views.add(data);
        }

        FileWriter writer = new FileWriter(myFile);
        for (int i = 0; i < views.size(); i++) {
            String name = views.get(i).getName();
            String type = views.get(i).getType();
            writer.write(name + "," + type + ",");
        }
        writer.close();
    }
}
