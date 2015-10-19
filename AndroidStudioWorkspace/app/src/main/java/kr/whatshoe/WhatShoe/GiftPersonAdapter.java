package kr.whatshoe.WhatShoe;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by jaewoo on 2015-08-20.
 */
public class GiftPersonAdapter extends ArrayAdapter {
    private ArrayList<GiftPerson> list;
    private int layout;
    LayoutInflater inflater;
    Context context;
    int position;


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
    public View getView(final int position, View convertView, ViewGroup parent) {
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


        final Spinner spinner = (Spinner) convertView.findViewById(R.id.spinner);
        Integer[] much = {1,2,3,4,5};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(
        context, android.R.layout.simple_spinner_item, much);
        spinner.setAdapter(adapter);
             spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                 @Override
                 public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                     person.setNum((Integer) spinner.getSelectedItem());
                 }

                 @Override
                 public void onNothingSelected(AdapterView<?> parent) {

                 }
             });
        return convertView;
    }
}
