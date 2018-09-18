package com.projectest;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.projectest.ListView_typeface.Adapter_Listview_TypeFace;
import com.projectest.ListView_typeface.Item_listview_Typeface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import yuku.ambilwarna.AmbilWarnaDialog;

import static com.projectest.Common_str._getList_typeface;
import static com.projectest.R.id.imageButtonColor;
import static com.projectest.R.id.imageButtonText;
import static com.projectest.R.id.relativeLayout;

public class MainActivity extends AppCompatActivity {

    ImageButton btn_imgstyle;
    //ImageButton btn_imgshow;
    ImageButton btn_imgxoa;
    ImageButton btn_imgslfile;
    ImageButton btn_imgshare;
    ImageButton btn_text;
    ImageButton add_btn_text;
    ImageButton btn_color;
    ImageButton btn_fonts;
    ImageButton btn_cut;
    ImageButton btn_resume;

    // Text
    EditText eText;
    int DefaultColor ;

    // Font
    ListView listview;

    // camera and select gallery
    ImageButton btn_imgcamera;
    static final int CAM_REQUEST = 1;
    static final int SELECT_FILE = 0;
    static final int SELECT_PICTURE = 2;
    // màng hình hiện camera
    ImageView imgAnh;

    // danh sach ảnh
    GridView gvHinhAnh;
    ArrayList<HinhAnh> arrayImage;
    HinhAnhApdapter adapter;

    int int_id_img; // hien anh style

    RelativeLayout re_pearent;

    //list ID image add new
    ArrayList<Integer> _list_id_image;
    ArrayList<EditText> _them_text_color;

    //choice image now
    int _choice_image_now = 0;
    Integer _choice_text_color = 1;
    // Save
    String id_sticker = "";

    StickerImageView _stk_zz; // tao 1 biến Xóa mới để tý thay thế cho StickerImageView đầu tiên

    StickerTextView  tv_color ; // tao 1 biến Xóa mới để tý thay thế cho StickerTextView đầu tiên

    StickerTextView  tv_color_font;

    String _path; // ve anh?
    CropView cropview = null;
    String path_camera = "sdcard/camera/image_.png";
    ////ads
//    StartAppAd startAppAd;
//
//    private AdView adView;
//    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        // Save and //RelativeLayout cha (re_ca)
        final RelativeLayout re_ca = (RelativeLayout) findViewById( R.id.re_image );

        // RelativeLayout Con cua re_ca
        final RelativeLayout con_re_ca = (RelativeLayout) findViewById( relativeLayout ) ;

        // RelativeLayout này con_con_re_ca con cua con_re_ca
        final RelativeLayout con_con_re_ca = (RelativeLayout) findViewById( R.id.con_con_re_ca ) ;

        // RelativeLayout hien button show text and color
        final RelativeLayout edittext_color = (RelativeLayout) findViewById( R.id.edittext_color ) ;

        //   LinearLayout để hiện thanh công cụ các 5 chức năng control_view
        final LinearLayout _Horizontal_controView = (LinearLayout)findViewById( R.id.linearLayout2);

        imgAnh = (ImageView) findViewById( R.id.imageViewAnh ); // mang hinh camera

        // style
        btn_imgstyle = (ImageButton) findViewById( R.id.imageButton ); // nut chon style anh

