package kr.whatshoe.WhatShoe;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jaewoo on 2015-08-20.
 */
public class WhatShoeDialog extends Dialog {
    final static int MALE = 1;
    final static int FEMALE = 2;
    private int sex = 0;
    private TextView mTotalTextView;
    private ArrayList<FixOrder> orderList;
    private String mText;
    private Button mButton;
    private Context context;
    private CheckBox[] checkBox;
    private ImageView[] shoeImg;
    private View.OnTouchListener layoutTouchListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.8f;
        getWindow().setAttributes(params);
        setDialogContentView(sex);
        setLayout();

    }

    private void setDialogContentView(int sex) {
        initializeTouchEvent();

        if (sex == MALE) {
            setContentView(R.layout.whatshoe_dialog);
            initializeCheckBox();
            initializeImageView();
            RelativeLayout wash_layout = (RelativeLayout) findViewById(R.id.forman_wash_layout);
            wash_layout.setOnTouchListener(layoutTouchListner);
            RelativeLayout half_layout = (RelativeLayout) findViewById(R.id.forman_half_layout);
            half_layout.setOnTouchListener(layoutTouchListner);
            RelativeLayout sub_layout = (RelativeLayout) findViewById(R.id.forman_sub_layout);
            sub_layout.setOnTouchListener(layoutTouchListner);
            RelativeLayout whole_layout = (RelativeLayout) findViewById(R.id.forman_whole_layout);
            whole_layout.setOnTouchListener(layoutTouchListner);
            setDefaultLayoutColor(wash_layout, orderList.get(0));
            setDefaultLayoutColor(whole_layout, orderList.get(1));
            setDefaultLayoutColor(half_layout, orderList.get(2));
            setDefaultLayoutColor(sub_layout, orderList.get(3));
        } else if (sex == FEMALE) {
            setContentView(R.layout.whatshoe_dialog_female);
            initializeCheckBox();
            initializeImageView();
            RelativeLayout wash_layout = (RelativeLayout) findViewById(R.id.forwoman_wash_layout);
            wash_layout.setOnTouchListener(layoutTouchListner);
            RelativeLayout sub_layout = (RelativeLayout) findViewById(R.id.forwoman_sub_layout);
            sub_layout.setOnTouchListener(layoutTouchListner);
            RelativeLayout submid_layout = (RelativeLayout) findViewById(R.id.forwoman_submid_layout);
            submid_layout.setOnTouchListener(layoutTouchListner);
            RelativeLayout subbig_layout = (RelativeLayout) findViewById(R.id.forwoman_subbig_layout);
            subbig_layout.setOnTouchListener(layoutTouchListner);
            setDefaultLayoutColor(wash_layout, orderList.get(4));
            setDefaultLayoutColor(sub_layout, orderList.get(5));
            setDefaultLayoutColor(submid_layout, orderList.get(6));
            setDefaultLayoutColor(subbig_layout, orderList.get(7));
        }

    }
    private void changeLayoutColor(View v, int index) {
        FixOrder order = orderList.get(index);
        if (order.getIsChecked() == 0) {
            v.setBackgroundResource(R.color.color_login_default);
            order.setIsChecked(1);
            checkBox[index].setChecked(true);
            shoeImg[index].setVisibility(View.VISIBLE);
        } else {
            v.setBackgroundColor(Color.rgb(255,255,255));
            order.setIsChecked(0);
            checkBox[index].setChecked(false);
            shoeImg[index].setVisibility(View.GONE);
        }
    }

    private void setDefaultLayoutColor(View v, FixOrder order) {
        if (order.getIsChecked() > 0) {
            v.setBackgroundResource(R.color.color_login_default);

        }
    }
    private void initializeTouchEvent(){
        layoutTouchListner = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    switch (v.getId()) {
                        case R.id.forman_wash_layout:
                            changeLayoutColor(v, 0);
                            refreshTotalPrice();
                            break;
                        case R.id.forman_whole_layout:
                            changeLayoutColor(v, 1);
                            refreshTotalPrice();
                            break;
                        case R.id.forman_half_layout:
                            changeLayoutColor(v, 2);
                            refreshTotalPrice();
                            break;
                        case R.id.forman_sub_layout:
                            changeLayoutColor(v, 3);
                            refreshTotalPrice();
                            break;
                        case R.id.forwoman_wash_layout:
                            changeLayoutColor(v, 4);
                            refreshTotalPrice();
                            break;
                        case R.id.forwoman_sub_layout:
                            changeLayoutColor(v, 5);
                            refreshTotalPrice();
                            break;
                        case R.id.forwoman_submid_layout:
                            changeLayoutColor(v, 6);
                            refreshTotalPrice();
                            break;
                        case R.id.forwoman_subbig_layout:
                            changeLayoutColor(v, 7);
                            refreshTotalPrice();
                            break;
                    }

                }
                return false;
            }
        };
    }
    private void initializeCheckBox(){
        checkBox = new CheckBox[8];
        checkBox[0] = (CheckBox)findViewById(R.id.forman_wash_check);
        checkBox[1] = (CheckBox)findViewById(R.id.forman_whole_check);
        checkBox[2] = (CheckBox)findViewById(R.id.forman_half_check);
        checkBox[3] = (CheckBox)findViewById(R.id.forman_sub_check);
        checkBox[4] = (CheckBox)findViewById(R.id.forwoman_wash_check);
        checkBox[5] = (CheckBox)findViewById(R.id.forwoman_sub_check);
        checkBox[6] = (CheckBox)findViewById(R.id.forwoman_submid_check);
        checkBox[7] = (CheckBox)findViewById(R.id.forwoman_subbig_check);
        for (int i= 0 ; i<checkBox.length;i++){
            if(orderList.get(i).getIsChecked()>0){
                checkBox[i].setChecked(true);
            }
        }
    }
    private void initializeImageView(){
        shoeImg = new ImageView[8];
        shoeImg[0] =(ImageView)findViewById(R.id.forman_wash_img);
        shoeImg[1]=(ImageView)findViewById(R.id.forman_whole_img);
        shoeImg[2]=(ImageView)findViewById(R.id.forman_half_img);
        shoeImg[3]=(ImageView)findViewById(R.id.forman_sub_img);
        shoeImg[4]=(ImageView)findViewById(R.id.forwoman_wash_img);
        shoeImg[5]=(ImageView)findViewById(R.id.forwoman_sub_img);
        shoeImg[6]=(ImageView)findViewById(R.id.forwoman_submid_img);
        shoeImg[7]=(ImageView)findViewById(R.id.forwoman_subbig_img);
        for (int i= 0 ; i<shoeImg.length;i++){
            if(orderList.get(i).getIsChecked()>0){
                shoeImg[i].setVisibility(View.VISIBLE);
            }
        }
        refreshTotalPrice();
    }
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


    private void setBtnClickListener() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void setLayout() {
        ImageButton cancleBtn = (ImageButton) findViewById(kr.whatshoe.WhatShoe.R.id.dialog_cancel_btn);
        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        for (int i= 0 ; i<orderList.size();i++){
            if(orderList.get(i).getIsChecked()>0){
                totalPrice+=orderList.get(i).getPrice();
            }
        }
        mTotalTextView.setText(totalPrice + " 원");
    }

    public void close() {
        this.dismiss();
    }

    private void makeOrder() {
//        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid_layout);
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Application.LAYOUT_INFLATER_SERVICE);
//        for (FixOrder order : orderList) {
//            View orderView = inflater.inflate(R.layout.dialog_order_item, gridLayout, false);
//            LayoutParams params = orderView.getLayoutParams();
//            params.width = gridLayout.getWidth() / 2;
//            TextView orderTitle = (TextView) orderView.findViewById(R.id.dialog_sub_text);
//            orderTitle.setText(order.dgetContentText());
//            orderView.setLayoutParams(params);
//            gridLayout.addView(orderView);
//        }
    }
}


