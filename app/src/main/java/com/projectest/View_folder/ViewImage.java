package com.projectest.View_folder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.projectest.MainActivity;
import com.projectest.R;

public class ViewImage extends Activity {
	// Declare Variable
	TextView text;
	ImageView imageview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from view_image.xml
		setContentView(R.layout.view_image);

		// Retrieve data from MainActivity on GridView item click
		Intent i = getIntent();

		// Get the position
		int position = i.getExtras().getInt("position");

		// Get String arrays FilePathStrings
		String filepath = i.getStringExtra("filepath");

		// Locate the ImageView in view_image.xml
		imageview = (ImageView) findViewById(R.id.full_image_view);

		// Decode the filepath with BitmapFactory followed by the position
		Bitmap bmp = BitmapFactory.decodeFile(filepath);

		// Set the decoded bitmap into ImageView
		imageview.setImageBitmap(bmp);



		startActivity( new Intent( getApplicationContext(), MainActivity.class ) );



	}

}