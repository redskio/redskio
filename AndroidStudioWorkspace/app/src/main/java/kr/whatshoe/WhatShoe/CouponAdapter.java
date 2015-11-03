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
public class CouponAdapter extends ArrayAdapter<Coupon> {
    private ArrayList<Coupon> list;
    private int layout;
    LayoutInflater inflater;
    Context context;
    int position;


    public CouponAdapter(Context context, int layout,
                         ArrayList<Coupon> objects) {
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
    public Coupon getItem(int position) {
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
        Coupon coupon = list.get(position);
        TextView priceText = (TextView)convertView.findViewById(R.id.price_text);
        priceText.setText(coupon.getPrice() + "원");
        TextView descText = (TextView)convertView.findViewById(R.id.desc_text);
        switch(Integer.parseInt(coupon.getDesc()))
        {
            case 1:
                descText.setText("과장님 충성 쿠폰");
                break;
            case 2:
                descText.setText("내 남친 불광 쿠폰");
                break;
            case 3:
                descText.setText("여친 뒷굽 쓰담 쿠폰");
                break;
            case 4:
                descText.setText("생일 축하 쿠폰");
                break;
        }

        TextView couponStatus = (TextView)convertView.findViewById(R.id.coupon_status_text);
        if(coupon.getStatus() == 1){
            couponStatus.setText("사용 전");
        } else {
            couponStatus.setText("사용\n완료");
        }
        return convertView;
    }
}
