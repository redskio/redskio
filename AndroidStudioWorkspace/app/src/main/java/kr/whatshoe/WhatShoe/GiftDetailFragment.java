package kr.whatshoe.WhatShoe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class GiftDetailFragment extends Fragment {

    public GiftDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DrawerActivity.type = DrawerActivity.GIFT_DETAIL_FRAGMENT_TYPE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gift_detail, container, false);
        ImageView couponImage = (ImageView)rootView.findViewById(R.id.coupon_image_detail);
        switch(getArguments().getInt("type"))
        {
            case 1 :
                couponImage.setImageResource(R.drawable.gift1_btn);
                break;
            case 2:
                couponImage.setImageResource(R.drawable.gift1_btn2);
                break;
            case 3:
                couponImage.setImageResource(R.drawable.gift1_btn3);
                break;
            case 4:
                couponImage.setImageResource(R.drawable.gift1_btn4);
                break;
        }

        Button nextStepBtn =(Button)rootView.findViewById(R.id.nextStepBtn);
        nextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GiftDetail2Fragment fragment = new GiftDetail2Fragment();
                Bundle bundle = new Bundle();
                bundle.putInt("type",getArguments().getInt("type"));
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.WhatShoe.R.id.container, fragment).commit();
            }
        });
        return rootView;
    }


}
