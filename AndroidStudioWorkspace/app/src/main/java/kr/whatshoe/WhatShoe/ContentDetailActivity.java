package kr.whatshoe.whatShoe;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
/*************************************************************
Activity for News contents detail page.
 hard coded contents.
 *************************************************************/
public class ContentDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int content = getIntent().getIntExtra("content",0);
        switch(content){
            case 0:
                setContentView(R.layout.fragment_content_detail);
                break;
            case 1:
                setContentView(R.layout.fragment_content_detail1);
                break;
            case 2:
                setContentView(R.layout.fragment_content_detail2);
                break;
            case 3:
                setContentView(R.layout.fragment_content_detail3);
                break;
            case 4:
                setContentView(R.layout.fragment_content_detail4);
                break;
        }

        actionBarSetting();
        RelativeLayout layout = (RelativeLayout) findViewById(kr.whatshoe.whatShoe.R.id.service_layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               MainActivity.currentFragment=MainActivity.FRAGMENT_FLAG_SERVICEORDER;
                finish();
            }
        });
    }
    private void actionBarSetting(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setElevation(0);
        actionBar.setTitle("매거진");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(255, 35,
                23, 21)));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
