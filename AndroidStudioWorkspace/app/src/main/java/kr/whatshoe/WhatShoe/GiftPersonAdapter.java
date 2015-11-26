package kr.whatshoe.whatShoe;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by jaewoo on 2015-08-20.
 */
public class GiftPersonAdapter extends ArrayAdapter {
    private ArrayList<GiftPerson> list;
    private int layout;
    LayoutInflater inflater;
    Context context;



    public GiftPersonAdapter(Context context, int layout,
                             ArrayList<GiftPerson> objects) {
        super(context, layout, objects);
        this.context = context;
        this.list = objects;
        this.layout = layout;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GiftPerson getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }
        final GiftPerson person = list.get(position);
        EditText personText = (EditText) convertView.findViewById(R.id.giftNameText);
        personText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                person.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        EditText phoneText = (EditText) convertView.findViewById(R.id.giftPhoneText);
        phoneText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                person.setPhoneNum(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        personText.setText(person.getName());
        phoneText.setText(person.getPhoneNum());


        Integer[] much = {1,2,3,4,5};
        ImageButton cancleItem = (ImageButton)convertView.findViewById(R.id.cancleItem);
        cancleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
                setListViewHeightBasedOnChildren((ListView) parent);
            }
        });

        return convertView;
    }
    public void setListViewHeightBasedOnChildren(ListView listView) {

        int totalHeight = 0;
        for (int i = 0; i < getCount(); i++) {
            View listItem = getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
