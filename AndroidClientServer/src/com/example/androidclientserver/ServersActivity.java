package com.example.androidclientserver;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.zxing.client.android.CaptureActivity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ServersActivity extends Activity implements OnItemClickListener {

	static final int REQUEST_IMAGE_CAPTURE = 1;
	private String mCurrentPhotoPath;
	private String rawQRInfo = "No servers scanned !!!";
	private ImageView mImageView ;
	private ImageButton btnQR;
	private ImageButton btnRefresh;
	private CountriesDbAdapter dbHelper;
	private SimpleCursorAdapter dataAdapter;
	private ListView listView;



	public static final Integer[] images = { R.drawable.android60x60,
		R.drawable.android60x60, R.drawable.android60x60, R.drawable.android60x60 };


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_servers);

		btnQR = (ImageButton) findViewById(R.id.btnQR);
		btnRefresh = (ImageButton) findViewById(R.id.btnRefresh);

		//DATABASE STUFF
		dbHelper = new CountriesDbAdapter(this);
		dbHelper.open();

		//Clean all data
		dbHelper.deleteAllCountries();
		//Add some data
		dbHelper.insertSomeCountries();

		//Generate ListView from SQLite Database
		displayListView();


		/**
		 *bUTTON CLICK EVENt for starting camera
		 * */
		btnQR.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {				

				//dispatchTakePictureIntent();
				Intent intent = new Intent(getApplicationContext(),CaptureActivity.class);
				intent.setAction("com.google.zxing.client.android.SCAN");

				//				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
				//				intent.setPackage("com.example.androidclientserver");
				// this stops saving ur barcode in barcode scanner app's history
				intent.putExtra("SAVE_HISTORY", false);
				startActivityForResult(intent, 0);


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


		Toast toast = Toast.makeText(getApplicationContext(),
				rawQRInfo,
				Toast.LENGTH_LONG);
		toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
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
		//		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
		//			Bundle extras = data.getExtras();
		//			setPic();
		//		}

		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String contents = data.getStringExtra("SCAN_RESULT");

				this.rawQRInfo = data.getStringExtra("SCAN_RESULT");
				Toast toast = Toast.makeText(getApplicationContext(),
						rawQRInfo,
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
				toast.show();
				Log.d("QR-TAG", "contents: " + contents);
			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
				Log.d("QR-TAG", "RESULT_CANCELED");
			}
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

	private void displayListView() {

		Cursor cursor = dbHelper.fetchAllCountries();

		// The desired columns to be bound
		String[] columns = new String[] {
				CountriesDbAdapter.KEY_CODE,
				CountriesDbAdapter.KEY_NAME,
				CountriesDbAdapter.KEY_CONTINENT,
				CountriesDbAdapter.KEY_REGION
		};

		// the XML defined views which the data will be bound to
		int[] to = new int[] { 
				R.id.code,
				R.id.name,
				R.id.continent,
				R.id.region,
		};

		// create the adapter using the cursor pointing to the desired data 
		//as well as the layout information
		dataAdapter = new SimpleCursorAdapter(
				this, R.layout.country_info, 
				cursor, 
				columns, 
				to,
				0);

		  ListView listView = (ListView) findViewById(R.id.listView1);
		  // Assign adapter to ListView
		  listView.setAdapter(dataAdapter);
		 
		 
		  listView.setOnItemClickListener(new OnItemClickListener() {
		   @Override
		   public void onItemClick(AdapterView<?> listView, View view, 
		     int position, long id) {
		   // Get the cursor, positioned to the corresponding row in the result set
		   Cursor cursor = (Cursor) listView.getItemAtPosition(position);
		 
		   // Get the state's capital from this row in the database.
		   String countryCode = 
		    cursor.getString(cursor.getColumnIndexOrThrow("code"));
		   Toast.makeText(getApplicationContext(),
		     countryCode, Toast.LENGTH_SHORT).show();
		 
		   }
		  });
		 
		  EditText myFilter = (EditText) findViewById(R.id.myFilter);
		  myFilter.addTextChangedListener(new TextWatcher() {
		 
		   public void afterTextChanged(Editable s) {
		   }
		 
		   public void beforeTextChanged(CharSequence s, int start, 
		     int count, int after) {
		   }
		 
		   public void onTextChanged(CharSequence s, int start, 
		     int before, int count) {
		    dataAdapter.getFilter().filter(s.toString());
		   }
		  });
		   
		  dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
		         public Cursor runQuery(CharSequence constraint) {
		             return dbHelper.fetchCountriesByName(constraint.toString());
		         }
		     });
		 
		 }
		}