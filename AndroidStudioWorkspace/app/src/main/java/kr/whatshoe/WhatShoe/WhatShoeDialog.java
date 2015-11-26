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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.whatshoe.Order.FixOrder;
import kr.whatshoe.Order.NormalOrderLayout;

/**
 * Created by jaewoo on 2015-08-20.
 */
public class WhatShoeDialog extends Dialog {
    final static int MALE = 1;
    final static int FEMALE = 2;
    private int sex = 0;
    private TextView mTotalTextView;
    private ArrayList<FixOrder> orderList;
    private ArrayList<NormalOrderLayout> orderLayoutList;
    private String mText;
    private Button mButton;
    private Context context;
    private int ETC = 0;
    private final int SOCKS = 5;
    private final int MAN_ETC = 1; //1건의 특수주문
    private final int STOCKING = 6;
    private final int BAND = 7;
    private final int WOMAN_ETC = 2; //2건의 특수주문


    public WhatShoeDialog(Context context) {
        // Dialog 배경을 투명 처리 해준다.
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        orderList = new ArrayList<FixOrder>();
        this.context = context;
    }


    public WhatShoeDialog(Context context, String title, String text, ArrayList<FixOrder> list, int sex) {
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
        setDialogContentView();
        setLayout();
    }

    private void setOrderData() {
        Resources resource = context.getResources();
        if (resource != null) {
            if (orderList != null && orderList.size() > 0)
                orderList.clear();
            if (sex == MALE) {
                String[] ordernameForman = resource.getStringArray(R.array.order_man_title);
                int[] orderpriceForman = resource.getIntArray(R.array.order_man_price);
                for (int i = 0; i < ordernameForman.length; i++) {
                    FixOrder fixorder = new FixOrder(ordernameForman[i], 0, orderpriceForman[i], "", sex, i);
                    orderList.add(fixorder);
                }
            } else if (sex == FEMALE) {
                String[] ordernameForworman = resource.getStringArray(R.array.order_woman_title);
                int[] orderpriceForworman = resource.getIntArray(R.array.order_woman_price);
                for (int i = 0; i < ordernameForworman.length; i++) {
                    FixOrder fixorder = new FixOrder(ordernameForworman[i], 0, orderpriceForworman[i], "", sex, i);
                    orderList.add(fixorder);
                }
            }
        }
    }

    private void setLayout() {
        ImageButton cancleBtn = (ImageButton) findViewById(kr.whatshoe.whatShoe.R.id.dialog_cancel_btn);
        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < orderList.size(); i++) {
                    orderList.get(i).setIsChecked(0);
                }
                close();
            }
        });
        mButton = (Button) findViewById(R.id.set_btn);
        mButton.setText("선택 완료");

        setBtnClickListener();
    }

    private void setDialogContentView() {

        if (sex == MALE) {
            setContentView(R.layout.whatshoe_dialog);

        } else if (sex == FEMALE) {
            setContentView(R.layout.whatshoe_dialog_female);
        }
        initializeOrderLayout();
        refreshTotalPrice();
    }


    private void initializeOrderLayout() {
        if (sex == MALE) {
            orderLayoutList = new ArrayList<NormalOrderLayout>();
            orderLayoutList.add(0, new NormalOrderLayout((RelativeLayout) findViewById(R.id.afterOrder_layout), (CheckBox) findViewById(R.id.afterOrder_check), orderList.get(0), (ImageView) findViewById(R.id.after_order_img)));
            orderLayoutList.add(1, new NormalOrderLayout((RelativeLayout) findViewById(R.id.forman_wash_layout), (CheckBox) findViewById(R.id.forman_wash_check), orderList.get(1), (ImageView) findViewById(R.id.forman_wash_img)));
            orderLayoutList.add(2, new NormalOrderLayout((RelativeLayout) findViewById(R.id.forman_half_layout), (CheckBox) findViewById(R.id.forman_half_check), orderList.get(2), (ImageView) findViewById(R.id.forman_half_img)));
            orderLayoutList.add(3, new NormalOrderLayout((RelativeLayout) findViewById(R.id.forman_sub_layout), (CheckBox) findViewById(R.id.forman_sub_check), orderList.get(3), (ImageView) findViewById(R.id.forman_sub_img)));
            orderLayoutList.add(4, new NormalOrderLayout((RelativeLayout) findViewById(R.id.forman_whole_layout), (CheckBox) findViewById(R.id.forman_whole_check), orderList.get(4), (ImageView) findViewById(R.id.forman_whole_img)));
            orderLayoutList.add(5, new NormalOrderLayout((RelativeLayout) findViewById(R.id.forman_socks_layout), (CheckBox) findViewById(R.id.forman_socks_check), orderList.get(5), (ImageView) findViewById(R.id.forman_socks)));

        } else if (sex == FEMALE) {
            orderLayoutList = new ArrayList<NormalOrderLayout>();
            orderLayoutList.add(0, new NormalOrderLayout((RelativeLayout) findViewById(R.id.afterOrder_layout), (CheckBox) findViewById(R.id.afterOrder_check), orderList.get(0), (ImageView) findViewById(R.id.after_order_img)));
            orderLayoutList.add(1, new NormalOrderLayout((RelativeLayout) findViewById(R.id.forwoman_wash_layout), (CheckBox) findViewById(R.id.forwoman_wash_check), orderList.get(1), (ImageView) findViewById(R.id.forwoman_wash_img)));
            orderLayoutList.add(2, new NormalOrderLayout((RelativeLayout) findViewById(R.id.forwoman_sub_layout), (CheckBox) findViewById(R.id.forwoman_sub_check), orderList.get(2), (ImageView) findViewById(R.id.forwoman_sub_img)));
            orderLayoutList.add(3, new NormalOrderLayout((RelativeLayout) findViewById(R.id.forwoman_submidbig_layout), (CheckBox) findViewById(R.id.forwoman_submidbig_check), orderList.get(3), (ImageView) findViewById(R.id.forwoman_midbig_img)));
            orderLayoutList.add(4, new NormalOrderLayout((RelativeLayout) findViewById(R.id.forwoman_half_layout), (CheckBox) findViewById(R.id.forwoman_half_check), orderList.get(4), (ImageView) findViewById(R.id.forwoman_half_img)));
            orderLayoutList.add(5, new NormalOrderLayout((RelativeLayout) findViewById(R.id.forwoman_boots_layout), (CheckBox) findViewById(R.id.forwoman_boots_check), orderList.get(5), (ImageView) findViewById(R.id.forwoman_boots_img)));
            orderLayoutList.add(6, new NormalOrderLayout((RelativeLayout) findViewById(R.id.forwoman_stocking_layout), (CheckBox) findViewById(R.id.forwoman_stocking_check), orderList.get(6), (ImageView) findViewById(R.id.forwoman_stocking)));
            orderLayoutList.add(7, new NormalOrderLayout((RelativeLayout) findViewById(R.id.forwoman_band_layout), (CheckBox) findViewById(R.id.forwoman_band_check), orderList.get(7), (ImageView) findViewById(R.id.forwoman_band)));

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
            etcOrder += orderList.get(SOCKS).getIsChecked();
        } else {
            ETC = WOMAN_ETC;
            etcOrder += orderList.get(STOCKING).getIsChecked() + orderList.get(BAND).getIsChecked();
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


