package com.projectest;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.projectest.ListView_typeface.Item_listview_Typeface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Admin on 10/2/2017.
 */

public class Common_str {
    //get list type face
    public static ArrayList<Item_listview_Typeface> _getList_typeface(Context context) {
        ArrayList<Item_listview_Typeface> _list = new ArrayList<Item_listview_Typeface>();

        AssetManager assetManager = context.getAssets();
        String[] files = new String[0];
        try {
            files = assetManager.list( "fonts" );
        } catch (Exception e) {
        }
        List<String> it = Arrays.asList( files );

        for (int i = 0; i < it.size(); i++) {
            String path = "fonts/" + it.get(i);

             Typeface tf = Typeface.createFromAsset(context.getAssets(), path);
            _list.add(new Item_listview_Typeface( tf ));
        }
        return _list;
    }
}
