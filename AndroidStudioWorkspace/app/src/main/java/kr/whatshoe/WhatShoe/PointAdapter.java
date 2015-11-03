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
public class PointAdapter extends ArrayAdapter<Point> {
    private ArrayList<Point> list;
    private int resource;
    LayoutInflater inflater;

    public PointAdapter(Context context, int resource,
                        ArrayList<Point> objects) {
        super(context, resource, objects);
        this.list = objects;
        this.resource = resource;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Point getItem(int position) {
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
        TextView title = (TextView)convertView.findViewById(R.id.title_text);
        TextView price = (TextView)convertView.findViewById(R.id.desc_text);
        TextView date  = (TextView)convertView.findViewById(R.id.date_text);
        return convertView;
    }
}