        //// ------ LAY ANH TU DUONG TUYET DOI
        Intent i = getIntent();
        // Get String arrays FilePathStrings
        try {
            String filepath = i.getStringExtra( "filepath" );

            if(filepath.length()>0) {
                // Locate the ImageView in view_image.xml
                final StickerImageView imageview_ = new StickerImageView( this );
                // Decode the filepath with BitmapFactory followed by the position
                Bitmap bmp = BitmapFactory.decodeFile( filepath );
                // Set the decoded bitmap into ImageView
                imageview_.setImageBitmap( bmp );

                con_re_ca.addView( imageview_ );

                imageview_.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            imageview_.showTouchImage();
                            _stk_zz = imageview_;
                        } catch (Exception ex) {
                            Toast.makeText( getApplicationContext(), "You must select a photo first", Toast.LENGTH_SHORT ).show();
                        }
                    }
                } );

            }
        }catch (Exception ex){

        }
        /////----- kET THUC LAY ANH TU DUONG DAN TUYET DOI

        AnhXa(); // hiện danh sách Arraylist

        btn_imgstyle.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder popDialog = new AlertDialog.Builder( MainActivity.this );

                final LayoutInflater inflater = (LayoutInflater) getSystemService( LAYOUT_INFLATER_SERVICE );

                final View Viewlayout = inflater.inflate( R.layout.l_pop_layer_list, (ViewGroup) findViewById( R.id.l_pop_layer_list ) );
                // hien ds list view

                popDialog.setView( Viewlayout );

                popDialog.create();
                final AlertDialog dg = popDialog.show();

                GridView gr = (GridView) Viewlayout.findViewById( R.id.gr );

                if (arrayImage.size() > 0) {
                    adapter = new HinhAnhApdapter( getApplicationContext(), android.R.layout.activity_list_item, arrayImage );
                    gr.setAdapter( adapter );

                }
                gr.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) { // ham lay anh tu onruslt ve 

                        Toast.makeText( MainActivity.this, arrayImage.get( position ).getTen(), Toast.LENGTH_SHORT ).show();
                        int_id_img = arrayImage.get( position ).getHinh();
                        final StickerImageView iv_sticker = new StickerImageView( MainActivity.this );
                        iv_sticker.setOwnerId( id_sticker );
                        iv_sticker.setImageDrawable( getResources().getDrawable( int_id_img ) ); // lay stle cho vo trong stickview
                        con_re_ca.addView( iv_sticker );
                        iv_sticker.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    iv_sticker.showTouchImage();
                                    Toast.makeText( MainActivity.this, "Image " + iv_sticker.getOwnerId(), Toast.LENGTH_SHORT ).show();
                                    _stk_zz = iv_sticker;
                                } catch (Exception ex) {
                                    Toast.makeText( getApplicationContext(), "You must select a photo first", Toast.LENGTH_SHORT ).show();
                                }
                            }
                        } );
//
                        dg.dismiss();
                    }
                } );


            }

        } );
        // text
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.content_main, null);
        final TextView textOut = (TextView)addView.findViewById(R.id.textout);
        eText = (EditText) findViewById(R.id.editText);
        textOut.setText(eText.getText().toString());
        tv_color = new StickerTextView(this);
        tv_color_font = new StickerTextView(this);
        textOut.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tv_color_font.setText(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        // Color
        DefaultColor = ContextCompat.getColor(MainActivity.this, R.color.colorPrimary);
        btn_text = (ImageButton) findViewById( imageButtonText);
        add_btn_text = (ImageButton) findViewById( R.id.imageButtonAddText ); // them text
        add_btn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final StickerTextView tv_sticker = new StickerTextView(MainActivity.this);
                tv_sticker.setOwnerId( id_sticker );
                String text = textOut.getText().toString();
                String newText = eText.getText().toString()+text;
                tv_sticker.setText(newText);
                tv_color_font = tv_sticker;
                con_con_re_ca.addView(tv_sticker);
                tv_sticker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tv_sticker.showTouchText();
                        Toast.makeText(MainActivity.this," "+ tv_sticker.getOwnerId(), Toast.LENGTH_SHORT).show();
                        tv_color = tv_sticker;
                    }
                });
                hideSoftKeyBoardOnTabClicked(view);
                eText.setVisibility( View.INVISIBLE );


            }
        });
        btn_text.setOnClickListener(new View.OnClickListener() { // show text ra
            @Override
            public void onClick(View view) {
                edittext_color.setVisibility( View.VISIBLE  );
                eText.setVisibility(View.VISIBLE);
            }
        });

        //--- Color
        btn_color = (ImageButton) findViewById(imageButtonColor);
        btn_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenColorPickerDialog(false);
            }
        });

        // Fonts
        btn_fonts = (ImageButton) findViewById(R.id.imageButtonFonts);
        btn_fonts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder popDialog = new AlertDialog.Builder( MainActivity.this );

                final LayoutInflater inflater = (LayoutInflater) getSystemService( LAYOUT_INFLATER_SERVICE );

                final View Viewlayout = inflater.inflate( R.layout.list_font_view, (ListView) findViewById( R.id.list_font_view ) );
                // hien ds list view

                popDialog.setView( Viewlayout );
                popDialog.create();
                final AlertDialog dg = popDialog.show();
                /// ds ArrayList
                ArrayList<Item_listview_Typeface> _list_font = new ArrayList<Item_listview_Typeface>();
                _list_font = _getList_typeface(getApplicationContext());
                //// lay ra tu ArrayAdapter co ten la Item_listview_Typeface
                ArrayAdapter<Item_listview_Typeface> adapter_list = new ArrayAdapter<Item_listview_Typeface>(MainActivity.this, android.R.layout.activity_list_item ,_list_font );
                // listview Font text
                listview = (ListView) Viewlayout.findViewById(R.id.list_view);
                listview.setAdapter( adapter_list );
                if(_list_font.size() >0){
                    listview.setAdapter( new Adapter_Listview_TypeFace( MainActivity.this ,_list_font ) );
                }

                final  ArrayList<Item_listview_Typeface> final_list_font = _list_font;
                listview.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        tv_color_font.setTypeface( final_list_font.get( i ).getType());
                        dg.dismiss();
                    }
                } );

        }
        });

        // --- Show
