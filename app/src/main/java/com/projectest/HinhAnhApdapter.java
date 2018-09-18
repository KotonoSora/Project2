package com.projectest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Admin on 8/21/2017.
 */

public class HinhAnhApdapter extends BaseAdapter{

    private Context context;
    private int layout;
    private List<HinhAnh> hinhAnhList;

    public HinhAnhApdapter(Context context, int layout, List<HinhAnh> hinhAnhList) {
        this.context = (Context) context;
        this.layout = layout;
        this.hinhAnhList = hinhAnhList;
    }

    @Override
    public int getCount() {
        return hinhAnhList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHoder{
        ImageView imgHinh;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHoder hoder;
        if( convertView == null){
            hoder = new ViewHoder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_gridview,null);
            hoder.imgHinh = (ImageView) convertView.findViewById(R.id.imageView2);
            convertView.setTag(hoder);
        }else {
            hoder = (ViewHoder) convertView.getTag();
        }
        //nhưng lưu ý khi ép kích thước ảnh vào Arraylist thì khi mang ra sủ dụng ảnh sẽ ko còn được kích thước như ban đầu
        //vì vậy khi sử dụng làm ảnh to trên màn hình thì phải resize lại kích thước cũ của ảnh

        //ép kích thước ảnh trước khi add vào arraylist
        // ở đây em dùng lớp đối tượng hình ảnh không chứa ảnh dưới dạng draw nên không the dùng ép kiểu ở đây
        // phải dùng ở chỗ gridview add kiểu vào

        HinhAnh hinhAnh = hinhAnhList.get(position);

        // ep kick thuoc anh
        Drawable d = null;
        d = context.getResources().getDrawable( hinhAnh.getHinh() );
        Bitmap b = ((BitmapDrawable) d).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 120, 120, false);// anh thấy ảnh nó hơi mờ có thể tăng kích thước ảnh ở đây nhé
        d = new BitmapDrawable(context.getResources(), bitmapResized);
        hoder.imgHinh.setImageDrawable( d );

        return convertView;
    }

}
