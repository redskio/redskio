package kr.whatshoe.whatShoe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jaewoo on 2015-08-20.
 */
public class HistoryAdapter extends ArrayAdapter<FixOrder> {
    private ArrayList<FixOrder> list;
    private int resource;
    LayoutInflater inflater;
    int position;

    public HistoryAdapter(Context context, int resource,
                          ArrayList<FixOrder> objects) {
        super(context, resource, objects);
        this.list = objects;
        this.resource = resource;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(list!=null) {
            return list.size();
        } else return 0;
    }

    @Override
    public FixOrder getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
        }

        TextView contentText = (TextView) convertView.findViewById(R.id.title_text);
        contentText.setText(list.get(position).getContentText());
        TextView timeText = (TextView) convertView.findViewById(R.id.time_text);
        timeText.setText(list.get(position).getTime());
        TextView priceText = (TextView) convertView.findViewById(R.id.price_text);
        priceText.setText(list.get(position).getPrice() + " 원");
        return convertView;
    }
}
