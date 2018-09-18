package com.projectest.ListView_typeface;

import android.graphics.Typeface;


public class Item_listview_Typeface {
    private Typeface type ;

    public Item_listview_Typeface() {
    }

    public Item_listview_Typeface(Typeface type) {
        this.type = type;
    }

    public Typeface getType() {
        return type;
    }

    public void setType(Typeface type) {
        this.type = type;
    }
}
