package kr.whatshoe.WhatShoe;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import kr.whatshoe.Util.HttpClient;

public class PayActivity extends AppCompatActivity {
    ArrayList<FixOrder> arrayList;
    ArrayList<FixOrder> orderList = new ArrayList<FixOrder>();
    SharedPreferences loginPreference;
    SharedPreferences orderPreference;
    String time;
    String locationResult="";
    String locationResultDetail = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        actionBarSetting();
        Bundle bundle = getIntent().getExtras();
        loginPreference = getSharedPreferences("login_pref", 0);
        orderPreference = getSharedPreferences("order_pref", 0);
        arrayList = bundle.getParcelableArrayList("order");
        locationResult = bundle.getString("locationResult", "");
        locationResultDetail = bundle.getString("locationResultDetail","");
        final EditText lResult = (EditText)findViewById(R.id.locationResult);
        EditText lResultDetail = (EditText)findViewById(R.id.locationResultDetail);
        lResult.setText(locationResult);
        lResultDetail.setText(locationResultDetail);




        int totalPay = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            FixOrder order = arrayList.get(i);
            if (order.getIsChecked()>0){
                totalPay+= order.getPrice();
                orderList.add(order);
            }
        }
        ListView listView = (ListView) findViewById(R.id.listView2);
        listView.setAdapter(new OrderAdapter(this, R.layout.order_item, orderList));
        TextView totalText = (TextView) findViewById(R.id.total_pay_text);
        totalText.setText(totalPay + " 원");
        Button button = (Button) findViewById(R.id.btn_cancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button cardPayBtn = (Button) findViewById(R.id.cardPayBtn);
        cardPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postOrder();
                finish();
            }
        });
        Button phonePayBtn = (Button) findViewById(R.id.phonePayBtn);
        phonePayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postOrder();
                finish();
            }
        });
        Button pointPayBtn = (Button) findViewById(R.id.pointPayBtn);
        pointPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postOrder();
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    private void postOrder(){
        time = getCurTime();
        RequestParams params = new RequestParams();
        params.put("id", loginPreference.getString("id", "whatshoe"));
        params.put("order_time", time);
        params.put("order_code", getOrderCode());
        params.put("order_address", locationResult + " " +locationResultDetail);
        params.put("order_state", 0);

        HttpClient.post("member/android_order.php", params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2,
                                  Throwable arg3) {
                Toast.makeText(PayActivity.this, "인터넷 접속이 원할하지 않습니다.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, String arg2) {
                if (arg2.trim().equals("\uFEFFsuccess")) {
                    Toast.makeText(PayActivity.this, "성공적으로 결제되었습니다. 왓슈맨이 달려갑니다.",
                            Toast.LENGTH_SHORT).show();
                    registOrderPref();
                    finish();
                } else {
                    Toast.makeText(PayActivity.this, "결재 정보에 문제가 있습니다. 관리자에게 문의해 주세요.",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
    private String getCurTime(){
        SimpleDateFormat formatter = new SimpleDateFormat ( "yyyy.MM.dd HH:mm:ss", Locale.KOREAN );
        Date currentTime = new Date( );
        String dTime = formatter.format ( currentTime );
        return dTime;
    }
    private String getOrderCode(){
        String orderCode = "";
        for(int i = 0 ; i < arrayList.size() ; i++){
            if(arrayList.get(i).getIsChecked()>0){
                orderCode+="1"+i;
            }
        }
        return orderCode;
    }
    private void registOrderPref(){
        if( orderPreference.contains("orderCode")){
            orderPreference.edit().clear().apply();
            orderPreference.edit().putString("orderCode", getOrderCode()).apply();
            orderPreference.edit().putString("orderTime", time).apply();
            orderPreference.edit().putBoolean("isRead",false).apply();
        } else {
            orderPreference.edit().putString("orderCode", getOrderCode()).apply();
            orderPreference.edit().putString("orderTime", time).apply();
            orderPreference.edit().putBoolean("isRead",false).apply();
        }
    }
    private void actionBarSetting(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setElevation(0);
        actionBar.setTitle("결제정보 확인 및 결제");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(255, 35,
                23, 21)));
    }
}