//        btn_imgshow = (ImageButton) findViewById( R.id.imageButtonShow );
//        btn_imgshow.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                try {
//                    con_re_ca.removeAllViews();
//                    goToGallery();
//                    add_btn_text.setVisibility( View.VISIBLE );
//                    btn_color.setVisibility(View.VISIBLE);
//                } catch (Exception ex) {
//
//                }
//            }
//        } );

        // Delete
        btn_imgxoa = (ImageButton) findViewById( R.id.imageButtonXoa );
        btn_imgxoa.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // laytout cha . remove (tao tên biên moi dat gang cho no = StickerImageView nhu luc dau)
                try {
                    con_con_re_ca.removeView( tv_color );
                    con_re_ca.removeView( _stk_zz );
                    Toast.makeText( getApplicationContext(), "Removed successfully", Toast.LENGTH_SHORT ).show();
                    add_btn_text.setVisibility( View.VISIBLE );
                    btn_color.setVisibility(View.VISIBLE);
                } catch (Exception ex) {
                    Toast.makeText( getApplicationContext(), "You must select a photo first", Toast.LENGTH_SHORT ).show();
                }
            }

        } );

        btn_imgcamera = (ImageButton) findViewById( R.id.imageButtonCamera );
        btn_imgcamera.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent camera_intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
                    File file = getFile();
                    camera_intent.putExtra( MediaStore.EXTRA_OUTPUT, Uri.fromFile( file ) );
                    startActivityForResult( camera_intent, CAM_REQUEST );
                    _Horizontal_controView.setVisibility( View.VISIBLE );
                }catch (Exception ep){
                    Toast.makeText( MainActivity.this, "You can only crop photos from the gallery" , Toast.LENGTH_LONG ).show();
                }
            }
        } );

        btn_imgslfile = (ImageButton) findViewById( R.id.imageButtonFileImage );
        btn_imgslfile.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SelectImage();
                    _Horizontal_controView.setVisibility( View.VISIBLE );
                } catch (Exception ex) {

                }

            }
        } );

        final ImageButton btn_imgsave = (ImageButton) findViewById( R.id.imageButtonSave );
        btn_imgsave.setOnClickListener( new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                try {
                    edittext_color.setVisibility( View.INVISIBLE );
                    eText.setVisibility(View.INVISIBLE);

                    re_ca.setDrawingCacheEnabled( true );
                    Bitmap bitmap = Bitmap.createBitmap( re_ca.getDrawingCache() );
                    re_ca.setDrawingCacheEnabled( false );

                    saveToInternalStorage( bitmap );
                    String savedImageURL = MediaStore.Images.Media.insertImage( getContentResolver(), bitmap, "Bird", "Image of bird" );
                    // Parse the gallery image url to uri
                    Uri savedImageURI = Uri.parse( savedImageURL );
                    Toast.makeText( getApplicationContext(), "Save successfully", Toast.LENGTH_SHORT ).show();


                    Intent switchLayout = new Intent(getApplicationContext(),SecondActivity.class);
                    Bundle bundle = new Bundle();
                    switchLayout.putExtra("",bundle);
                    startActivity( switchLayout );
                } catch (Exception ex) {
                    Toast.makeText( getApplicationContext(), "You must select a photo first", Toast.LENGTH_SHORT ).show();
                }


            }

        } );
        btn_imgshare = (ImageButton) findViewById( R.id.imageButtonShare );
        btn_imgshare.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edittext_color.setVisibility( View.INVISIBLE );
                eText.setVisibility(View.INVISIBLE);

                re_ca.setDrawingCacheEnabled( true );
                Bitmap bitmap = Bitmap.createBitmap( re_ca.getDrawingCache() );
                re_ca.setDrawingCacheEnabled( false );
                onBitmapLoaded( bitmap );
            }
        } );

        btn_cut = (ImageButton) findViewById(R.id.imageButtonCut);
        btn_cut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                       cropview =  new CropView(MainActivity.this,_path);
                       setContentView(cropview);

