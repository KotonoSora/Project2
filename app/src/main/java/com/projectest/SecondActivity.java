package com.projectest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class SecondActivity extends Activity {
    private ImageButton btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout2);

        btBack = (ImageButton) findViewById( R.id.btn_comback );

        btBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onBackPressed();
//                finish();
//                startActivity( new Intent( getApplicationContext(), MainActivity.class ) );

            }
        });
    }

    @Override
    public void onBackPressed()
    {
        // do stuff
        super.onBackPressed();
    }
}