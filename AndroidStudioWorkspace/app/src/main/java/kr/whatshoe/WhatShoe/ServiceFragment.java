package kr.whatshoe.whatShoe;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import java.util.ArrayList;

import kr.whatshoe.Order.FixOrder;


/**
 * A placeholder fragment containing a simple view.
 */
public class ServiceFragment extends Fragment implements View.OnClickListener {
    ArrayList<FixOrder> orderlist = new ArrayList<FixOrder>();

    private ImageButton manBtn;
    private ImageButton womanBtn;
    private SharedPreferences loginPreferences;

    public ServiceFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.show();


        View rootView = inflater.inflate(R.layout.fragment_service, container, false);
        manBtn = (ImageButton) rootView.findViewById(R.id.manBtn);
        manBtn.setOnClickListener(this);
        womanBtn = (ImageButton) rootView.findViewById(R.id.womanBtn);
        womanBtn.setOnClickListener(this);


        ImageButton cancelBtn = (ImageButton) rootView.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(this);
        Button giftBtn = (Button) rootView.findViewById(R.id.servicepage_gift_btn);
        giftBtn.setOnClickListener(this);
        ImageButton premiumWoman = (ImageButton) rootView.findViewById(R.id.premium_woman_btn);
        premiumWoman.setOnClickListener(this);
        ImageButton premiumMan = (ImageButton) rootView.findViewById(R.id.premium_man_btn);
        premiumMan.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        loginPreferences = getActivity().getSharedPreferences("login_pref", 0);
        MainActivity.currentFragment = MainActivity.FRAGMENT_FLAG_SERVICEORDER;
        if (!MainActivity.needLogin) {
            if (!loginPreferences.contains("id")) {
                Intent loginIntent = new Intent();
                loginIntent.setClass(getActivity(), LoginActivity.class);
                startActivity(loginIntent);
                Log.i("notLogin", "notLogin");
            }
        }
        super.onResume();
    }

    private void DialogChoiceMenu(Context context, int sex, final boolean premium) {
        Dialog dialog = null;
        if (premium) {
                dialog = new WhatShoePremiumDialog(context, "구두 수선", "선택 완료", orderlist, sex);
        } else {
            dialog = new WhatShoeDialog(context, "구두 수선", "선택 완료", orderlist, sex);
        }
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                if (isEmptyOrder(orderlist)) {
                    if (null == getActivity()) {

                    } else {
                        Toast.makeText(getActivity(), "현재 선택된 주문이 없습니다. 주문을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                MapFragment fragment = new MapFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("order", orderlist);
                bundle.putBoolean("premium",premium);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.whatShoe.R.id.container, fragment).commit();
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.manBtn:

                manBtn.setImageResource(R.drawable.servicepage_man_push);
                womanBtn.setImageResource(R.drawable.servicepage_woman);
                DialogChoiceMenu(getActivity(), WhatShoeDialog.MALE, false);
                break;
            case R.id.womanBtn:
                manBtn.setImageResource(R.drawable.servicepage_man);
                womanBtn.setImageResource(R.drawable.servicepage_woman_push);
                DialogChoiceMenu(getActivity(), WhatShoeDialog.FEMALE, false);
                break;
            case R.id.cancel_btn:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new ContentFragment()).commit();
                break;
            case R.id.servicepage_gift_btn:
                MainActivity.currentFragment = MainActivity.FRAGMENT_FLAG_SERVICEORDER;
                intent = new Intent();
                intent.setClass(getActivity(), DrawerActivity.class);
                intent.putExtra("Fragment", DrawerActivity.GIFT_FRAGMENT_TYPE);
                startActivity(intent);
                break;
            case R.id.premium_woman_btn:
                DialogChoiceMenu(getActivity(), WhatShoePremiumDialog.FEMALE, true);
                break;
            case R.id.premium_man_btn:
                DialogChoiceMenu(getActivity(), WhatShoePremiumDialog.MALE, true);
                break;
            default:
                break;
        }
    }


    private boolean isEmptyOrder(ArrayList<FixOrder> array) {
        if (array == null) {
            return true;
        }
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getIsChecked() > 0) {
                return false;
            }

        }
        return true;
    }
}
