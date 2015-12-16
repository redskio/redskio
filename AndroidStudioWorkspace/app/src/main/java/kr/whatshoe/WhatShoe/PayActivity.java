package kr.whatshoe.whatShoe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Random;

import kr.whatshoe.Order.FixOrder;
import kr.whatshoe.Util.HttpClient;

public class PayActivity extends AppCompatActivity {
    ArrayList<FixOrder> arrayList;
    ArrayList<FixOrder> orderList = new ArrayList<FixOrder>();
    SharedPreferences loginPreference;
    SharedPreferences orderPreference;
    private static final String CODEVERSION= "#02";
    String time;
    String locationResult="";
    String locationResultDetail = "";
    int totalPay = 0;
    private EditText phoneText;
    private EditText oneLintText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        actionBarSetting();
        Bundle bundle = getIntent().getExtras();
        if(bundle==null || bundle.isEmpty()){
            finish();
        }
        loginPreference = getSharedPreferences("login_pref", 0);
        orderPreference = getSharedPreferences("order_pref", 0);
        arrayList = bundle.getParcelableArrayList("order");
        locationResult = bundle.getString("locationResult");
        locationResultDetail = bundle.getString("locationResultDetail");
        final EditText lResult = (EditText)findViewById(R.id.locationResult);
        EditText lResultDetail = (EditText)findViewById(R.id.locationResultDetail);
        lResult.setText(locationResult);
        lResultDetail.setText(locationResultDetail);
        phoneText = (EditText)findViewById(R.id.phoneCheck);
        final String phoneNum = loginPreference.getString("phone","");
        if(phoneNum.length()<8){
            getPhoneNum();
        }
        phoneText.setText(phoneNum);
        oneLintText = (EditText)findViewById(R.id.oneLineText);


        for (int i = 0; i < arrayList.size(); i++) {
            FixOrder order = arrayList.get(i);
            if (order.getIsChecked()>0){
                totalPay+= order.getPrice();
                orderList.add(order);
            }
        }
        ListView listView = (ListView) findViewById(R.id.listView2);
        listView.setAdapter(new OrderAdapter(this, R.layout.order_item, orderList));
        setListViewHeightBasedOnChildren(listView);
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
                postPGOrder("card");
            }
        });
        Button phonePayBtn = (Button) findViewById(R.id.phonePayBtn);
        phonePayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postPGOrder("phone");
            }
        });
        Button pointPayBtn = (Button) findViewById(R.id.pointPayBtn);
        pointPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(PayActivity.this, "결제 방법을 준비중입니다. ",
                        Toast.LENGTH_SHORT).show();
            }
        });
        Button placePayBtn = (Button) findViewById(R.id.placePayBtn);
        placePayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postOrder();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
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
    private void getPhoneNum(){
        RequestParams params = new RequestParams();
        params.put("id", loginPreference.getString("id", "whatshoe"));

        HttpClient.post("member/android_get_phoneNum.php", params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2,
                                  Throwable arg3) {
                Toast.makeText(PayActivity.this, "인터넷 접속이 원할하지 않습니다.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, String arg2) {
                if (arg2.trim().equals("\uFEFFfail")) {

                } else {
                    phoneText.setText(arg2.substring(1));
                }
            }
        });
    }
    private void postPGOrder(final String how){
        if(phoneText.length()<8){
            Toast.makeText(PayActivity.this, "연락 가능한 연락처를 입력해 주세요.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        time = getCurTime();
        final int randomcode = new Random().nextInt(100000);
        RequestParams params = new RequestParams();
        params.put("id", loginPreference.getString("id", "whatshoe"));
        params.put("order_time", time);
        params.put("order_code", getOrderCode());
        params.put("order_address", locationResult + " " +locationResultDetail);
        params.put("order_phone",phoneText.getText().toString());
        params.put("order_Text",oneLintText.getText().toString());
        params.put("with",how);
        params.put("code",randomcode);
        loginPreference.edit().putString("phone",phoneText.getText().toString()).apply();

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
                    registOrderPref();
                    Intent intent = new Intent();
                    intent.putExtra("price",Integer.toString(totalPay));
                    intent.putExtra("openpaytype",how);
                    intent.putExtra("recvphone", phoneText.getText().toString());
                    intent.putExtra("code", randomcode);
                    intent.setClass(PayActivity.this, PGActivity.class);
                    startActivityForResult(intent, 0);
                } else {
                    Toast.makeText(PayActivity.this, "결재 정보에 문제가 있습니다. 관리자에게 문의해 주세요.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void postOrder(){
        if(phoneText.length()<8){
            Toast.makeText(PayActivity.this, "연락 가능한 연락처를 입력해 주세요.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        time = getCurTime();
        RequestParams params = new RequestParams();
        params.put("id", loginPreference.getString("id", "whatshoe"));
        params.put("order_time", time);
        params.put("order_code", getOrderCode());
        params.put("order_address", locationResult + " " +locationResultDetail);
        params.put("order_state", 0);
        params.put("order_phone",phoneText.getText().toString());
        params.put("order_Text",oneLintText.getText().toString());

        loginPreference.edit().putString("phone",phoneText.getText().toString()).apply();

        HttpClient.post("member/android_regist_order.php", params, new TextHttpResponseHandler() {

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
                    pushOrder();
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
        String orderCode = CODEVERSION;
        FixOrder order = null;
        for(int i = 0 ; i <arrayList.size();i++){
            order =  arrayList.get(i);
            if(order.getIsChecked()>0){
                orderCode+= (order.getGender()*100 + order.getCode());
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
    public void setListViewHeightBasedOnChildren(ListView listView) {
        OrderAdapter listAdapter = (OrderAdapter) listView.getAdapter();
        if (listAdapter == null) {
            //pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    private void pushOrder(){
        RequestParams params = new RequestParams();
        params.put("id",loginPreference.getString("id","whatshoe"));
        params.put("message", "곧 왓슈가 달려갑니다. 도착 예정 시간은 30분입니다.");
        params.put("token", loginPreference.getString("token",""));

        HttpClient.post("member/android_push_me.php", params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2,
                                  Throwable arg3) {
                Toast.makeText(PayActivity.this, "인터넷 접속이 원할하지 않습니다.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, String arg2) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(PayActivity.this, "요청 결과를 처리 중입니다.",
                Toast.LENGTH_SHORT).show();
        RequestParams params = new RequestParams();
        params.put("id", loginPreference.getString("id", "whatshoe"));
        params.put("order_time", orderPreference.getString("orderTime", "0"));
        params.put("order_code", orderPreference.getString("orderCode","0"));
        HttpClient.post("member/android_get_orderstatus.php", params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2,
                                  Throwable arg3) {
                Toast.makeText(PayActivity.this, "인터넷 접속상태를 확인해 주세요.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, String arg2) {
                if (arg2.trim().equals("\uFEFFfail")) {
                    Toast.makeText(PayActivity.this, "결제가 취소되었습니다.",
                            Toast.LENGTH_LONG).show();
                    orderPreference.edit().clear().commit();
                    finish();
                } else {
                    Toast.makeText(PayActivity.this, "결제가 완료되었습니다.",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }
}
