package com.projectest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.projectest.View_folder.GridViewAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Admin on 10/17/2017.
 */

public class GridViewActivity extends Activity {
    // Declare variables
    private ArrayList<String> FilePathStrings;
  //  private String[] FileNameStrings;

    private File[] listFile;
    GridView grid;
    GridViewAdapter adapter;
    File file;

    String id_sticker = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_gridview);

        // Check for SD Card
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "Error! No SDCARD Found!", Toast.LENGTH_LONG)
                    .show();
        } else {
            // Locate the image folder in your SD Card
            file = new File(Environment.getExternalStorageDirectory() + "/IMG_CUT");
            // Create a new folder if no folder named SDImageTutorial exist
            file.mkdirs();
        }
        FilePathStrings = new ArrayList<String>(  );
        if (file.isDirectory()) {
            listFile = file.listFiles();

            for (int i = 0; i < listFile.length; i++) {
                // Get the path of the image file
                String fh = file + "/" + listFile[i].getName();
                FilePathStrings.add( fh );
            }
        }

        // Locate the GridView in gridview_main.xml
        grid = (GridView) findViewById(R.id.gridview);

        if (FilePathStrings.size() > 0) {
            // Pass String arrays to LazyAdapter Class
            adapter = new GridViewAdapter(GridViewActivity.this,R.layout.view_image ,FilePathStrings);
            // Set the LazyAdapter to the GridView
            grid.setAdapter(adapter);
        }

        // Capture gridview item click
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(GridViewActivity.this, MainActivity.class);
                i.putExtra("filepath", FilePathStrings.get( position ));
                startActivity(i);
                ////
//
            }

        });
    }



}
