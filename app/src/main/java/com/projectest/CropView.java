package com.projectest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.media.ExifInterface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CropView extends View implements View.OnTouchListener {
    private Paint paint;
    public static List<Point> points;
    int DIST = 2;
    boolean flgPathDraw = true;

    Point mfirstpoint = null;
    boolean bfirstpoint = false;

    Point mlastpoint = null;

    private  Context mContext;
    private Bitmap bitmap;
    private String _path;
    final static int reqHeight = 600;
    final static int reqWidth = 600;
    //float angle=0;
    // private boolean cancelable;

    public CropView(Context context, String _path) {
        super( context );


        this._path = _path;
        mContext = context;


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(_path,options);
        options.inSampleSize = scaleSampleSize( options );
        options.inJustDecodeBounds=false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        bitmap = BitmapFactory.decodeFile( _path , options );
        bitmap = Bitmap.createBitmap(rotateImage(bitmap));

        setFocusable( true );
        setFocusableInTouchMode( true );

        paint = new Paint( Paint.ANTI_ALIAS_FLAG );
        paint.setStyle( Paint.Style.STROKE );
        paint.setPathEffect( new DashPathEffect( new float[]{5, 10}, 0 ) );
        paint.setStrokeWidth( 6 ); //
        paint.setColor( Color.WHITE ); //

        this.setOnTouchListener( this );
        points = new ArrayList<Point>();

        bfirstpoint = false;
    }

    private Bitmap rotateImage(Bitmap bitmap) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface( _path );
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
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public CropView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setFocusable(true);
        setFocusableInTouchMode(true);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(Color.WHITE);

        this.setOnTouchListener(this);
        points = new ArrayList<Point>();
        bfirstpoint = false;

    }

    ///------- OnDraw
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
           // matrix.postScale(1f, 1f);
           // matrix.postRotate( 45 );
            canvas.drawBitmap(bitmap,0 ,0 , null);

            Path path = new Path();
            boolean first = true;

            for (int i = 0; i < points.size(); i += 2) {
                Point point = points.get( i );
                if (first) {
                    first = false;
                    path.moveTo( point.x, point.y );
                } else if (i < points.size() - 1) {
                    Point next = points.get( i + 1 );
                    path.quadTo( point.x, point.y, next.x, next.y );
                } else {
                    mlastpoint = points.get( i );
                    path.lineTo( point.x, point.y );
                }
            }
            canvas.drawPath( path, paint );
        }catch (Exception exp){
            Toast.makeText(mContext, "onDraw gets error: " + exp.getMessage() , Toast.LENGTH_SHORT ).show();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Point point = new Point();
        point.x = (int) motionEvent.getX();
        point.y = (int) motionEvent.getY();

        if (flgPathDraw) {

            if (bfirstpoint) {
                if (comparepoint( mfirstpoint, point )) {
                    points.add( mfirstpoint );
                    flgPathDraw = false;
                    showcropdialog();
                } else {
                    points.add( point );
                }
            } else {
                points.add( point );
            }

            if (!(bfirstpoint)) {

                mfirstpoint = point;
                bfirstpoint = true;
            }
        }

        invalidate();

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

            mlastpoint = point;
            if (flgPathDraw) {
                if (points.size() > 12) {
                    if (!comparepoint( mfirstpoint, mlastpoint )) {
                        flgPathDraw = false;
                        points.add( mfirstpoint );
                        showcropdialog();
                    }
                }
            }
        }
        return true;
    }

    ///------- comparepoint
    private boolean comparepoint(Point mfirstpoint, Point point) {
        int left_range_x = (int) (point.x - 3);
        int left_range_y = (int) (point.y - 3);

        int right_range_x = (int) (point.x + 3);
        int right_range_y = (int) (point.y + 3);

        if ((left_range_x < mfirstpoint.x && mfirstpoint.x < right_range_x)
                && (left_range_y < mfirstpoint.y && mfirstpoint.y < right_range_y)) {
            if (points.size() < 10) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public void fillinPartofPath() {
        Point point = new Point();
        point.x = points.get(0).x;
        point.y = points.get(0).y;

        points.add(point);
        invalidate();
    }

    public void resetView() {
        points.clear();
        paint.setColor( Color.RED );
        paint.setStyle( Paint.Style.STROKE );
        flgPathDraw = true;
        invalidate();
    }
    ///// --- scaleSampleSize
    private int scaleSampleSize (BitmapFactory.Options options){
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1    ;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while (halfWidth/inSampleSize > reqWidth || halfHeight/inSampleSize > reqHeight){
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    ///------

    ////------ showcropdialog
    private void showcropdialog() {
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent;
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        intent = new Intent( mContext, CropActivity.class );
                        intent.putExtra( "crop", true );
                       intent.putExtra( "linkpath", _path );
                        //intent.putExtra( "linkpath", saveImageToExternalStorage( getCircularBitmap( bitmap ) ) );
                        mContext.startActivity( intent );
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        intent = new Intent( mContext, CropActivity.class );
                        intent.putExtra( "crop", false );
                        intent.putExtra( "linkpath", _path );
                        //mContext.startActivity( intent );
                        bfirstpoint = false;

                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder( mContext );
        builder.setTitle( "Save Cut Photos" )
                .setMessage( "Do you want to save crop image? " )
                .setPositiveButton( "Crop", dialogClickListener )
                .setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resetView();
                        dialogInterface.cancel();
                    }
                } ).show()
                .setCancelable( false );
                clearAnimation();
    }
}