package com.example.androidclientserver;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class ServersActivity extends Activity implements OnItemClickListener {

	static final int REQUEST_IMAGE_CAPTURE = 1;
	private String mCurrentPhotoPath;
	private ImageView mImageView ;
	private ImageButton btnQR;
	private ImageButton btnRefresh;

	public static final String[] titles = new String[] { "Strawberry",
		"Banana", "Orange", "Mixed" };

	public static final String[] descriptions = new String[] {
		"It is an aggregate accessory fruit",
		"It is the largest herbaceous flowering plant", "Citrus Fruit",
	"Mixed Fruits" };

	public static final Integer[] images = { R.drawable.android60x60,
		R.drawable.android60x60, R.drawable.android60x60, R.drawable.android60x60 };
	

	ListView listView;
	List<RowItem> rowItems;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_servers);
		
		btnQR = (ImageButton) findViewById(R.id.btnQR);
		btnRefresh = (ImageButton) findViewById(R.id.btnRefresh);
		
		/**
		 *bUTTON CLICK EVENt for starting camera
		 * */
		btnQR.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {				

				dispatchTakePictureIntent();

			}
		});
		
		/**
		 *REFRESH List Button
		 * */
		btnRefresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {				
				 Toast toast = Toast.makeText(getApplicationContext(),
			                "This will force List Update",
			                Toast.LENGTH_SHORT);
			        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
			        toast.show();

			}
		});
		
		rowItems = new ArrayList<RowItem>();
		for (int i = 0; i < titles.length; i++) {
			RowItem item = new RowItem(images[i], titles[i], descriptions[i]);
			rowItems.add(item);
		}

		
		listView = (ListView) findViewById(R.id.serversList);
		CustomBaseAdapter adapter = new CustomBaseAdapter(this, rowItems);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		 Toast toast = Toast.makeText(getApplicationContext(),
	                "Item " + (position + 1) + ": " + rowItems.get(position),
	                Toast.LENGTH_SHORT);
	        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
	        toast.show();
		

	}


	//Put this in listener
	private void dispatchTakePictureIntent() {

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				// Error occurred while creating the File

			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			setPic();


		}


	}


	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
				imageFileName,  /* prefix */
				".jpg",         /* suffix */
				storageDir      /* directory */
				);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}

	private void setPic() {
		// Get the dimensions of the View
		int targetW = mImageView.getWidth();
		int targetH = mImageView.getHeight();

		// Get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		// Determine how much to scale down the image
		int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		mImageView.setImageBitmap(bitmap);
		Toast toast = Toast.makeText(getApplicationContext(),
				"Height " ,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();

	}
}
