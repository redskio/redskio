package kr.whatshoe.WhatShoe;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class DrawerActivity extends AppCompatActivity {
    public static final int POINT_FRAGMENT_TYPE = 1;
    public static final int GIFT_FRAGMENT_TYPE = 2;
    public static final int HEALTHCARE_FRAGMENT_TYPE = 3;
    public static final int COUPON_FRAGMENT_TYPE = 4;
    public static final int EVENT_FRAGMENT_TYPE = 5;
    public static final int NOTICE_FRAGMENT_TYPE = 6;
    public static final int HISTORY_FRAGMENT_TYPE = 7;
    public static final int GIFT_DETAIL_FRAGMENT_TYPE=21;
    public static final int GIFT_DETAIL_FRAGMENT2_TYPE=22;
    public static int type= 0;
    private String title = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        type = getIntent().getIntExtra("Fragment",POINT_FRAGMENT_TYPE);
        switch(type){
            case POINT_FRAGMENT_TYPE :
                title = "POINT";
                actionBarSetting();
                getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.WhatShoe.R.id.container, new PointFragment()).commit();
                break;
            case GIFT_FRAGMENT_TYPE :
                title = "선물 하기";
                actionBarSetting();
                getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.WhatShoe.R.id.container, new GiftFragment()).commit();
                break;
            case HEALTHCARE_FRAGMENT_TYPE :
                title = "왓슈 헬스케어";
                actionBarSetting();
                getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.WhatShoe.R.id.container, new HealthcareFragment()).commit();
                break;
            case COUPON_FRAGMENT_TYPE:
                title = "왓슈 쿠폰";
                actionBarSetting();
                getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.WhatShoe.R.id.container, new CouponFragment()).commit();
                break;
            case EVENT_FRAGMENT_TYPE:
                title = "왓슈 이벤트";
                actionBarSetting();
                getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.WhatShoe.R.id.container, new EventFragment()).commit();
                break;
            case HISTORY_FRAGMENT_TYPE:
                title = "주문 내역";
                actionBarSetting();
                getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.WhatShoe.R.id.container, new HistoryFragment()).commit();
                break;
        }
    }
    private void actionBarSetting(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setElevation(0);
        actionBar.setTitle(title);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(255, 35,
                23, 21)));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if (type == GIFT_DETAIL_FRAGMENT_TYPE) {
                    getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.WhatShoe.R.id.container, new GiftFragment()).commit();
                } else if (type == GIFT_DETAIL_FRAGMENT2_TYPE) {
                    getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.WhatShoe.R.id.container, new GiftFragment()).commit();
                } else {
                    finish();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if (type == GIFT_DETAIL_FRAGMENT_TYPE) {
            getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.WhatShoe.R.id.container, new GiftFragment()).commit();
        } else if (type == GIFT_DETAIL_FRAGMENT2_TYPE) {
            getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.WhatShoe.R.id.container, new GiftFragment()).commit();
        } else {
            finish();
        }
    }
}
