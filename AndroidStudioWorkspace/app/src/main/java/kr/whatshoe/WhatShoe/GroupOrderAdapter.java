package kr.whatshoe.whatShoe;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import java.util.ArrayList;

/**
 * Created by jaewoo on 2015-08-20.
 */
public class GroupOrderAdapter extends ArrayAdapter<FixOrder> {
    private ArrayList<FixOrder> list;
    private ArrayList<Integer> washOrder;
    private int layout;
    LayoutInflater inflater;
    Context context;
    int position;


    public GroupOrderAdapter(Context context, int layout,
                             ArrayList<FixOrder> objects) {
        super(context, layout, objects);
        this.context = context;
        this.list = objects;
        this.layout = layout;
        washOrder = new ArrayList<Integer>();

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public FixOrder getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<FixOrder> getOrderList() {
        int totalWash = 0;
        for (int i = 0; i < washOrder.size(); i++) {
                totalWash+= washOrder.get(i);
        }


        return list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }
        ImageButton button = (ImageButton) convertView.findViewById(kr.whatshoe.whatShoe.R.id.imageButton2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ArrayList<FixOrder> arraylist = new ArrayList<FixOrder>();
//                Resources resources = context.getResources();
//                if (resources != null) {
//                    String[] fixItem = resources.getStringArray(kr.whatshoe.WhatShoe.R.array.fixitem);
//                    int[] priceItem = resources.getIntArray(kr.whatshoe.WhatShoe.R.array.priceArray);
//                    for (int i = 0; i < fixItem.length; i++) {
//                        arraylist.add(new FixOrder(fixItem[i], 0, priceItem[i]));
//                    }
//                }
//                WhatShoeAdapter adapter = new WhatShoeAdapter(context, kr.whatshoe.WhatShoe.R.layout.dialog_item, arraylist);
                final WhatShoeDialog dialog = new WhatShoeDialog(context, "구두 수선", "선택 완료", arraylist,WhatShoeDialog.MALE);
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        for (int i = 0; i < list.size(); i++) {
                            int num = arraylist.get(i).getIsChecked();
                            if (num > 0) {
                                list.get(i).setIsChecked(num + 1);
                            }
                        }
                    }
                });
            }
        });
        washOrder.add(position, 0);
        ImageButton button1 = (ImageButton) convertView.findViewById(kr.whatshoe.whatShoe.R.id.imageButton);
        button1.setOnClickListener(new View.OnClickListener() {
            int order = washOrder.get(position);

            @Override
            public void onClick(View v) {
                if (order > 0) {
                    ((ImageButton) v).setImageResource(kr.whatshoe.whatShoe.R.drawable.servicepage_woman);
                    order = 0;
                } else {
                    ((ImageButton) v).setImageResource(kr.whatshoe.whatShoe.R.drawable.servicepage_woman_push);
                    order = 1;
                }
            }
        });

        return convertView;
    }
}
