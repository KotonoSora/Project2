package com.projectest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.projectest.CropView.reqHeight;
import static com.projectest.CropView.reqWidth;

public class CropActivity extends AppCompatActivity {

    private static final String TAG = null;
    ImageView compositeImageView;
    boolean crop;
    float angle=0;

    private String link_path_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            crop = extras.getBoolean("crop");
        }


        link_path_image = extras.getString( "linkpath" );

        DisplayMetrics dm = new DisplayMetrics();
        try {
            getWindowManager().getDefaultDisplay().getMetrics(dm);

        } catch (Exception ex) {
        }
        int w = (dm.widthPixels);
        int h = (dm.heightPixels);

        compositeImageView = (ImageView) findViewById(R.id.our_imageview);
        compositeImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(link_path_image,options);
        options.inSampleSize = scaleSampleSize( options );
        options.inJustDecodeBounds=false;
        Bitmap bmp = BitmapFactory.decodeFile( link_path_image  ,options );

        /// rotare ảnh nằm ngang thì chuyển lại ảnh thẳng
        //Bitmap bitmap_ = rotateImage(bitmap,angle);
        bmp = Bitmap.createBitmap( rotateImage( bmp ) );

        Bitmap resultingImage = Bitmap.createBitmap( w , h , bmp.getConfig() );

        Canvas canvas = new Canvas(resultingImage);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Path path = new Path();
        for (int i = 0; i < CropView.points.size(); i++) {
             path.lineTo( CropView.points.get(i).x, CropView.points.get(i).y);
        }
        canvas.drawPath(path, paint);

        if (crop) {
            paint.setXfermode(new PorterDuffXfermode( PorterDuff.Mode.SRC_IN));
        }
        else {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        }

        canvas.drawBitmap(bmp,0 ,0, paint);

        // Crop bitmap------------------------------------------
        Bitmap newBitmap = Bitmap.createBitmap(resultingImage, _getMax_Min_X(0), _getMax_Min_Y(0), _getMax_Min_X(1) - _getMax_Min_X(0), _getMax_Min_Y(1) - _getMax_Min_Y(0), null, false);
        compositeImageView.setImageBitmap(newBitmap);
        saveImageToExternalStorage(newBitmap);
    }
    private Bitmap rotateImage(Bitmap bmp) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(link_path_image);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, exif.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix(  );
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate( 90 );
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate( 180 );
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate( 270 );
                break;
            default:
        }
        return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
    }
    ///// ----- scaleSampleSize . Ep kich thuoc ảnh cho no nhỏ lại
    private int scaleSampleSize (BitmapFactory.Options options){
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while (halfWidth/inSampleSize > reqWidth || halfHeight/inSampleSize > reqHeight){
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
    ///// --- crop ảnh thu nhỏ không cho khoảng cách .
    ///--------- Xét get_Max_Min của X
    private int _getMax_Min_X(int m_m) {//mm=0 min; mm=1 max
        int max_min = CropView.points.get(0).x;

        if (m_m == 0) {
            for (int i = 0; i < CropView.points.size(); i++) {
                if (max_min >= CropView.points.get(i).x) {
                    max_min = CropView.points.get(i).x;
                }
            }

        } else {
            for (int i = 0; i < CropView.points.size(); i++) {
                if (max_min <= CropView.points.get(i).x) {
                    max_min = CropView.points.get(i).x;
                }
            }
        }
        return max_min;
    }
    ////-------------- Xét get_Max_Min của Y
    private int _getMax_Min_Y(int m_m) {//mm=0 min; mm=1 max
        int max_min = CropView.points.get(0).y;

        if (m_m == 0) {
            for (int i = 0; i < CropView.points.size(); i++) {
                if (max_min >= CropView.points.get(i).y) {
                    max_min = CropView.points.get(i).y;
                }
            }

        } else {
            for (int i = 0; i < CropView.points.size(); i++) {
                if (max_min <= CropView.points.get(i).y) {
                    max_min = CropView.points.get(i).y;
                }
            }
        }
        return max_min;
    }
    //////---- Save
        @Nullable
    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File( Environment.getExternalStorageDirectory() + "/IMG_CUT" );

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat( "ddMMyyyy_HHmm" ).format( new Date() ); // dat 1 bien ten la timeStamp
        File mediaFile;
        String mImageName = "IMG_" + timeStamp + ".png";
        mediaFile = new File( mediaStorageDir.getPath() + File.separator + mImageName );
        return mediaFile;
    }

    private void saveImageToExternalStorage(Bitmap finalBitmap) {
       // String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = getOutputMediaFile();
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 80, out);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
            startActivity( new Intent( getApplicationContext(), GridViewActivity.class ) );
    }

}
