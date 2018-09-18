package com.projectest.View_folder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.projectest.R;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {

	// Declare variables
	private Context context;
	private ArrayList<String> filepath;

	public GridViewAdapter(Context a, int view_image, ArrayList<String> fpath) {
		context = a;
		filepath = fpath;
	}

    public int getCount() {
		return filepath.size();

	}

	public Object getItem(int position) {
		return filepath.get(position); // tra ve 1 bien offject
	}

	public long getItemId(int position) {
		return position;
	}

	private class ViewHoder{
		ImageView imgHinh; TextView textView;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHoder hoder;
		if( convertView == null){

			hoder = new ViewHoder();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.listview_with_text_image,null);
			hoder.imgHinh = (ImageView) convertView.findViewById(R.id.image);

			hoder.textView = (TextView) convertView.findViewById( R.id.text ) ;

			convertView.setTag(hoder);
		}else {
			hoder = (ViewHoder) convertView.getTag();
		}
		// Decode the filepath with BitmapFactory followed by the position
		// cach lay duong dan file List File Ảnh dùng đường dẫn String.
		try{
            Bitmap myBitmap = BitmapFactory.decodeFile(filepath.get( position ));
            int ww = myBitmap.getWidth();
            int hh = myBitmap.getHeight();


            float _scale = 300;

            if(ww<hh){
                _scale = hh/ww;
            }else{
                _scale = ww/hh;
            }

            int h_size = (int) (300/_scale);

            hoder.textView.setText(  filepath.get( position ));
            // Set the decoded bitmap into ImageView
            Bitmap bitmapResized = Bitmap.createScaledBitmap(myBitmap, 300,h_size , false);// anh thấy ảnh nó hơi mờ có thể tăng kích thước ảnh ở đây nhé
            Drawable d = new BitmapDrawable(context.getResources(), bitmapResized);
            hoder.imgHinh.setImageDrawable( d );
        }catch (Exception ep){

        }
		return convertView;
	}
}