//                        Intent crop_activity = new Intent(MainActivity.this,CropActivity.class);
//                        startActivity( crop_activity );


                }catch (Exception ep){
                   Toast.makeText( MainActivity.this, "You can only crop photos from the gallery" , Toast.LENGTH_LONG ).show();
                }

            }
        });

        btn_resume = (ImageButton) findViewById( R.id.imageButtonResume );
        btn_resume.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyBoardOnTabClicked(view);
                edittext_color.setVisibility( View.INVISIBLE );

            }
        } );


//        //-----------------action rate more share
//        ImageView i_rate = (ImageView) findViewById(R.id.imageView3);
//        ImageView i_share = (ImageView) findViewById(R.id.imageView4);
//        ImageView i_more = (ImageView) findViewById(R.id.imageView5);

//        i_rate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rateapp();
//            }
//        });
//        i_share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                moreApp();
//            }
//        });
//        i_more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                moreApp();
//            }
//        });

        ///ads
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId(ADMOB_INTERSTITIAL_ID);
//        requestNewInterstitial();
//        //
//        StartAppAd.init(this, START_APP_DEV_ID, START_APP_ADS_ID);
//        startAppAd = new StartAppAd(this);
//
//        adView = new AdView(this);
//        adView.setAdSize(AdSize.SMART_BANNER);
//        adView.setAdUnitId(ADMOB_BANNER_ID);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);
//
//        adView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                RelativeLayout layout = (RelativeLayout) findViewById(R.id.re_ads);
//                if (layout != null) {
//                    layout.removeAllViews();
//                    layout.addView(adView);
//                }
//            }
//        });

    }


    //---- tat ban phim'
    private void hideSoftKeyBoardOnTabClicked(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //-------- Text Choose Color
    private void OpenColorPickerDialog(boolean AlphaSupport) {

        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(MainActivity.this, DefaultColor, AlphaSupport, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int color) {


                DefaultColor = color;
                tv_color_font.setTextColor(color);
            }

            @Override
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {

                Toast.makeText(MainActivity.this, "Color Picker Closed", Toast.LENGTH_SHORT).show();
            }
        });
        ambilWarnaDialog.show();
    }
    //---- ket thuc
    //------ select image show
//    private void goToGallery() {
//        // in onCreate or any event where your want the user to
//        // select a file
//        Intent intent = new Intent();
//        intent.setType( "image/*" );
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult( Intent.createChooser( intent, "Select Picture" ), SELECT_PICTURE );
//
//    }
    //-------

    //----------  Share

    public void onBitmapLoaded(Bitmap bitmap) {
        // Get access to the URI for the bitmap
        Uri bmpUri = getLocalBitmapUri( bitmap );
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction( Intent.ACTION_SEND );
            shareIntent.putExtra( Intent.EXTRA_STREAM, bmpUri );
            shareIntent.setType( "image/*" );
            // Launch sharing dialog for image
            startActivity( Intent.createChooser( shareIntent, "Share Image" ) );
        } else {
            // ...sharing failed, handle error
        }
    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File( Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOWNLOADS ), "IMG_" + System.currentTimeMillis() + ".png" );
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream( file );
            bmp.compress(Bitmap.CompressFormat.PNG, 80, out);
            out.close();
            bmpUri = Uri.fromFile( file );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
    //--- ket thuc Share


    //------------------Save
    private void saveToInternalStorage(Bitmap bm2) {


        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream( pictureFile );
            bm2.compress( Bitmap.CompressFormat.PNG, 80, fos );
            fos.close();
        } catch (Exception e) {
        }
    }

    @Nullable
    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File( Environment.getExternalStorageDirectory() + "/DCIM/camera" );

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
    //--- ket thuc Save

    //-----------------camera and select gallery

    private void SelectImage() {
        final CharSequence[] items = {"Gallery", "Canel"};
        AlertDialog.Builder builder = new AlertDialog.Builder( MainActivity.this );
        builder.setTitle( "Selec Image Gallery" );
        AlertDialog.Builder builder1 = builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Gallery")) {

                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, SELECT_FILE);

                } else if (items[which].equals("Canel")) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();

    }

    //-----
    private File getFile() {
        File foder = new File( "sdcard/camera" );

        if (!foder.exists()) {
            foder.mkdir();
        }

        File image_file = new File( foder, "image_.png" );

        return image_file;
    }

    //---

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == CAM_REQUEST) {

                    imgAnh.setImageDrawable(Drawable.createFromPath(path_camera));
                    _path = path_camera;

                } else if (requestCode == SELECT_FILE || requestCode == SELECT_PICTURE) {

                    Uri selectedImage = data.getData();
                    String picturePath = null;      //cursor.getString(columnIndex);
                    try {
                        picturePath = PathUtil.getPath(getApplicationContext(), selectedImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Bitmap myBitmap = BitmapFactory.decodeFile(picturePath);
                    try {
                        ExifInterface exif = new ExifInterface(picturePath);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        Matrix matrix = new Matrix();
                        if (orientation == 6) {
                            matrix.postRotate(90);
                        } else if (orientation == 3) {
                            matrix.postRotate(180);
                        } else if (orientation == 8) {
                            matrix.postRotate(270);
                        }
                        myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true); // rotating bitmap
                    } catch (Exception e) {
                    }
                    imgAnh.setImageBitmap(myBitmap);
                    _path = picturePath;

                }
            }
        }
        }// --- ket thuc camera



    //add data ti Arraylist
    private void AnhXa() {

        arrayImage = new ArrayList<>();
        // image new
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh63 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh65 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh66 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh71 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh72 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh73 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh78 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh75 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh116 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh80 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh81 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh83 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh85 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh89 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh90 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh93 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh94 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh95 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh96 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh97 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh98 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh99 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh100 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh101 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh102 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh103 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh104 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh105 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh106 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh107 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh108 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh109 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh110 ) ); // dong 33
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh19 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh111 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh112 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh68 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh74 ) );
        // light
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.light_1 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.light_2 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.light_3 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.light_4 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.light_5 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.light_6 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.light_7 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.light_8 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.light_9 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.light_10 ) );
//        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.light_11 ) );
//        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.light_12 ) );
//        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.light_13 ) );
//        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.light_14 ) );
//        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.light_15 ) );

        //
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh4 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh114 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh115 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh5 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh6 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh7 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh8 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh113 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh9 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh10 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh11 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh13 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh1 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh3 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh69 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh70 ) );


        // dong vat
