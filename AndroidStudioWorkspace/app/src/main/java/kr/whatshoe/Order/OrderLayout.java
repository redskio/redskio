package kr.whatshoe.Order;

import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import kr.whatshoe.whatShoe.R;

/**
 * Created by jaewoo on 2015-08-20.
 */
public class OrderLayout{
    protected RelativeLayout layout;
    protected CheckBox checkBox;
    protected FixOrder fixOrder;

    public OrderLayout(RelativeLayout layout, CheckBox checkBox, FixOrder fixOrder){
        this.layout = layout;
        this.checkBox = checkBox;
        this.fixOrder = fixOrder;
        initLayout();
    }

    public void initLayout(){
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()== MotionEvent.ACTION_DOWN) {
                    checkOrder();
                }
                return false;
            }
        });
    }
    protected void checkOrder() {
        if(fixOrder.getIsChecked()==0) {
            fixOrder.setIsChecked(1);
            checkBox.setChecked(true);
            layout.setBackgroundResource(R.color.color_login_default);
        } else {
            fixOrder.setIsChecked(0);
            checkBox.setChecked(false);
            layout.setBackgroundResource(R.color.color_white);
        }

    }

    public CheckBox getCheckBox(){
        return checkBox;
    }
}
