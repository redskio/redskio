package kr.whatshoe.WhatShoe;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.whatshoe.Util.HttpClient;
import kr.whatshoe.Util.WhatshoeDbHelper;

/**
 * Created by jaewoo on 2015-08-20.
 */
public class CouponDialog extends Dialog {
    Context context;
    private ArrayList<Coupon> list;
    WhatshoeDbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.8f;
        getWindow().setAttributes(params);
        setContentView(R.layout.coupon_dialog);
        if(list== null){
            list = new ArrayList<Coupon>();
        }
        setLayout();

    }

    public CouponDialog(Context context) {
        // Dialog 배경을 투명 처리 해준다.

        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
    }
    public CouponDialog(Context context, ArrayList<Coupon> array) {
        // Dialog 배경을 투명 처리 해준다.
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
        this.list = array;
    }

    private void setLayout() {
        ImageButton closeBtn = (ImageButton)findViewById(R.id.coupon_close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        final EditText couponCode = (EditText)findViewById(R.id.couponText);
        Button registerBtn  = (Button)findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestParams params = new RequestParams();
                params.put("code", couponCode.getText());
                HttpClient.postJson("member/android_get_coupon.php", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        JSONObject jo = response;
                        try {
                            String desc = jo.getString("desc");
                            String date = jo.getString("date");
                            String price = jo.getString("price");
                            SharedPreferences idPreferences = context.getSharedPreferences("login_pref", 0);
                            String id = idPreferences.getString("id", "WhatShoe");
                            addCouponToDB(desc, date, price, 1);
                            Toast.makeText(context,price+"원 쿠폰입니다.",Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            Toast.makeText(context,"등록되지 않은 쿠폰번호입니다.",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        } catch (Exception exception){
                            Toast.makeText(context,"등록되지 않은 쿠폰번호입니다.",Toast.LENGTH_SHORT).show();
                            exception.printStackTrace();
                        }
                        super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Toast.makeText(context,"등록되지 않은 쿠폰번호입니다.",Toast.LENGTH_SHORT).show();
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
            }
        });


}

    public void close() {
        this.dismiss();
    }

    private void addCouponToDB(String desc, String date, String price, int state){
        dbHelper = new WhatshoeDbHelper(getContext());
        dbHelper.open();
        dbHelper.createCoupon(desc,date,price, Integer.toString(state));
    }
}


