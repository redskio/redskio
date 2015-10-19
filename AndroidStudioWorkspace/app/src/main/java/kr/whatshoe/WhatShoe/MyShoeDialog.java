package kr.whatshoe.WhatShoe;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;

import kr.whatshoe.Util.HttpClient;

/**
 * Created by jaewoo on 2015-08-20.
 */
public class MyShoeDialog extends Dialog {
    final static int MALE = 1;
    final static int FEMALE = 2;
    Context context;

    private ArrayList<Integer> orderList;
    private ImageView[] images;
    private TextView[] statusTitles;
    private ImageView[] statusIcons;
    SharedPreferences orderPreferences;
    SharedPreferences loginPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.8f;
        getWindow().setAttributes(params);
        setContentView(R.layout.myshoe_dialog);
        orderPreferences = context.getSharedPreferences("order_pref",0);
        loginPreferences = context.getSharedPreferences("login_pref",0);
        orderList = new ArrayList<Integer>();
        setLayout();

    }

    public MyShoeDialog(Context context) {
        // Dialog 배경을 투명 처리 해준다.

        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
    }

    public MyShoeDialog(Context context, String title, String text) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
    }
    private void setLayout() {
        LinearLayout callBtn = (LinearLayout)findViewById(R.id.myshoe_call_layout);
        callBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("http://goto.kakao.com/@whatshoe"));
                context.startActivity(intent);
                return false;
            }
        });
        ImageButton closeBtn = (ImageButton)findViewById(R.id.myshoe_close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        ImageView myshoeImage = (ImageView)findViewById(R.id.myshoe_image);
        int sex = getOrderSex();
        if(sex==MALE) {
            LinearLayout myshoeLayout = (LinearLayout)findViewById(R.id.myshoe_layout);
            myshoeLayout.setVisibility(View.VISIBLE);
            LinearLayout myshoeEmptyLayout = (LinearLayout)findViewById(R.id.myshoe_empty_layout);
            myshoeEmptyLayout.setVisibility(View.GONE);
            myshoeImage.setImageResource(R.drawable.myshoe_men);
        } else if( sex==FEMALE) {
            LinearLayout myshoeLayout = (LinearLayout)findViewById(R.id.myshoe_layout);
            myshoeLayout.setVisibility(View.VISIBLE);
            LinearLayout myshoeEmptyLayout = (LinearLayout)findViewById(R.id.myshoe_empty_layout);
            myshoeEmptyLayout.setVisibility(View.GONE);
            myshoeImage.setImageResource(R.drawable.myshoe_women);
        } else{
            LinearLayout myshoeLayout = (LinearLayout)findViewById(R.id.myshoe_layout);
            myshoeLayout.setVisibility(View.GONE);
            LinearLayout myshoeEmptyLayout = (LinearLayout)findViewById(R.id.myshoe_empty_layout);
            myshoeEmptyLayout.setVisibility(View.VISIBLE);
        }
        initImageResources();
        initStatus();
        TextView callText = (TextView)findViewById(R.id.call_text);
        Html.ImageGetter imageGetter = new Html.ImageGetter() {

            @Override

            public Drawable getDrawable(String source) {
                if ( source.equals( "icon" ) ){
                    Drawable drawable = context.getResources().getDrawable( R.drawable.kakao );
                    drawable.setBounds( 0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight() );
                    return drawable;
                }

                return null;

            }
        };
        Spanned htmlText = Html.fromHtml( "지금 <img src=\"icon\" width=50 height=50> 문의 하기", imageGetter, null );
        callText.setText(htmlText);

}
    private int getOrderSex(){
        initOrderData();
        if(orderList.isEmpty())
            return 0;
        if(orderList.get(0)>=14){
            return FEMALE;
        } else {
            return MALE;
        }
    }
    private void initOrderData(){

        String order = orderPreferences.getString("orderCode","0");
        int ordercode = Integer.parseInt(order);
        while(ordercode%100 !=0){
            orderList.add(ordercode%100);
            ordercode/=100;
        }
    }
    private void initImageResources(){
        images= new ImageView[8];
        images[0] =(ImageView)findViewById(R.id.myshoe_men1);
        images[1]=(ImageView)findViewById(R.id.myshoe_men2);
        images[2]=(ImageView)findViewById(R.id.myshoe_men3);
        images[3]=(ImageView)findViewById(R.id.myshoe_men4);
        images[4]=(ImageView)findViewById(R.id.myshoe_women1);
        images[5]=(ImageView)findViewById(R.id.myshoe_women2);
        images[6]=(ImageView)findViewById(R.id.myshoe_women3);
        images[7]=(ImageView)findViewById(R.id.myshoe_women4);
        for (int i= 0 ; i<orderList.size();i++){
                images[orderList.get(i)-10].setVisibility(View.VISIBLE);
        }
    }
    private void initStatus() {
        statusIcons= new ImageView[3];
        statusIcons[0] =(ImageView)findViewById(R.id.pickup_image);
        statusIcons[1] =(ImageView)findViewById(R.id.fix_image);
        statusIcons[2] =(ImageView)findViewById(R.id.delivery_image);
        statusTitles= new TextView[3];
        statusTitles[0] =(TextView)findViewById(R.id.pickup_title);
        statusTitles[1] =(TextView)findViewById(R.id.fix_title);
        statusTitles[2] =(TextView)findViewById(R.id.delivery_title);
        RequestParams params = new RequestParams();
        params.put("id", loginPreferences.getString("id", "whatshoe"));
        params.put("order_time", orderPreferences.getString("orderTime", "0"));
        params.put("order_code", orderPreferences.getString("orderCode","0"));
        HttpClient.post("member/android_get_orderstatus.php", params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2,
                                  Throwable arg3) {
                Toast.makeText(context, "인터넷 접속상태를 확인해 주세요.",
                        Toast.LENGTH_SHORT).show();
                Log.i("PostingFailed", arg2);
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, String arg2) {
                Log.i("!!!!!!!!!!!!",arg2);
                if (arg2.trim().equals("\uFEFFfail")) {
                    turnOnStatus((0));
                } else if(arg2.trim().equals("\uFEFF0")){
                    turnOnStatus(0);
                } else if(arg2.trim().equals("\uFEFF1")){
                    turnOnStatus(1);
                } else if(arg2.trim().equals("\uFEFF2")){
                    turnOnStatus(2);
                }
            }
        });
    }
    private void turnOnStatus(int value){
        statusIcons[0].setImageResource(R.drawable.myshoe_pickup);
        statusIcons[1].setImageResource(R.drawable.myshoe_fix);
        statusIcons[2].setImageResource(R.drawable.myshoe_delivery);
        statusTitles[0].setTextColor(Color.argb(80, 255, 255, 255));
        statusTitles[1].setTextColor(Color.argb(80, 255, 255, 255));
        statusTitles[2].setTextColor(Color.argb(80, 255, 255, 255));
        switch(value){
            case 0:
                statusIcons[0].setImageResource(R.drawable.myshoe_pickup_check);
                statusTitles[0].setTextColor(Color.argb(150, 255, 255, 255));
                break;
            case 1:
                statusIcons[1].setImageResource(R.drawable.myshoe_fix_check);
                statusTitles[1].setTextColor(Color.argb(150, 255, 255, 255));
                break;
            case 2:
                statusIcons[2].setImageResource(R.drawable.myshoe_delivery_check);
                statusTitles[2].setTextColor(Color.argb(150, 255, 255, 255));
                break;
        }
    }

    public void close() {
        this.dismiss();
    }

}

