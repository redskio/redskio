package kr.whatshoe.whatShoe;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.whatshoe.Order.FixOrder;
import kr.whatshoe.Order.OrderLayout;

/**
 * Created by jaewoo on 2015-08-20.
 */
public class WhatShoePremiumDialog extends Dialog {
    final static int MALE = 3;
    final static int FEMALE = 4;
    private int sex = 0;
    private TextView mTotalTextView;
    private ArrayList<FixOrder> orderList;
    private ArrayList<OrderLayout> orderLayoutList;
    private String mText;
    private Button mButton;
    private Context context;
    private int ETC = 0;
    private final int SOCKS = 14;
    private final int TABACCO_MAN = 15;
    private final int LIGHTER_MAN = 16;
    private final int MAN_ETC = 3; //1건의 특수주문
    private final int STOCKING = 14;
    private final int BAND = 15;
    private final int TABACCO_WOMAN = 16;
    private final int LIGHTER_WOMAN = 17;
    private final int WOMAN_ETC = 4; //2건의 특수주문

    public WhatShoePremiumDialog(Context context) {
        // Dialog 배경을 투명 처리 해준다.
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        orderList = new ArrayList<FixOrder>();
        this.context = context;
    }

    public WhatShoePremiumDialog(Context context, String title, String text, ArrayList<FixOrder> list, int sex) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mText = text;
        this.sex = sex;
        this.orderList = list;
        this.context = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.8f;
        getWindow().setAttributes(params);
        setOrderData();
        setDialogContentView(sex);
        setLayout();

    }
    private void setOrderData() {
        Resources resource = context.getResources();
        if (resource != null) {
            if (orderList != null && orderList.size() > 0)
                orderList.clear();
            if (sex == MALE) {
                String[] ordernameForman = resource.getStringArray(R.array.premium_man_title);
                int[] orderpriceForman = resource.getIntArray(R.array.premium_man_price);
                for (int i = 0; i < ordernameForman.length; i++) {
                    FixOrder fixorder = new FixOrder(ordernameForman[i], 0, orderpriceForman[i], "", sex, i);
                    orderList.add(fixorder);
                }
            } else if (sex == FEMALE) {
                String[] ordernameForworman = resource.getStringArray(R.array.premium_woman_title);
                int[] orderpriceForworman = resource.getIntArray(R.array.premium_woman_price);
                for (int i = 0; i < ordernameForworman.length; i++) {
                    FixOrder fixorder = new FixOrder(ordernameForworman[i], 0, orderpriceForworman[i], "", sex, i);
                    orderList.add(fixorder);
                }
            }
        }
    }
    private void setDialogContentView(int sex) {

        if (sex == MALE) {
            setContentView(R.layout.whatshoe_premium_dialog);

        } else if (sex == FEMALE) {
            setContentView(R.layout.whatshoe_premium_dialog_female);
        }
        initializeOrderLayout();
        refreshTotalPrice();

    }
    private void initializeOrderLayout() {
        if (sex == MALE) {
            orderLayoutList = new ArrayList<OrderLayout>();
            orderLayoutList.add(0, new OrderLayout((RelativeLayout) findViewById(R.id.after_order_layout), (CheckBox) findViewById(R.id.after_order_check), orderList.get(0)));
            orderLayoutList.add(1, new OrderLayout((RelativeLayout) findViewById(R.id.pre_shoe_full_man), (CheckBox) findViewById(R.id.pre_shoe_full_man_check), orderList.get(1)));
            orderLayoutList.add(2, new OrderLayout((RelativeLayout) findViewById(R.id.pre_deepshoecare_man), (CheckBox) findViewById(R.id.pre_deepshoecare_man_check), orderList.get(2)));
            orderLayoutList.add(3, new OrderLayout((RelativeLayout) findViewById(R.id.pre_heel_mid_man), (CheckBox) findViewById(R.id.pre_heel_mid_man_check), orderList.get(3)));
            orderLayoutList.add(4, new OrderLayout((RelativeLayout) findViewById(R.id.pre_heel_big_man), (CheckBox) findViewById(R.id.pre_heel_big_man_check), orderList.get(4)));
            orderLayoutList.add(5, new OrderLayout((RelativeLayout) findViewById(R.id.pre_vibram_heel_man), (CheckBox) findViewById(R.id.pre_vibram_heel_man_check), orderList.get(5)));
            orderLayoutList.add(6, new OrderLayout((RelativeLayout) findViewById(R.id.pre_soles_rubber_man), (CheckBox) findViewById(R.id.pre_soles_rubber_man_check), orderList.get(6)));
            orderLayoutList.add(7, new OrderLayout((RelativeLayout) findViewById(R.id.pre_vibram_soles_man), (CheckBox) findViewById(R.id.pre_vibram_soles_man_check), orderList.get(7)));
            orderLayoutList.add(8, new OrderLayout((RelativeLayout) findViewById(R.id.pre_allsoles_man), (CheckBox) findViewById(R.id.pre_allsoles_man_check), orderList.get(8)));
            orderLayoutList.add(9, new OrderLayout((RelativeLayout) findViewById(R.id.pre_italy_allsoles_man), (CheckBox) findViewById(R.id.pre_italy_allsoles_man_check), orderList.get(9)));
            orderLayoutList.add(10, new OrderLayout((RelativeLayout) findViewById(R.id.pre_eng_allsoles_man), (CheckBox) findViewById(R.id.pre_eng_allsoles_man_check), orderList.get(10)));
            orderLayoutList.add(11, new OrderLayout((RelativeLayout) findViewById(R.id.pre_bond_man), (CheckBox) findViewById(R.id.pre_bond_man_check), orderList.get(11)));
            orderLayoutList.add(12, new OrderLayout((RelativeLayout) findViewById(R.id.pre_color_man), (CheckBox) findViewById(R.id.pre_color_man_check), orderList.get(12)));
            orderLayoutList.add(13, new OrderLayout((RelativeLayout) findViewById(R.id.pre_sewing_man), (CheckBox) findViewById(R.id.pre_sewing_man_check), orderList.get(13)));
            orderLayoutList.add(14, new OrderLayout((RelativeLayout) findViewById(R.id.pre_socks), (CheckBox) findViewById(R.id.pre_socks_man_check), orderList.get(14)));
            orderLayoutList.add(15, new OrderLayout((RelativeLayout) findViewById(R.id.pre_tobacco), (CheckBox) findViewById(R.id.pre_cigarette_check), orderList.get(15)));
            orderLayoutList.add(16, new OrderLayout((RelativeLayout) findViewById(R.id.pre_lighter), (CheckBox) findViewById(R.id.pre_lighter_check), orderList.get(16)));
        } else if (sex == FEMALE) {
            orderLayoutList = new ArrayList<OrderLayout>();
            orderLayoutList.add(0, new OrderLayout((RelativeLayout) findViewById(R.id.after_order_layout), (CheckBox) findViewById(R.id.after_order_check), orderList.get(0)));
            orderLayoutList.add(1, new OrderLayout((RelativeLayout) findViewById(R.id.pre_shoe_full_woman), (CheckBox) findViewById(R.id.pre_shoecare_full_woman_check), orderList.get(1)));
            orderLayoutList.add(2, new OrderLayout((RelativeLayout) findViewById(R.id.pre_deepshoecare_woman), (CheckBox) findViewById(R.id.pre_deepshoecare_woman_check), orderList.get(2)));
            orderLayoutList.add(3, new OrderLayout((RelativeLayout) findViewById(R.id.pre_heel_small_woman), (CheckBox) findViewById(R.id.pre_heel_small_woman_check), orderList.get(3)));
            orderLayoutList.add(4, new OrderLayout((RelativeLayout) findViewById(R.id.pre_heel_mid_woman), (CheckBox) findViewById(R.id.pre_heel_mid_woman_check), orderList.get(4)));
            orderLayoutList.add(5, new OrderLayout((RelativeLayout) findViewById(R.id.pre_heel_big_woman), (CheckBox) findViewById(R.id.pre_heel_big_woman_check), orderList.get(5)));
            orderLayoutList.add(6, new OrderLayout((RelativeLayout) findViewById(R.id.pre_soles_rubber_woman), (CheckBox) findViewById(R.id.pre_soles_rubber_woman_check), orderList.get(6)));
            orderLayoutList.add(7, new OrderLayout((RelativeLayout) findViewById(R.id.pre_vibram_soles_woman), (CheckBox) findViewById(R.id.pre_vibram_soles_woman_check), orderList.get(7)));
            orderLayoutList.add(8, new OrderLayout((RelativeLayout) findViewById(R.id.pre_all_soles), (CheckBox) findViewById(R.id.pre_all_soles_check), orderList.get(8)));
            orderLayoutList.add(9, new OrderLayout((RelativeLayout) findViewById(R.id.pre_heel_woman), (CheckBox) findViewById(R.id.pre_heel_woman_check), orderList.get(9)));
            orderLayoutList.add(10, new OrderLayout((RelativeLayout) findViewById(R.id.pre_in_woman), (CheckBox) findViewById(R.id.pre_in_woman_check), orderList.get(10)));
            orderLayoutList.add(11, new OrderLayout((RelativeLayout) findViewById(R.id.pre_bond_woman), (CheckBox) findViewById(R.id.pre_bond_woman_check), orderList.get(11)));
            orderLayoutList.add(12, new OrderLayout((RelativeLayout) findViewById(R.id.pre_color_woman), (CheckBox) findViewById(R.id.pre_color_woman_check), orderList.get(12)));
            orderLayoutList.add(13, new OrderLayout((RelativeLayout) findViewById(R.id.pre_sewing_woman), (CheckBox) findViewById(R.id.pre_sewing_woman_check), orderList.get(13)));
            orderLayoutList.add(14, new OrderLayout((RelativeLayout) findViewById(R.id.pre_stocking), (CheckBox) findViewById(R.id.pre_stocking_check), orderList.get(14)));
            orderLayoutList.add(15, new OrderLayout((RelativeLayout) findViewById(R.id.pre_band_layout), (CheckBox) findViewById(R.id.pre_band_check), orderList.get(15)));
            orderLayoutList.add(16, new OrderLayout((RelativeLayout) findViewById(R.id.pre_tobacco), (CheckBox) findViewById(R.id.pre_cigarette_check), orderList.get(16)));
            orderLayoutList.add(17, new OrderLayout((RelativeLayout) findViewById(R.id.pre_lighter), (CheckBox) findViewById(R.id.pre_lighter_check), orderList.get(17)));

        }
        for(int i=0; i < orderLayoutList.size();i++){
            CheckBox cb = orderLayoutList.get(i).getCheckBox();
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    refreshTotalPrice();
                }
            });
        }
    }
    private boolean checkOrder() {
        int etcOrder = 0;
        int count = 0;
        if (sex == MALE) {
            ETC = MAN_ETC;
            etcOrder += orderList.get(SOCKS).getIsChecked() +  orderList.get(TABACCO_MAN).getIsChecked() +  orderList.get(LIGHTER_MAN).getIsChecked();
        } else {
            ETC = WOMAN_ETC;
            etcOrder += orderList.get(STOCKING).getIsChecked() + orderList.get(BAND).getIsChecked() +  orderList.get(TABACCO_WOMAN).getIsChecked() +  orderList.get(LIGHTER_WOMAN).getIsChecked();
        }

        for (int i = 0; i < orderList.size() - ETC; i++) {
            count += orderList.get(i).getIsChecked();
        }
        if (count == 0) {
            if (etcOrder > 0) {
                return false;
            }
            return true;
        }
        return true;
    }


    private void setBtnClickListener() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkOrder()) {
                    dismiss();
                } else {
                    Toast.makeText(context, "양말, 스타킹 및 밴드 서비스는 다른 주문과 함께 이용 가능합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setLayout() {
        ImageButton cancleBtn = (ImageButton) findViewById(R.id.dialog_cancel_btn);
        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0 ; i<orderList.size();i++){
                    orderList.get(i).setIsChecked(0);
                }
                close();
            }
        });
        mButton = (Button) findViewById(R.id.set_btn);
        mButton.setText("선택 완료");

        setBtnClickListener();
//        makeOrder();
    }

    private void refreshTotalPrice() {
        int totalPrice = 0;
        mTotalTextView = (TextView) findViewById(R.id.dialogTotalPriceText);
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getIsChecked() > 0) {
                totalPrice += orderList.get(i).getPrice();
            }
        }
        mTotalTextView.setText(totalPrice + " 원");
    }

    public void close() {
        this.dismiss();
    }

}


