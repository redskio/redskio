package kr.whatshoe.WhatShoe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class GiftFragment extends Fragment implements View.OnClickListener {

    public GiftFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((DrawerActivity)getActivity()).type = DrawerActivity.GIFT_FRAGMENT_TYPE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gift, container, false);
        ImageButton gift1 = (ImageButton) rootView.findViewById(R.id.gift1);
        ImageButton gift2 = (ImageButton) rootView.findViewById(R.id.gift2);
        ImageButton gift3 = (ImageButton) rootView.findViewById(R.id.gift3);
        ImageButton gift4 = (ImageButton) rootView.findViewById(R.id.gift4);
        gift1.setOnClickListener(this);
        gift2.setOnClickListener(this);
        gift3.setOnClickListener(this);
        gift4.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        GiftDetailFragment fragment = new GiftDetailFragment();

        switch (v.getId()) {
            case R.id.gift1:
                bundle.putInt("type", 1);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.WhatShoe.R.id.container, fragment).commit();
                break;
            case R.id.gift2:
                bundle.putInt("type", 2);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.WhatShoe.R.id.container, fragment).commit();
                break;
            case R.id.gift3:
                bundle.putInt("type", 3);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.WhatShoe.R.id.container, fragment).commit();
                break;
            case R.id.gift4:
                bundle.putInt("type", 4);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.WhatShoe.R.id.container, fragment).commit();
                break;
        }
    }

}
