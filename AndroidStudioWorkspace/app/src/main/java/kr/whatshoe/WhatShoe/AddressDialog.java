package kr.whatshoe.whatShoe;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by jaewoo on 2015-08-20.
 */
public class AddressDialog extends Dialog {
    Context context;
    TextView currentText ;
    TextView detailText ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.8f;
        getWindow().setAttributes(params);
        setContentView(R.layout.address_dialog);
        setLayout();

    }

    public AddressDialog(Context context) {
        // Dialog 배경을 투명 처리 해준다.

        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
    }
    public AddressDialog(Context context, TextView current , TextView Detail) {
        // Dialog 배경을 투명 처리 해준다.
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
        this.currentText = current;
        this.detailText = Detail;
    }

    private void setLayout() {
        ImageButton closeBtn = (ImageButton)findViewById(R.id.address_close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        final EditText locationDialogText = (EditText)findViewById(R.id.locationDialogText);
        locationDialogText.setText(currentText.getText());
        final EditText locationDialogDetail = (EditText)findViewById(R.id.locationDialogDetail);
        locationDialogDetail.setText(detailText.getText());
        Button registerBtn  = (Button)findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentText.setText(locationDialogText.getText().toString());
                detailText.setText(locationDialogDetail.getText().toString());
                close();
            }
        });


}

    public void close() {
        this.dismiss();
    }

}


