package com.example.myinstagram.instagram;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.myinstagram.adapters.ImageAdapter;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class CameraFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;
    File storageDir;
    ImageAdapter imageAdapter;
    Uri mCapturedImageURI;

    public CameraFragment() {
        storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        storageDir = new File(storageDir, "MyInstagram");
        if (!storageDir.exists())
            storageDir.mkdir();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        super.onCreate(savedInstanceState);

        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();

            }
        });

        List<String> imageURLs = new ArrayList<>();
        String[] fileNames = storageDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (new File(dir, name).isDirectory())
                    return false;
                return name.toLowerCase().endsWith(".jpg");
            }
        });
        if(fileNames!=null && fileNames.length>0){
            Arrays.sort(fileNames);
            for (String bitmapFileName : fileNames) {
                imageURLs.add(storageDir.getPath() + "/" + bitmapFileName);
            }
        }
        imageAdapter = new ImageAdapter(getActivity(), imageURLs);
        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        gridview.setAdapter(imageAdapter);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            /*String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(mCapturedImageURI, projection, null, null, null);
            int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String capturedImageFilePath = cursor.getString(column_index_data);
            cursor.close();*/
            imageAdapter.addImageURL(mCurrentPhotoPath);
            imageAdapter.notifyDataSetChanged();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                System.out.println("Error occured while creating image file");
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
