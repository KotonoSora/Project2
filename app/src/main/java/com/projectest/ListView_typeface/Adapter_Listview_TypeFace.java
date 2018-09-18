package com.projectest.ListView_typeface;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.projectest.R;

import java.util.ArrayList;


public class Adapter_Listview_TypeFace extends BaseAdapter{


    private Context context;
    private ArrayList<Item_listview_Typeface> listData;
    private LayoutInflater layoutInflater;

    public Adapter_Listview_TypeFace(Context _a, ArrayList<Item_listview_Typeface> l_Data) {
        context = _a;
        listData = l_Data;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHoder{
        TextView textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate( R.layout.item_list_font, null); // dung laytout list cua _font bua sau nen dat ten dung Item chu dau
            holder.textView = (TextView) convertView.findViewById( R.id.show_text_view); // hien chu cua text font va con cua item_list_font

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

       holder.textView.setTypeface(listData.get( position ).getType());

        return convertView;
    }

    static class ViewHolder {  // dung sau ham public
        TextView textView;
    }
}