//        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh12 ) );
//        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh48 ) );
//        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh49 ) );
//        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh50 ) );
//        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh51 ) );
//        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh52 ) );
//        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh53 ) );
//        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh54 ) );
//        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh55 ) );
//        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh56 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh14 ) );
//        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh18 ) );
//        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh77 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh84 ) );

        // du thu
        //       arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh2 ) );
//        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh26 ) );
        //
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh60 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh62 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh59 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh20 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh15 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh16 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh17 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh22 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh21 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh31 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh24 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh25 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh29 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh33 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh34 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh35 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh37 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh38 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh39 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh40 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh41 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh46 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh57 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.non_1 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.non_2 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.non_3 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.non_4 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.non_5 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.non_6 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.non_7 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh47 ) ); // phone

        // mat na
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.mask_1 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.mask_2 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.mask_3 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.mask_4 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.mask_5 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.mask_6 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.mask_7 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.mask_8 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.mask_9 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.mask_10 ) );
  //      arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.mask_11 ) );
//        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.mask_13 ) );
//        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.mask_23 ) );
//        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.mask_26 ) );
        // mat kinh
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh36 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh45 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_30 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_1 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_2 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_3 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_4 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_5 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_6 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_8 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_7 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_9 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_10 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_11 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_12 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_13 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_14 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_15 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_16 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_17 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_18 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_19 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_20 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_21 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_23 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_25 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_26 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_27 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_28 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_29 ) );
//        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_30 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_33 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_34 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_35 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_36 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_37 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_38 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_39 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_40 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_41 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_42 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_43 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_44 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_45 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_46 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_47 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.glasses_48 ) );


        // emoji
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.emoji_1 ) );
//        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.emoji_2 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.emoji_3 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.emoji_4 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.emoji_5 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.emoji_6 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.emoji_7 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.emoji_8 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.emoji_9 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.emoji_10 ) );


        // cui 1
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh58 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh61 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh64 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh67 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh76 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh79 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh82 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh86 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh87 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh88 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh91 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh92 ) );

        // cui 2
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh42 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh43 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh0 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh27 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh28 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh30 ) );
        arrayImage.add( new HinhAnh( "You Have Selected", R.drawable.anh32 ) );


        // 179 image
    }

    ///rate more share
    ////rate more share
//


//    public void share() {
//        this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                boolean hasFacebookApp = false;
//                String url = "http://play.google.com/store/apps/details?id="
//                        + getApplicationContext().getPackageName();
//
//                String text = getResources().getString(R.string.app_name) + " : "
//                        + url;
//                Intent share = new Intent(Intent.ACTION_SEND);
//                share.setType("text/plain");
//                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//
//                share.putExtra(Intent.EXTRA_SUBJECT, "Share");
//                share.putExtra(Intent.EXTRA_TEXT, text);
//
//                startActivity(Intent.createChooser(share,
//                        "Share link!"));
//            }
//        });
//
//    }
}
