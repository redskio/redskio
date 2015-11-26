package kr.whatshoe.Order;

import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by jaewoo on 2015-08-20.
 */
public class NormalOrderLayout extends OrderLayout {
    ImageView img;
    public NormalOrderLayout(RelativeLayout layout, CheckBox checkBox, FixOrder fixOrder) {
        super(layout, checkBox, fixOrder);
    }
    public NormalOrderLayout(RelativeLayout layout, CheckBox checkBox, FixOrder fixOrder,ImageView img) {
        super(layout, checkBox, fixOrder);
        this.layout = layout;
        this.checkBox = checkBox;
        this.fixOrder = fixOrder;
        this.img = img;
    }

    @Override
    public void initLayout() {
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()== MotionEvent.ACTION_DOWN) {
                    checkOrder();
                    changeImageStatus();
                }
                return false;
            }
        });
    }
    private void changeImageStatus(){
        if(img==null){
            return;
        }
        if(this.fixOrder.getIsChecked()>0){
            img.setVisibility(View.VISIBLE);
        } else {
            img.setVisibility(View.GONE);
        }
    }
}
