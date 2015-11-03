package kr.whatshoe.whatShoe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class LoadingActivity extends Activity {
    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(kr.whatshoe.whatShoe.R.layout.activity_loading);
        mRunnable = new Runnable() {
            @Override
            public void run() {
                SharedPreferences preference = getSharedPreferences("login_pref", 0);
                if (!preference.contains("id")) {
                    Intent loginIntent = new Intent();
                    loginIntent.setClass(LoadingActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                }
                finish();

            }
        };
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 1000);
    }
}