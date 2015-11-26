package kr.whatshoe.whatShoe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


/*************************************************************
 * Content Fragment shown for the first page.
 * not used now
 * Used at MainActivity.
 *************************************************************/
public class ContentFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private ImageView contentImgUpper;
    private ImageView contentImg1;
    private ImageView contentImg2;
    private ImageView contentImg3;
    private ImageView contentImg4;
    private SharedPreferences orderPreferences;
    private SharedPreferences loginPreferences;

    public ContentFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(kr.whatshoe.whatShoe.R.layout.fragment_content, container, false);
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        RelativeLayout layout = (RelativeLayout) rootView.findViewById(kr.whatshoe.whatShoe.R.id.service_layout);
        layout.setOnClickListener(this);
        RelativeLayout upperLayout = (RelativeLayout) rootView.findViewById(R.id.upper_content_layout);
        upperLayout.setOnClickListener(this);
        upperLayout.setOnTouchListener(this);
        RelativeLayout contentLayout1 = (RelativeLayout) rootView.findViewById(kr.whatshoe.whatShoe.R.id.contentLayout1);
        contentLayout1.setOnClickListener(this);
        contentLayout1.setOnTouchListener(this);
        RelativeLayout contentLayout2 = (RelativeLayout) rootView.findViewById(kr.whatshoe.whatShoe.R.id.contentLayout2);
        contentLayout2.setOnClickListener(this);
        contentLayout2.setOnTouchListener(this);
        RelativeLayout contentLayout3 = (RelativeLayout) rootView.findViewById(kr.whatshoe.whatShoe.R.id.contentLayout3);
        contentLayout3.setOnClickListener(this);
        contentLayout3.setOnTouchListener(this);
        RelativeLayout contentLayout4 = (RelativeLayout) rootView.findViewById(kr.whatshoe.whatShoe.R.id.contentLayout4);
        contentLayout4.setOnClickListener(this);
        contentLayout4.setOnTouchListener(this);
        contentImgUpper = (ImageView) rootView.findViewById(R.id.contentImgUpper);
        contentImg1 = (ImageView) rootView.findViewById(R.id.contentImg1);
        contentImg2 = (ImageView) rootView.findViewById(R.id.contentImg2);
        contentImg3 = (ImageView) rootView.findViewById(R.id.contentImg3);
        contentImg4 = (ImageView) rootView.findViewById(R.id.contentImg4);

        return rootView;
    }

    @Override
    public void onResume() {

        MainActivity.currentFragment = MainActivity.FRAGMENT_FLAG_CONTENT;
        if (!MainActivity.needLogin) {
            if (!loginPreferences.contains("id")) {
                getActivity().finish();
                Log.i("notLogin", "notLogin");
            }
        }
        super.onResume();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case kr.whatshoe.whatShoe.R.id.service_layout:
                getActivity().getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.whatShoe.R.id.container, new ServiceFragment()).commit();
                break;
            case R.id.upper_content_layout:
                startContentDetail(ContentDetailActivity.class, 0);
                break;
            case kr.whatshoe.whatShoe.R.id.contentLayout1:
                startContentDetail(ContentDetailActivity.class, 1);
                break;
            case kr.whatshoe.whatShoe.R.id.contentLayout2:
                startContentDetail(ContentDetailActivity.class, 2);
                break;
            case kr.whatshoe.whatShoe.R.id.contentLayout3:
                startContentDetail(ContentDetailActivity.class, 3);
                break;
            case kr.whatshoe.whatShoe.R.id.contentLayout4:
                startContentDetail(ContentDetailActivity.class, 4);
                break;
            default:
                break;
        }
    }

    private void onPressedEffect(ImageView img, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            img.setColorFilter(Color.argb(70, 50, 50, 50));
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            img.clearColorFilter();
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            img.clearColorFilter();
        }
    }

    private void startContentDetail(Class<?> cls, int num) {
        Intent intent = new Intent();
        intent.putExtra("content", num);
        intent.setClass(getActivity(), cls);
        getActivity().startActivity(intent);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.upper_content_layout:
                onPressedEffect(contentImgUpper, event);
                break;
            case kr.whatshoe.whatShoe.R.id.contentLayout1:
                onPressedEffect(contentImg1, event);
                break;
            case kr.whatshoe.whatShoe.R.id.contentLayout2:
                onPressedEffect(contentImg2, event);
                break;
            case kr.whatshoe.whatShoe.R.id.contentLayout3:
                onPressedEffect(contentImg3, event);
                break;
            case kr.whatshoe.whatShoe.R.id.contentLayout4:
                onPressedEffect(contentImg4, event);
                break;
            default:
                break;
        }

        return false;
    }
}
