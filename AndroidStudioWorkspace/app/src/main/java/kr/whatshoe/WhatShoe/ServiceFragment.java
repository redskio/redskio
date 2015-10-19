package kr.whatshoe.WhatShoe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class ServiceFragment extends Fragment implements View.OnClickListener {
    private LayoutInflater inflater = null;
    ArrayList<FixOrder> arraylist = new ArrayList<FixOrder>();
    private ImageButton manBtn;
    private ImageButton womanBtn;
    private ImageView[] shoeImg;
    private String locationResult = "";
    private String locationResultDetail = "";
    public ServiceFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.hide();

        View rootView = inflater.inflate(R.layout.fragment_service, container, false);
        this.inflater = inflater;
        locationResult = getArguments().getString("locationResult");
        locationResultDetail = getArguments().getString("locationResultDetail");
        manBtn = (ImageButton) rootView.findViewById(R.id.manBtn);
        manBtn.setOnClickListener(this);
        womanBtn = (ImageButton) rootView.findViewById(R.id.womanBtn);
        womanBtn.setOnClickListener(this);


        Button payBtn = (Button) rootView.findViewById(R.id.payBtn);
        payBtn.setOnClickListener(this);


        ImageButton cancelBtn = (ImageButton) rootView.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(this);

        ImageButton giftBtn = (ImageButton) rootView.findViewById(R.id.servicepage_gift_btn);
        giftBtn.setOnClickListener(this);

        Resources resource = getResources();
        if (resource != null) {
            String[] ordernameForman = resource.getStringArray(R.array.order);
            int[] orderpriceForman = getResources().getIntArray(R.array.price);
            for (int i = 0; i < ordernameForman.length; i++) {
                FixOrder fixorder = new FixOrder(ordernameForman[i], 0, orderpriceForman[i]);
                arraylist.add(fixorder);
            }
        }

        initializeImageView(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        MainActivity.currentFragment = MainActivity.FRAGMENT_FLAG_SERVICEORDER;


        super.onResume();
    }


    private void DialogChoiceMenu(Context context, int sex) {
        final WhatShoeDialog dialog = new WhatShoeDialog(context, "구두 수선", "선택 완료", arraylist, sex);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.i("!!!!!!!!!!!!", "!!!!!!!!!!!!!!!!");
                if (inflater != null) {
                    for (int i = 0; i < arraylist.size(); i++) {
                        if (arraylist.get(i).getIsChecked() > 0) {
                            shoeImg[i].setVisibility(View.VISIBLE);
                        } else {
                            shoeImg[i].setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.manBtn:

                manBtn.setImageResource(R.drawable.servicepage_btn2_push);
                womanBtn.setImageResource(R.drawable.servicepage_btn1);
                for (int i = 4; i < arraylist.size(); i++) {
                    arraylist.get(i).setIsChecked(0);
                }
                DialogChoiceMenu(getActivity(), WhatShoeDialog.MALE);
                break;
            case R.id.womanBtn:

                manBtn.setImageResource(R.drawable.servicepage_btn2);
                womanBtn.setImageResource(R.drawable.servicepage_btn1_push);
                for (int i = 0; i < 4; i++) {
                    arraylist.get(i).setIsChecked(0);
                }
                DialogChoiceMenu(getActivity(), WhatShoeDialog.FEMALE);
                break;
            case R.id.payBtn:
                if (isEmptyOrder(arraylist)) {
                    Toast.makeText(getActivity(), "현재 선택된 주문이 없습니다. 주문을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }
                MainActivity.currentFragment = MainActivity.FRAGMENT_FLAG_CONTENT;
                intent = new Intent();
                intent.setClass(getActivity(), PayActivity.class);
                intent.putExtra("order", arraylist);
                intent.putExtra("locationResult", locationResult);
                intent.putExtra("locationResultDetail", locationResultDetail);
                getActivity().startActivity(intent);
//                }

                break;
            case R.id.cancel_btn:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new MainActivityFragment()).commit();
                break;
            case R.id.servicepage_gift_btn:
                MainActivity.currentFragment = MainActivity.FRAGMENT_FLAG_SERVICE;
                intent = new Intent();
                intent.setClass(getActivity(), DrawerActivity.class);
                intent.putExtra("Fragment", DrawerActivity.GIFT_FRAGMENT_TYPE);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    private void initializeImageView(View rootView) {

        shoeImg = new ImageView[8];
        shoeImg[0] = (ImageView) rootView.findViewById(R.id.main_man_wash_img);
        shoeImg[1] = (ImageView) rootView.findViewById(R.id.main_man_whole_img);
        shoeImg[2] = (ImageView) rootView.findViewById(R.id.main_man_half_img);
        shoeImg[3] = (ImageView) rootView.findViewById(R.id.main_man_sub_img);
        shoeImg[4] = (ImageView) rootView.findViewById(R.id.main_woman_wash_img);
        shoeImg[5] = (ImageView) rootView.findViewById(R.id.main_woman_sub_img);
        shoeImg[6] = (ImageView) rootView.findViewById(R.id.main_woman_submid_img);
        shoeImg[7] = (ImageView) rootView.findViewById(R.id.main_woman_subbig_img);


    }

    private boolean isEmptyOrder(ArrayList<FixOrder> array) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getIsChecked() > 0) {
                return false;
            }

        }
        return true;
    }
}
