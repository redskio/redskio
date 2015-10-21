package kr.whatshoe.WhatShoe;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import kr.whatshoe.Util.WhatshoeDbHelper;


/**
 * A placeholder fragment containing a simple view.
 */
public class CouponFragment extends Fragment implements View.OnClickListener {
    private ArrayList<Coupon> array;
    private CouponAdapter adapter;
    private LinearLayout emptyView;
    public CouponFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_coupon, container, false);
        array = new ArrayList<Coupon>();
        Button couponBtn = (Button) rootView.findViewById(R.id.couponBtn);
        couponBtn.setOnClickListener(this);

        ListView listView = (ListView)rootView.findViewById(R.id.couponList);
        adapter = new CouponAdapter(getActivity(),R.layout.coupon_item,array);
        listView.setAdapter(adapter);
        emptyView = (LinearLayout)rootView.findViewById(R.id.empty_view);
        return rootView;
    }

    @Override
    public void onResume() {
        requestDBUpdate();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.couponBtn:
                final CouponDialog dialog = new CouponDialog(getActivity());
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestDBUpdate();
                    }
                });
                dialog.show();
                break;
        }
    }
    private void requestDBUpdate(){
        WhatshoeDbHelper dbHelper = new WhatshoeDbHelper(getActivity());
        dbHelper.open();
        Cursor cursor = dbHelper.fetchAllCoupons();
        array.clear();
        while (cursor.moveToNext()) {
            array.add(new Coupon(cursor.getString(1), "", Integer.parseInt(cursor.getString(3)), "", Integer.parseInt(cursor.getString(4))));
            //1 : desc, 2: date , 3 : price 4 : status
        }
        dbHelper.close();
        if(array.isEmpty()){
            emptyView.setVisibility(View.VISIBLE);
        }else {
            emptyView.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }
}
