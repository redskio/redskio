package kr.whatshoe.whatShoe;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.whatshoe.Util.HttpClient;

/**
 * Created by jaewoo on 2015-08-20.
 */
public class MyShoeDialog extends Dialog implements View.OnClickListener {
    final static int MALE = 1;
    final static int FEMALE = 2;
    final static int PREMALE = 3;
    final static int PREFEMALE = 4;
    final static int NODATA = 5;
    Context context;

    private ArrayList<Integer> orderList;
    private ImageView[] images;
    private TextView[] statusTitles;
    private TextView[] statusGuide;
    private ImageView[] statusIcons;
    private int sex;
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
        orderPreferences = context.getSharedPreferences("order_pref", 0);
        loginPreferences = context.getSharedPreferences("login_pref", 0);
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
        LinearLayout callBtn = (LinearLayout) findViewById(R.id.myshoe_call_layout);
        callBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://goto.kakao.com/@whatshoe"));
                context.startActivity(intent);
                return false;
            }
        });
        ImageButton closeBtn = (ImageButton) findViewById(R.id.myshoe_close);
        closeBtn.setOnClickListener(this);
        ImageView myshoeImage = (ImageView) findViewById(R.id.myshoe_image);
        ImageButton pickUpCancleBtn = (ImageButton) findViewById(R.id.pickup_cancle_btn);
        pickUpCancleBtn.setOnClickListener(this);
        Log.i("!!!!!!!!!!","!!!!!!!!!");
        initStatus();
        sex = getOrderSex();
        if (sex == MALE) {
            LinearLayout myshoeLayout = (LinearLayout) findViewById(R.id.myshoe_layout);
            myshoeLayout.setVisibility(View.VISIBLE);
            LinearLayout myshoeEmptyLayout = (LinearLayout) findViewById(R.id.myshoe_empty_layout);
            myshoeEmptyLayout.setVisibility(View.GONE);
            myshoeImage.setImageResource(R.drawable.myshoe_men1);
            changeGuideText(0);
        } else if (sex == FEMALE) {
            LinearLayout myshoeLayout = (LinearLayout) findViewById(R.id.myshoe_layout);
            myshoeLayout.setVisibility(View.VISIBLE);
            LinearLayout myshoeEmptyLayout = (LinearLayout) findViewById(R.id.myshoe_empty_layout);
            myshoeEmptyLayout.setVisibility(View.GONE);
            myshoeImage.setImageResource(R.drawable.myshoe_women);
            changeGuideText(0);
        } else if (sex == PREMALE) {
            LinearLayout myshoeLayout = (LinearLayout) findViewById(R.id.myshoe_layout);
            myshoeLayout.setVisibility(View.VISIBLE);
            LinearLayout myshoeEmptyLayout = (LinearLayout) findViewById(R.id.myshoe_empty_layout);
            myshoeEmptyLayout.setVisibility(View.GONE);
            myshoeImage.setImageResource(R.drawable.myshoe_pre_men);
            changeGuideText(1);
        } else if (sex == PREFEMALE) {
            LinearLayout myshoeLayout = (LinearLayout) findViewById(R.id.myshoe_layout);
            myshoeLayout.setVisibility(View.VISIBLE);
            LinearLayout myshoeEmptyLayout = (LinearLayout) findViewById(R.id.myshoe_empty_layout);
            myshoeEmptyLayout.setVisibility(View.GONE);
            myshoeImage.setImageResource(R.drawable.myshoe_pre_women);
            changeGuideText(1);
        } else {
            LinearLayout myshoeLayout = (LinearLayout) findViewById(R.id.myshoe_layout);
            myshoeLayout.setVisibility(View.GONE);
            LinearLayout myshoeEmptyLayout = (LinearLayout) findViewById(R.id.myshoe_empty_layout);
            myshoeEmptyLayout.setVisibility(View.VISIBLE);
        }

        TextView callText = (TextView) findViewById(R.id.call_text);
        Html.ImageGetter imageGetter = new Html.ImageGetter() {

            @Override

            public Drawable getDrawable(String source) {
                if (source.equals("icon")) {
                    Drawable drawable = context.getResources().getDrawable(R.drawable.kakao);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    return drawable;
                }

                return null;

            }
        };
        Spanned htmlText = Html.fromHtml("지금 <img src=\"icon\" width=50 height=50> 문의 하기", imageGetter, null);
        callText.setText(htmlText);
        initImageResources();

    }

    private int getOrderSex() {
        initOrderData();
        if (orderList.isEmpty())
            return NODATA;
        int firstOrder = orderList.get(0);
        if (firstOrder < 200) {
            return MALE;
        } else if (firstOrder < 300) {
            return FEMALE;
        } else if (firstOrder < 400) {
            return PREMALE;
        } else {
            return PREFEMALE;
        }
    }

    private void initOrderData() { // integer의 범위를 벗어나기 때문에 String 상태에서 3글자씩 잘라 integer형태로 orderList에 삽입
        String order = orderPreferences.getString("orderCode", "0");
        Log.i("whatshoe", order);
        int index = 3; //version code #01 을 제외한 실제 코드, Local이므로 versionCode가 필요 없음
        while (true) {

            if(index==order.length()|| order.length()<3){
                break;
            }
            String ordercode = order.substring(index, index + 3);
            int codeItem = Integer.parseInt(ordercode);

            Log.e("!!!!!!!!", ordercode);
            orderList.add(codeItem);
            index+=3;
        }
    }

    private void initImageResources() {
        images = new ImageView[4];
        if(sex==MALE) {

            images[0] = (ImageView) findViewById(R.id.myshoe_men1);//구두닦이
            images[1] = (ImageView) findViewById(R.id.myshoe_men2);//반창
            images[2] = (ImageView) findViewById(R.id.myshoe_men3);//통굽
            images[3] = (ImageView) findViewById(R.id.myshoe_men4);//보조굽
        } else if(sex==FEMALE) {
            images[0] = (ImageView) findViewById(R.id.myshoe_women1);//구두닦이
            images[1] = (ImageView) findViewById(R.id.myshoe_women2);//보조굽소
            images[2] = (ImageView) findViewById(R.id.myshoe_women3);//보조굽중

        }
        for (int i = 0; i < orderList.size(); i++) {
            try {
                images[orderList.get(i) %100 -1].setVisibility(View.VISIBLE);
            } catch (Exception e) {
                continue;
            }
        }

    }

    private void initStatus() {
        statusIcons = new ImageView[3];
        statusIcons[0] = (ImageView) findViewById(R.id.pickup_image);
        statusIcons[1] = (ImageView) findViewById(R.id.fix_image);
        statusIcons[2] = (ImageView) findViewById(R.id.delivery_image);
        statusTitles = new TextView[3];
        statusTitles[0] = (TextView) findViewById(R.id.pickup_title);
        statusTitles[1] = (TextView) findViewById(R.id.fix_title);
        statusTitles[2] = (TextView) findViewById(R.id.delivery_title);
        statusGuide = new TextView[3];
        statusGuide[0] = (TextView)findViewById(R.id.pickup_guide);
        statusGuide[1] = (TextView)findViewById(R.id.fix_guide);
        statusGuide[2] = (TextView)findViewById(R.id.delivery_guide);
        RequestParams params = new RequestParams();
        Log.i("!!!!!!!!!!!!",loginPreferences.getString("id", "whatshoe")+" "+orderPreferences.getString("orderTime", "0")+ " "+orderPreferences.getString("orderCode", "0"));
        params.put("id", loginPreferences.getString("id", "whatshoe"));
        params.put("order_time", orderPreferences.getString("orderTime", "0"));
        params.put("order_code", orderPreferences.getString("orderCode", "0"));
        HttpClient.post("member/android_get_orderstatus.php", params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2,
                                  Throwable arg3) {
                Log.i("!!!!!!!!!!!!!",arg2);
                Toast.makeText(context, "인터넷 접속상태를 확인해 주세요.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, String arg2) {
                Log.i("!!!!!!!!!!!!!",arg2);
                if (arg2.trim().equals("\uFEFFfail")) {
                    orderPreferences.edit().clear().commit();
                } else if (arg2.trim().equals("\uFEFF0")) {
                    turnOnStatus(0);
                } else if (arg2.trim().equals("\uFEFF1")) {
                    turnOnStatus(1);
                } else if (arg2.trim().equals("\uFEFF2")) {
                    turnOnStatus(2);
                } else if (arg2.trim().equals("\uFEFF3")) {
                    orderPreferences.edit().clear().apply();
                } else {
                    orderPreferences.edit().clear().apply();
                }
            }
        });
    }

    private void turnOnStatus(int value) {
        statusIcons[0].setImageResource(R.drawable.myshoe_pickup);
        statusIcons[1].setImageResource(R.drawable.myshoe_fix);
        statusIcons[2].setImageResource(R.drawable.myshoe_delivery);
        statusTitles[0].setTextColor(Color.argb(80, 255, 255, 255));
        statusTitles[1].setTextColor(Color.argb(80, 255, 255, 255));
        statusTitles[2].setTextColor(Color.argb(80, 255, 255, 255));
        statusGuide[0].setTextColor(Color.argb(80, 255, 255, 255));
        statusGuide[1].setTextColor(Color.argb(80, 255, 255, 255));
        statusGuide[2].setTextColor(Color.argb(80, 255, 255, 255));
        switch (value) {
            case 0:
                statusIcons[0].setImageResource(R.drawable.myshoe_pickup_check);
                statusTitles[0].setTextColor(Color.argb(150, 255, 255, 255));
                statusGuide[0].setTextColor(Color.argb(150, 255, 255, 255));
                break;
            case 1:
                statusIcons[1].setImageResource(R.drawable.myshoe_fix_check);
                statusTitles[1].setTextColor(Color.argb(150, 255, 255, 255));
                statusGuide[1].setTextColor(Color.argb(150, 255, 255, 255));
                break;
            case 2:
                statusIcons[2].setImageResource(R.drawable.myshoe_delivery_check);
                statusTitles[2].setTextColor(Color.argb(150, 255, 255, 255));
                statusGuide[2].setTextColor(Color.argb(150, 255, 255, 255));
                break;
        }
    }

    private void pushStatus(int status) {
        RequestParams params = new RequestParams();
        params.put("id", loginPreferences.getString("id", "whatshoe"));
        params.put("order_time", orderPreferences.getString("orderTime", "0"));
        params.put("order_code", orderPreferences.getString("orderCode", "0"));
        params.put("order_state", status);
        HttpClient.post("member/android_get_orderupdate.php", params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2,
                                  Throwable arg3) {
                Toast.makeText(context, "인터넷 접속상태를 확인해 주세요.",
                        Toast.LENGTH_SHORT).show();
                Log.i("PostingFailed", arg2);
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, String arg2) {
                if (arg2.trim().equals("\uFEFFfail")) {
                    Toast.makeText(context, "취소에 실패했습니다. 고객센터로 문의해 주세요.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "주문이 취소되었습니다.",
                            Toast.LENGTH_SHORT).show();
                    orderPreferences.edit().clear().apply();
                }
            }
        });
    }
    private void changeGuideText(int status){

        if(status == 0) {
            statusGuide[0].setText("약 30min");
            statusGuide[1].setText("약 60min");
            statusGuide[2].setText("약 30min");
        } else {
            statusGuide[0].setText("오후 2시 이전\n당일 픽업");
            statusGuide[1].setText("약 3일 소요");
            statusGuide[2].setText("약 1일 소요");
        }
    }

    public void close() {
        this.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.myshoe_close:
                close();
                break;
            case R.id.pickup_cancle_btn:
                try {
                    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                    Date orderDate = transFormat.parse(orderPreferences.getString("orderTime", "2015-10-1 10:10:10"));
                    Long i = orderDate.getTime() + 900 * 1000;
                    if (new Date().getTime() > i) {
                        Toast.makeText(context, "주문 후 15분 내에만 취소 할 수 있습니다.",
                                Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        pushStatus(4);
                        close();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "취소에 실패했습니다. 고객센터로 문의해 주세요.",
                            Toast.LENGTH_SHORT).show();

                }

                break;
        }
    }
}


